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

import com.argentumjk.server.Skill;
import com.argentumjk.server.net.*;

import io.netty.buffer.ByteBuf;

public class SendSkillsResponse extends ServerPacket {
	// SendSkills,b[NUMSKILLS]:skills
	@Override
	public ServerPacketID id() {
		return ServerPacketID.SendSkills;
	}
	public byte[] skills;
	public SendSkillsResponse(byte[] skills){
		this.skills = skills;
	}
	public static SendSkillsResponse decode(ByteBuf in) {    
		try {                                   
			byte[] skills = readBytes(in, Skill.values().length);
			return new SendSkillsResponse(skills);                  
		} catch (IndexOutOfBoundsException e) { 
			return null;                        
		}                                       
	}                                        
	@Override
	public void encode(ByteBuf out) {
		writeByte(out,this.id().id());
		writeBytes(out,skills);
	}
};

