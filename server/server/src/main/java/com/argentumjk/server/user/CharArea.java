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

public class CharArea {

	public int areaID = 0;

	/** mask of current area on x-axis */
	public int currentAreaX = 0;
	/** mask of current area on y-axis */
	public int currentAreaY = 0;

	/** mask of areas to send on x-axis */
	public int areasToSendX = 0;
	/** mask of areas to send on y-axis */
	public int areasToSendY = 0;

	public int minX = 0;
	public int minY = 0;
	
	public void reset() {
		this.areaID = 0;
		this.currentAreaX = 0;
		this.currentAreaY = 0;
		this.areasToSendX = 0;
		this.areasToSendY = 0;
	}

}
