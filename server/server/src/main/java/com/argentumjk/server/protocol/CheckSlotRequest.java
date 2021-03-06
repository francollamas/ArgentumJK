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
package com.argentumjk.server.protocol;

import com.argentumjk.server.net.*;

import io.netty.buffer.ByteBuf;

public class CheckSlotRequest extends ClientPacket {
	// CheckSlot,s:userName,b:slot
	@Override
	public ClientPacketID id() {
		return ClientPacketID.CheckSlot;
	}
	public String userName;
	public byte slot;
	public CheckSlotRequest(String userName,byte slot){
		this.userName = userName;
		this.slot = slot;
	}
	public static CheckSlotRequest decode(ByteBuf in) {    
		try {                                   
			String userName = readStr(in);
			byte slot = readByte(in);
			return new CheckSlotRequest(userName,slot);                  
		} catch (IndexOutOfBoundsException e) { 
			return null;                        
		}                                       
	}                                        
};

