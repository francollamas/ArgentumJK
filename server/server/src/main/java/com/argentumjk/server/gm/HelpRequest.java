package com.argentumjk.server.gm;

import java.util.ArrayList;
import java.util.List;

import com.argentumjk.server.Constants;
import com.argentumjk.server.GameServer;
import com.argentumjk.server.protocol.ShowSOSFormResponse;
import com.argentumjk.server.user.User;
import com.argentumjk.server.util.FontType;
import com.argentumjk.server.util.Log;
import com.argentumjk.server.util.Util;

public class HelpRequest {
	
	private GameServer server;

    /** User names than asked for help */
    private List<String> helpRequests = new ArrayList<>();

	public HelpRequest(GameServer server) {
		super();
		this.server = server;
	}
    
    public List<String> helpRequests() {
        return this.helpRequests;
    }

	public void clearAllHelpRequestToGm(User admin) {
		// Comando /BORRAR SOS
		// Comando para borrar todos pedidos /GM pendientes
		if (!admin.isGM()) {
			return;
		}
    	this.helpRequests().clear();
		admin.sendMessage("Todos los /GM pendientes han sido eliminados.", FontType.FONTTYPE_INFO);
		Log.logGM(admin.getUserName(), "/BORRAR SOS");
	}

	public void askForHelpToGM(User user) {
		// Comando /GM
		// Pedir ayuda a los GMs.
		List<String> requests = helpRequests();
		if (!requests.contains(user.getUserName())) {
			requests.add(user.getUserName());
			user.sendMessage("El mensaje ha sido entregado, ahora solo debes esperar que se desocupe algun GM.",
					FontType.FONTTYPE_INFO);
		} else {
			requests.remove(user.getUserName());
			requests.add(user.getUserName());
			user.sendMessage(
					"Ya habias mandado un mensaje, tu mensaje ha sido movido al final de la cola de mensajes. Ten paciencia.",
					FontType.FONTTYPE_INFO);
		}
	}

	public void sendHelpRequests(User admin) {
		// Comando /SHOW SOS
		if (!admin.isGM()) {
			return;
		}
		String sosList = Util.join("" + Constants.NULL_CHAR, helpRequests);
		admin.sendPacket(new ShowSOSFormResponse(sosList));
	}

	public void removeHelpRequest(User admin, String userName) {
		// Comando SOSDONE
		if (!admin.isGM()) {
			return;
		}
		helpRequests().remove(userName);
	}
    
}
