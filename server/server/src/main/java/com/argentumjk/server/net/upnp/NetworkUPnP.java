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
package com.argentumjk.server.net.upnp;

import com.argentumjk.server.Constants;
import com.badlogic.gdx.Gdx;

public class NetworkUPnP {

	private NetworkUPnP() {}

    public static void openUPnP() {
		Gdx.app.log("Warn: ", "Attempting UPnP port forwarding...");
        if (UPnP.isUPnPAvailable()) { //is UPnP available?
            if (UPnP.isMappedTCP(Constants.SERVER_PORT)) { //is the port already mapped?
                Gdx.app.log("Warn: ", "UPnP port forwarding not enabled: port is already mapped");
            } else if (UPnP.openPortTCP(Constants.SERVER_PORT)) { //try to map port
            	Gdx.app.log("Warn: ", "UPnP port forwarding enabled");
            } else {
            	Gdx.app.log("Warn: ", "UPnP port forwarding failed");
            }
        } else {
        	Gdx.app.log("Warn: ", "UPnP is not available");
        }
    }
}
