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
package com.argentumjk.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import com.argentumjk.server.util.IniFile;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * @author gorlok
 */
public class GamblerStats {
	private static final String APUESTAS_DAT = "apuestas.dat";

	private long ganancias = 0;
	private long perdidas = 0;
	private long jugadas = 0;

	public GamblerStats() {
		this.loadStats();
	}

	public long getGanancias() {
		return ganancias;
	}

	public long getPerdidas() {
		return perdidas;
	}

	public long getJugadas() {
		return jugadas;
	}

	public void loadStats() {
		Gdx.app.log("Trace: ", "loading gambler stats");

		try {
			IniFile ini = new IniFile(Constants.DAT_DIR + File.separator + APUESTAS_DAT);

			ganancias = ini.getLong("Main", "Ganancias");
			perdidas = ini.getLong("Main", "Perdidas");
			jugadas = ini.getLong("Main", "Jugadas");

		} catch (FileNotFoundException e) {
			Gdx.app.error("Fatal: ", "", e);
		} catch (IOException e) {
			Gdx.app.error("Fatal: ", "", e);
		}
	}

	private void saveStats() {
		Gdx.app.log("Trace: ", "saving gambler stats");

		FileHandle fileHandle = Gdx.files.local(Constants.DAT_DIR + File.separator + APUESTAS_DAT);
		try {
			if (!fileHandle.exists()) {
				fileHandle.writeString("", false);
			}
			IniFile ini = new IniFile(Constants.DAT_DIR + File.separator + APUESTAS_DAT, Files.FileType.Local);

			ini.setValue("Main", "Ganancias", ganancias);
			ini.setValue("Main", "Perdidas", perdidas);
			ini.setValue("Main", "Jugadas", jugadas);
			
			ini.store(Constants.DAT_DIR + File.separator + APUESTAS_DAT);
		} catch (IOException e) {
			Gdx.app.error("Fatal: ", "", e);
		}
	}

	public void incrementLost(int gold) {
		this.perdidas += gold;
		this.jugadas++;

		saveStats();
	}

	public void incrementWins(int gold) {
		this.ganancias += gold;
		this.jugadas++;

		saveStats();
	}

}
