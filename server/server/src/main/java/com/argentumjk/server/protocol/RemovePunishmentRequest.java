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

public class RemovePunishmentRequest extends ClientPacket {
	// RemovePunishment,s:userName,b:punishment,s:newText
	@Override
	public ClientPacketID id() {
		return ClientPacketID.RemovePunishment;
	}
	public String userName;
	public byte punishment;
	public String newText;
	public RemovePunishmentRequest(String userName,byte punishment,String newText){
		this.userName = userName;
		this.punishment = punishment;
		this.newText = newText;
	}
	public static RemovePunishmentRequest decode(ByteBuf in) {    
		try {                                   
			String userName = readStr(in);
			byte punishment = readByte(in);
			String newText = readStr(in);
			return new RemovePunishmentRequest(userName,punishment,newText);                  
		} catch (IndexOutOfBoundsException e) { 
			return null;                        
		}                                       
	}                                        
};

