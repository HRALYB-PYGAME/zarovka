package ija.ija2024.homework2.common;

import java.util.ArrayList;
import java.util.List;

import ija.ija2024.tool.common.ToolField;

public class GameNode implements ToolField {
	Position position;
	boolean link, bulb, power;
	boolean east_connector, north_connector, south_connector, west_connector;
	boolean isPowered = false;
	List<Observer> observers = new ArrayList<>();

	public String toString() {
		String info = "{";

		String type;
		if (isBulb()) type = "B";
		else if (isPower()) type = "P";
		else if (isLink()) type = "L";
		else type = "E";

		info = info.concat(type);
		info = info.concat("[" + String.valueOf(position.row) + "@" + String.valueOf(position.col) + "][");
		
		boolean flag = false;

		if (north_connector) {
			info = info.concat("NORTH");
			flag = true;
		}
		
		if (east_connector) {
			if (flag) info = info.concat(",");
			info = info.concat("EAST");
			flag = true;
		}

		if (south_connector) {
			if (flag) info = info.concat(",");
			info = info.concat("SOUTH");
			flag = true;
		}

		if (west_connector) {
			if (flag) info = info.concat(",");
			info = info.concat("WEST");
		}

		info = info.concat("]}");

		return info;
	}

	public boolean light() {
		return isPowered;
	}

	public void setLight() {
		isPowered = true;
	}

	public void destroyLight() {
		isPowered = false;
	}

	public void setPosition(Position p) {
		position = p;
	}

	public Position getPosition() {
		return position;
	}

	public void link() {
		link = true;
		bulb = false;
		power = false;
	}

	public boolean isLink() {
		return link;
	}
	
	public void bulb() {
		bulb = true;
		link = false;
		power = false;
	}

	public boolean isBulb() {
		return bulb;
	}

	public void power() {
		power = true;
		link = false;
		bulb = false;
	}

	public boolean isPower() {
		return power;
	}

	public void turn() {
		boolean east, north, south, west;
		
		east = east_connector;
		north = north_connector;
		south = south_connector;
		west = west_connector;

		east_connector = north_connector = south_connector = west_connector = false;

		if (east) south_connector = true;
		if (north) east_connector = true;
		if (south) west_connector = true;
		if (west) north_connector = true;
		
		notifyObservers();
	}

	public boolean containsConnector(Side s) {
		if (s == Side.EAST) {
			return east_connector;
		} else if (s == Side.NORTH) {
			return north_connector;
		} else if (s == Side.SOUTH) {
			return south_connector;
		} else if (s == Side.WEST) {
			return west_connector;
		} else return false;
	}

	public void setSide(Side s) {
		if (s == Side.EAST) {
			east_connector = true;
		} else if (s == Side.NORTH) {
			north_connector = true;
		} else if (s == Side.SOUTH) {
			south_connector = true;
		} else if (s == Side.WEST) {
			west_connector = true;
		}
	}

	public void resetSides() {
		east_connector = north_connector = south_connector = west_connector = false;
	}
	
	public boolean north() {
		return  north_connector;
	}

	public boolean south() {
		return  south_connector;
	}

	public boolean east() {
		return  east_connector;
	}

	public boolean west() {
		return west_connector;
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void notifyObservers() {
		for (Observer o : observers) {
			o.update(this);
		}
	}
	
	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}
}
