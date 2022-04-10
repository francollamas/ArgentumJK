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

import static com.argentumjk.server.Constants.MAX_USER_PETS;

import java.util.ArrayList;
import java.util.List;

import com.argentumjk.server.npc.Npc;

public class UserPets {
	
	private List<Npc> pets = new ArrayList<>();
	
	public List<Npc> getPets() {
		return pets;
	}

	public boolean hasPets() {
		return this.pets.size() > 0;		
	}

	public boolean isFullPets() {
		return this.pets.size() >= MAX_USER_PETS;
	}
	
	public void addPet(Npc pet) {
		this.pets.add(pet);
	}
	
	public void removeAll() {
		for (Npc pet : pets) {
			pet.releasePet();
			pet.quitarNPC();
		}
		pets.clear();
	}
	
	public void removeInvocationPets() {
		List<Npc> n = new ArrayList<>(pets);
		for (Npc pet : n) {
			if (pet.isSpellSpawnedPet()) {
				removePet(pet);
				pet.quitarNPC();
			}
		}
	}
	
	/** Ordenar a las mascotas del usuario atacar a un Npc */
	public void petsAttackNpc(Npc targetNpc) {
		for (Npc pet : pets) {
			pet.setPetTargetNpc(targetNpc);
		}
	}

	public void petsFollowMaster() {
		for (Npc pet : pets) {
			pet.followMaster();
		}
	}

	/**
	 * Las mascotas que tienen como objetivo al Npc target, deben volver a seguir al
	 * amo
	 */
	public void petsFollowMaster(Npc targetNpc) {
		for (Npc pet : pets) {
			if (pet.targetNpc() == targetNpc) {
				pet.followMaster();
			}
		}
	}

	public void petsFollowMaster(short target) {
		for (Npc pet : pets) {
			if (pet.targetUser() == target) {
				pet.targetUser(0);
				pet.followMaster();
			}
		}
	}

	public void removePet(Npc npc) {
		if (pets.contains(npc)) {
			pets.remove(npc);
			npc.releasePet();
		}
	}
	
	public boolean canTame(int npcNumber) {
		// DomarMascota
		// This function checks how many NPCs of the same type have been tamed by the user.
		// Returns True if that amount is less than two.
		for (Npc pet: pets) {
			// y si es mascota de invocación?
			if (pet.getNumber() == npcNumber) {
				return false;
			}
		}
		return true;
	}
	
}
