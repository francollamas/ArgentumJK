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

public class UpdateTagAndStatusResponse extends ServerPacket {
	// UpdateTagAndStatus,i:charIndex,b:criminal,s:tag
	@Override
	public ServerPacketID id() {
		return ServerPacketID.UpdateTagAndStatus;
	}
	public short charIndex;
	public byte criminal;
	public String tag;
	public UpdateTagAndStatusResponse(short charIndex,byte criminal,String tag){
		this.charIndex = charIndex;
		this.criminal = criminal;
		this.tag = tag;
	}
	public static UpdateTagAndStatusResponse decode(ByteBuf in) {    
		try {                                   
			short charIndex = readShort(in);
			byte criminal = readByte(in);
			String tag = readStr(in);
			return new UpdateTagAndStatusResponse(charIndex,criminal,tag);                  
		} catch (IndexOutOfBoundsException e) { 
			return null;                        
		}                                       
	}                                        
	@Override
	public void encode(ByteBuf out) {
		writeByte(out,this.id().id());
		writeShort(out,charIndex);
		writeByte(out,criminal);
		writeStr(out,tag);
	}
};

