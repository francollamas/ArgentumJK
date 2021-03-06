package com.argentumjk.server.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MapConstraint {
	
	NONE("NO"),
	NEWBIE("NEWBIE"),
	FACTION("FACCION"),
	ROYAL_ARMY("ARMADA"),
	DARK_LEGION("CAOS");
	
	private String name;

	private MapConstraint(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	private static MapConstraint[] VALUES = MapConstraint.values();
	
	public static MapConstraint fromName(String value) {
		if (value == null) {
			return NONE;
		}
		for (MapConstraint constraint : VALUES) {
			if (constraint.toString().equalsIgnoreCase(value.trim())) {
				return constraint;
			}
		}
		return NONE;
	}
	
	public static List<String> getNames() {
		List<String> list = new ArrayList<>();
		for (MapConstraint v : VALUES) {
			String s = v.name;
			list.add(s);
		}
		return list;
	}
	
}