/*******************************************************************************
 *     Gorlok AO, an implementation of Argentum Online using Java.
 *     Copyright (C) 2019 Pablo Fernando Lillia «gorlok» 
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.argentumjk.server.user;

import static com.argentumjk.server.Constants.DAT_DIR;
import static com.argentumjk.server.Constants.Leña;
import static com.argentumjk.server.Constants.LingoteHierro;
import static com.argentumjk.server.Constants.LingoteOro;
import static com.argentumjk.server.Constants.LingotePlata;
import static com.argentumjk.server.Constants.SERRUCHO_CARPINTERO;
import static com.argentumjk.server.Constants.SOUND_LABURO_CARPINTERO;
import static com.argentumjk.server.Constants.SOUND_MARTILLO_HERRERO;

import java.io.File;
import java.util.ArrayList;


;
import com.argentumjk.server.GameServer;
import com.argentumjk.server.ObjType;
import com.argentumjk.server.ObjectInfo;
import com.argentumjk.server.Skill;
import com.argentumjk.server.inventory.InventoryObject;
import com.argentumjk.server.map.Map;
import com.argentumjk.server.net.BlacksmithArmors_DATA;
import com.argentumjk.server.net.BlacksmithWeapons_DATA;
import com.argentumjk.server.net.CarpenterObjects_DATA;
import com.argentumjk.server.protocol.BlacksmithArmorsResponse;
import com.argentumjk.server.protocol.BlacksmithWeaponsResponse;
import com.argentumjk.server.protocol.CarpenterObjectsResponse;
import com.argentumjk.server.util.FontType;
import com.argentumjk.server.util.IniFile;
import com.badlogic.gdx.Gdx;

// ESTO PROVIENE DE TRABAJO.BAS
public class Work {
	
	
    private short[] armasHerrero;
    private short[] armadurasHerrero;
    private short[] objCarpintero;
    
    private GameServer server;
    
	public Work(GameServer server) {
		super();
		this.server = server;
	}

    public short[] getArmasHerrero() {
        return this.armasHerrero;
    }

    public short[] getArmadurasHerrero() {
        return this.armadurasHerrero;
    }

    public short[] getObjCarpintero() {
        return this.objCarpintero;
    }
	
	public ObjectInfo findObj(int oid) {
		return GameServer.instance().findObj(oid);
	}

	private boolean tieneObjetos(User user, short objid, int cant) {
		int total = 0;
		for (int i = 1; i <= user.getUserInv().getSize(); i++) {
			if (user.getUserInv().getObject(i).objid == objid) {
				total += user.getUserInv().getObject(i).cant;
			}
		}
		return (cant <= total);
	}

	private void herreroQuitarMateriales(User user, short objid) {
		ObjectInfo info = findObj(objid);
		if (info.LingH > 0) {
			user.quitarObjetos(LingoteHierro, info.LingH);
		}
		if (info.LingP > 0) {
			user.quitarObjetos(LingotePlata, info.LingP);
		}
		if (info.LingO > 0) {
			user.quitarObjetos(LingoteOro, info.LingO);
		}
	}

	public void carpinteroQuitarMateriales(User user, short objid) {
		ObjectInfo info = findObj(objid);
		if (info.Madera > 0) {
			user.quitarObjetos(Leña, info.Madera);
		}
	}

	private boolean carpinteroTieneMateriales(User user, short objid) {
		ObjectInfo info = findObj(objid);
		if (info.Madera > 0) {
			if (!tieneObjetos(user, Leña, info.Madera)) {
				user.sendMessage("No tenes suficientes madera.", FontType.FONTTYPE_INFO);
				return false;
			}
		}
		return true;
	}

	private boolean herreroTieneMateriales(User user, short objid) {
		ObjectInfo info = findObj(objid);
		if (info.LingH > 0) {
			if (!tieneObjetos(user, LingoteHierro, info.LingH)) {
				user.sendMessage("No tienes suficientes lingotes de hierro.", FontType.FONTTYPE_INFO);
				return false;
			}
		}
		if (info.LingP > 0) {
			if (!tieneObjetos(user, LingotePlata, info.LingP)) {
				user.sendMessage("No tienes suficientes lingotes de plata.", FontType.FONTTYPE_INFO);
				return false;
			}
		}
		if (info.LingO > 0) {
			if (!tieneObjetos(user, LingoteOro, info.LingO)) {
				user.sendMessage("No tienes suficientes lingotes de oro.", FontType.FONTTYPE_INFO);
				return false;
			}
		}
		return true;
	}

	private boolean puedeConstruir(User user, short objid) {
		ObjectInfo info = findObj(objid);
		return herreroTieneMateriales(user, objid) 
				&& user.skills().get(Skill.SKILL_Herreria) >= info.SkHerreria;
	}

	private boolean puedeConstruirHerreria(short objid) {
		for (int i = 0; i < getArmasHerrero().length; i++) {
			if (getArmasHerrero()[i] == objid) {
				return true;
			}
		}
		for (int i = 0; i < getArmadurasHerrero().length; i++) {
			if (getArmadurasHerrero()[i] == objid) {
				return true;
			}
		}
		return false;
	}

	private void herreroConstruirItem(User user, short objid) {
		if (puedeConstruir(user, objid) && puedeConstruirHerreria(objid)) {
			Map mapa = this.server.getMap(user.pos().map);
			if (mapa == null) {
				return;
			}
			ObjectInfo info = findObj(objid);
			herreroQuitarMateriales(user, objid);
			if (info.objType == ObjType.Weapon) {
				user.sendMessage("Has construido el arma!.", FontType.FONTTYPE_INFO);
			} else if (info.objType == ObjType.ESCUDO) {
				user.sendMessage("Has construido el escudo!.", FontType.FONTTYPE_INFO);
			} else if (info.objType == ObjType.CASCO) {
				user.sendMessage("Has construido el casco!.", FontType.FONTTYPE_INFO);
			} else if (info.objType == ObjType.Armadura) {
				user.sendMessage("Has construido la armadura!.", FontType.FONTTYPE_INFO);
			}
			if (user.getUserInv().agregarItem(objid, 1) < 1) {
				mapa.dropItemOnFloor(user.pos().x, user.pos().y, new InventoryObject(objid, 1));
			}
			user.riseSkill(Skill.SKILL_Herreria);
			user.sendInventoryToUser();
			user.sendWave(SOUND_MARTILLO_HERRERO);
			user.getFlags().Trabajando = true;
		}
	}

	private boolean puedeConstruirCarpintero(short objid) {
		for (int i = 0; i < getObjCarpintero().length; i++) {
			if (getObjCarpintero()[i] == objid) {
				return true;
			}
		}
		return false;
	}

	private void carpinteroConstruirItem(User user, short objid) {
		ObjectInfo info = findObj(objid);
		Map mapa = this.server.getMap(user.pos().map);
		if (mapa == null) {
			return;
		}
		if (carpinteroTieneMateriales(user, objid) 
				&& user.skills().get(Skill.SKILL_Carpinteria) >= info.SkCarpinteria
				&& puedeConstruirCarpintero(objid) 
				&& user.getUserInv().getArma().ObjIndex == SERRUCHO_CARPINTERO) {
			carpinteroQuitarMateriales(user, objid);
			user.sendMessage("¡Has construido el objeto!", FontType.FONTTYPE_INFO);
			if (user.getUserInv().agregarItem(objid, 1) < 1) {
				mapa.dropItemOnFloor(user.pos().x, user.pos().y, new InventoryObject(objid, 1));
			}
			user.riseSkill(Skill.SKILL_Carpinteria);
			user.sendInventoryToUser();
			user.sendWave(SOUND_LABURO_CARPINTERO);
			user.getFlags().Trabajando = true;
		}
	}

	public void craftBlacksmith(User user, short objid) {
		if (objid < 1) {
			return;
		}
		ObjectInfo info = findObj(objid);
		if (info.SkHerreria == 0) {
			return;
		}
		herreroConstruirItem(user, objid);
	}

	public void craftCarpenter(User user, short objid) {
		if (objid < 1) {
			return;
		}
		ObjectInfo info = findObj(objid);
		if (info.SkCarpinteria == 0) {
			return;
		}
		carpinteroConstruirItem(user, objid);
	}

    private void loadBlacksmithingWeapons() {
    	Gdx.app.log("Trace: ", "loading blacksmithing weapons");
        try {
            IniFile ini = new IniFile(DAT_DIR + File.separator + "ArmasHerrero.dat");
            short cant = ini.getShort("INIT", "NumArmas");
            this.armasHerrero = new short[cant];
            Gdx.app.debug("Debug: ", "ArmasHerreria cantidad=" + cant);
            for (int i = 0; i < cant; i++) {
                this.armasHerrero[i] = ini.getShort("Arma" + (i+1), "Index");
                Gdx.app.debug("Debug: ", "ArmasHerrero[" + i + "]=" + this.armasHerrero[i]);
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBlacksmithingArmors() {
    	Gdx.app.log("Trace: ", "loading backsmithing armors");
        try {
            IniFile ini = new IniFile(DAT_DIR + File.separator + "ArmadurasHerrero.dat");
            short cant = ini.getShort("INIT", "NumArmaduras");
            Gdx.app.debug("Debug: ", "ArmadurasHerrero cantidad=" + cant);
            this.armadurasHerrero = new short[cant];
            for (int i = 0; i < cant; i++) {
                this.armadurasHerrero[i] = ini.getShort("Armadura" + (i+1), "Index");
                Gdx.app.debug("Debug: ", "ArmadurasHerrero[" + i + "]=" + this.armadurasHerrero[i]);
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCarpentryObjects() {
    	Gdx.app.log("Trace: ", "loading carpentry objects");
        try {
            IniFile ini = new IniFile(DAT_DIR + File.separator + "ObjCarpintero.dat");
            short cant = ini.getShort("INIT", "NumObjs");
            Gdx.app.debug("Debug: ", "ObjCarpintero cantidad=" + cant);
            this.objCarpintero = new short[cant];
            for (int i = 0; i < cant; i++) {
                this.objCarpintero[i] = ini.getShort("Obj" + (i+1), "Index");
                Gdx.app.debug("Debug: ", "ObjCarpintero[" + i + "]=" + this.objCarpintero[i]);
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

	public void loadCraftableObjects() {
        loadBlacksmithingWeapons();
        loadBlacksmithingArmors();
        loadCarpentryObjects();
	}

    public void sendBlacksmithWeapons(User user) {
		ArrayList<BlacksmithWeapons_DATA> validWeapons = new ArrayList<BlacksmithWeapons_DATA>();
    	for (short objid : getArmasHerrero()) {
            ObjectInfo info = findObj(objid);
            if (info.SkHerreria <= user.skillHerreriaEfectivo()) {
            	validWeapons.add(new BlacksmithWeapons_DATA(info.Nombre, info.LingH, info.LingP, info.LingO, objid));
            }
    	}

        user.sendPacket(
        		new BlacksmithWeaponsResponse(
					(short) validWeapons.size(), 
					validWeapons.toArray(new BlacksmithWeapons_DATA[0])));
    }
 
    public void sendCarpenterObjects(User user) {
		ArrayList<CarpenterObjects_DATA> validObjects = new ArrayList<CarpenterObjects_DATA>();
    	for (short objid : getObjCarpintero()) {
            ObjectInfo info = findObj(objid);
            if (info.SkHerreria <= user.skillCarpinteriaEfectivo()) {
            	validObjects.add(new CarpenterObjects_DATA(info.Nombre, (short)info.Madera, objid));
            }
    	}

        user.sendPacket(
        		new CarpenterObjectsResponse(
        				(short) validObjects.size(), 
        				validObjects.toArray(new CarpenterObjects_DATA[0])));
    }

    public void sendBlacksmithArmors(User user) {
		ArrayList<BlacksmithArmors_DATA> validArmaduras = new ArrayList<BlacksmithArmors_DATA>();
    	for (short objid : getArmadurasHerrero()) {
            ObjectInfo info = findObj(objid);
            if (info.SkHerreria <= user.skillHerreriaEfectivo()) {
            	validArmaduras.add(new BlacksmithArmors_DATA(info.Nombre, info.LingH, info.LingP, info.LingO, objid));
            }
    	}

        user.sendPacket(
        		new BlacksmithArmorsResponse(
        				(short) validArmaduras.size(), 
        				validArmaduras.toArray(new BlacksmithArmors_DATA[0])));
    }

}
