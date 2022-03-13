package com.argentumjk.server.gm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.argentumjk.server.Constants;
import com.argentumjk.server.GameServer;
import com.argentumjk.server.user.User;
import com.argentumjk.server.user.UserStorage;
import com.argentumjk.server.util.FontType;
import com.argentumjk.server.util.Log;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class BannIP {
	
	GameServer server;

    private List<String> bannedIPs = new ArrayList<>();
    
    public BannIP(GameServer server) {
    	this.server = server;
	}

    public List<String> getBannedIPs() {
        return this.bannedIPs;
    }
    
    public boolean isIpBanned(String ip) {
    	return getBannedIPs().contains(ip);    	
    }

	public void banUser(User admin, String userName, String reason) {
		// Comando /BAN
		if (!admin.isGod() && !admin.isAdmin()) {
			return;
		}
		User user = this.server.userByName(userName);
		if (user == null) {
			if (User.userExists(userName)) {
				user = new User(server);
				try {
					user.userStorage.loadUserFromStorageOffline(userName);
				} catch (IOException ignore) {
					return;
				}
			} else {
				admin.sendMessage("El usuario no existe.", FontType.FONTTYPE_INFO);
				return;							
			}
		}
		if (user.getFlags().privileges > admin.getFlags().privileges) {
			admin.sendMessage("No puedes /BAN a usuarios de mayor jerarquia a la tuya!", FontType.FONTTYPE_INFO);
			return;
		}
		server.sendMessageToAdmins(admin, admin.getUserName() + " /BAN a " + user.getUserName() + " por: " + reason, FontType.FONTTYPE_SERVER);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		UserStorage.addPunishment(userName, admin.getUserName() + ">> /BAN por: " + reason + ". " + sdf.format(new java.util.Date()));
		user.sendError("Has sido expulsado permanentemente del servidor.");
		user.banned(admin.getUserName(), reason);
		if (user.isLogged()) {
			user.quitGame();
		}
		UserStorage.banUser(user.getUserName(), admin.getUserName(), reason);
		Log.logGM(admin.getUserName(), "/BAN " + userName + " por: " + reason);
	}

	public void unbanUser(User admin, String userName) {
		// Comando /UNBAN
		if (!admin.isGod() && !admin.isAdmin()) {
			return;
		}
		if (!UserStorage.isUserBanned(userName)) {
			admin.sendMessage("No se puede perdonar, porque el usuario no está expulsado.", FontType.FONTTYPE_INFO);
			return;			
		}
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		UserStorage.addPunishment(userName, admin.getUserName() + ">> /UNBAN " + sdf.format(new java.util.Date()));
		UserStorage.unBanUser(userName);
		Log.logGM(admin.getUserName(), "/UNBAN a " + userName);
		admin.sendMessage(userName + " unbanned.", FontType.FONTTYPE_SERVER);
		server.sendMessageToAdmins(admin, admin.getUserName() + " /UNBAN " + userName + ".", FontType.FONTTYPE_SERVER);
	}
    
	public void banIPUser(User admin, String userName, String reason) {
		// Comando /BANIP
		if (!admin.isGod() && !admin.isAdmin()) {
			return;
		}
		User user = this.server.userByName(userName);
		if (user == null) {
			admin.sendMessage("Usuario desconectado.", FontType.FONTTYPE_INFO);
			return;
		}
		String bannedIP = user.getIP();
		banUser(admin, userName, reason);
		banIP(admin, bannedIP, reason);
	}

	public void banIP(User admin, String bannedIP, String reason) {
		// Comando /BANIP
		if (!admin.isGod() && !admin.isAdmin()) {
			return;
		}
		List<String> bannedIPs = getBannedIPs();
		if (bannedIPs.contains(bannedIP)) {
			admin.sendMessage("La IP " + bannedIP + " ya se encuentra en la lista de bans.", FontType.FONTTYPE_INFO);
			return;
		}
		bannedIPs.add(bannedIP);
		saveBannedIPList();
		server.sendMessageToAdmins(admin, admin.getUserName() + " Baneo la IP " + bannedIP, FontType.FONTTYPE_SERVER);
		
        // Find every user with that ip and ban him!
		server.getUsers().stream().forEach(p -> {
			if (p.getIP() == bannedIP) {
				banUser(admin, p.getUserName(), "Banned IP " + bannedIP + " por: " + reason);
			}
		});
	}

	public void unbanIP(User admin, String bannedIP) {
		// Comando /UNBANIP
		if (!admin.isGod() && !admin.isAdmin()) {
			return;
		}
		Log.logGM(admin.getUserName(), "/UNBANIP " + bannedIP);
		List<String> bannedIPs = getBannedIPs();
		if (bannedIPs.contains(bannedIP)) {
			bannedIPs.remove(bannedIP);
			saveBannedIPList();
			admin.sendMessage("La IP " + bannedIP + " se ha quitado de la lista de bans.", FontType.FONTTYPE_INFO);
			server.sendMessageToAdmins(admin, admin.getUserName() + " ha quitado la IP " + bannedIP + " de la lista de bans.", 
					FontType.FONTTYPE_SERVER);
		} else {
			admin.sendMessage("La IP " + bannedIP + " NO se encuentra en la lista de bans.", FontType.FONTTYPE_INFO);
		}
	}
	
	public void bannedIPList(User admin) {
		// Command /BANIPLIST
		if (!admin.isGM()) {
			return;
		}
		Log.logGM(admin.getUserName(), "/BANIPLIST");
	    
		if (getBannedIPs().isEmpty()) {
			admin.sendMessage("No hay banned IPs.", FontType.FONTTYPE_INFO);			
		} else {
			admin.sendMessage("Banned IPs: " + String.join(", ", getBannedIPs()), FontType.FONTTYPE_INFO);
		}
	}

	public void bannedIPReload(User admin) {
		// Command /BANIPRELOAD
		if (!admin.isGM()) {
			return;
		}
		Log.logGM(admin.getUserName(), "/BANIPRELOAD");

		loadBannedIPList();
	}
	
	public void loadBannedIPList() {
		final String fileName = Constants.DAT_DIR + File.separator + "BanIps.dat";
		this.bannedIPs.clear();

		FileHandle fileHandle = Gdx.files.internal(fileName);
		String text = fileHandle.readString();
		String ips[] = text.split("\\r?\\n");
		this.bannedIPs.addAll(Arrays.asList(ips));
	}
	
	public void saveBannedIPList() {
		final String fileName = Constants.DAT_DIR + File.separator + "BanIps.dat";
		try {
			Files.write(Paths.get(fileName), String.join("\n", this.bannedIPs).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
