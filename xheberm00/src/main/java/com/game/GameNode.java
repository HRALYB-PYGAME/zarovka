/*
 * authors:
 * 		Matyáš Hebert xhebrm00
 * 		Jan Ostatnický xostatj00
 *
 * The GameNode class implementing the nodes of the game, their types, the turning of the nodes, their connectors and logic for managing observers.
 */ 

package com.game;

import java.util.ArrayList;
import java.util.List;

import ija.ija2024.tool.common.ToolField;

/**
 * Class representing a single node on a game board.
 * Each node have its position, type, its connectors and a type.
 */
public class GameNode implements ToolField {
	Position position;
	boolean link, bulb, power;
	boolean east_connector, north_connector, south_connector, west_connector;
	boolean isPowered = false;
	List<Observer> observers = new ArrayList<>();

	/**
	 * Returns a type of the connections of this node.
	 * 
	 * @return an integer representing a type of connectors the node has.
     * <ul>
     *   <li>0 - no connectors</li>
     *   <li>1 - one connector</li>
     *   <li>2 - two connectors on opposing sides</li>
     *   <li>3 - two connectors on bordering sides</li>
     *   <li>4 - three connectors</li>
     *   <li>5 - four connectors</li>
     * </ul>
	 */
    public int connectorsType(){
        int count = 0;
        if (north_connector) count++;
        if (south_connector) count++;
        if (east_connector) count++;
        if (west_connector) count++;
    
        if (count == 0) {
            return 0;
        } else if (count == 1) {
            return 1;
        } else if (count == 2) {
            if ((north_connector && south_connector) || (east_connector && west_connector)) {
                return 2;
            } else {
                return 3;
            }
        } else if (count == 3) {
            return 4;
        } else {
            return 5;
        }
    }

	/**
	 * Returns a string describing the current state and type of this node.
	 * 
	 * @return a string that includes all important info about the GameNode
	 */
	@Override public String toString() {
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

	/** 
	 * @return true if the GameNode is powered; false otherwise
	 */
	public boolean light() {
		return isPowered;
	}

	/**
	 * Power this node.
	 */
	public void setLight() {
		isPowered = true;
	}

	/**
	 * Unpower this node.
	 */
	public void destroyLight() {
		isPowered = false;
	}

	/**
     * Sets the position of this node on the game board.
     * 
     * @param p position
     */
	public void setPosition(Position p) {
		position = p;
	}

	/**
	 * @return Position representing current position of this node
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Changes a type of this node to link.
	*/
	public void link() {
		link = true;
		bulb = false;
		power = false;
	}

	/**
	 * @return true if the GameNode is link; false otherwise
	 */
	public boolean isLink() {
		return link;
	}
	
	/**
	 * Changes a type of this node to bulb.
	*/
	public void bulb() {
		bulb = true;
		link = false;
		power = false;
	}

	/**
	 * @return true if the GameNode is bulb; false otherwise
	 */
	public boolean isBulb() {
		return bulb;
	}

	/**
	 * Changes a type of this node to power.
	*/
	public void power() {
		power = true;
		link = false;
		bulb = false;
	}

	/**
	 * @return true if the GameNode is power; false otherwise
	 */
	public boolean isPower() {
		return power;
	}

	/**
	 * @return true if the GameNode is empty; false otherwise
	 */
    public boolean isEmpty() {
        return !(power || link || bulb);
    }

	/**
	 * Rotate this node by 90 degrees clockwise and notify observers.
	 */
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

	/**
	 * Checks whether this node is connected via a connector on a certain Side.
	 * @param s Side to check the connection on.
	 * @return true if connector on Side s exists; false otherwise
	 */
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

	/**
	 * Adds a connector on a certain Side.
	 * @param s Side to add a connector to.
	 */
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

	/**
	 * Remove all connectors of this node.
	 */
	public void resetSides() {
		east_connector = north_connector = south_connector = west_connector = false;
	}
	
	/**
	 * @return true if node contains a connector on the north side; false otherwise
	 */
	public boolean north() {
		return  north_connector;
	}

	/**
	 * @return true if node contains a connector on the south side; false otherwise
	 */
	public boolean south() {
		return  south_connector;
	}

	/**
	 * @return true if node contains a connector on the east side; false otherwise
	 */
	public boolean east() {
		return  east_connector;
	}

	/**
	 * @return true if node contains a connector on the west side; false otherwise
	 */
	public boolean west() {
		return west_connector;
	}

	/**
	 * @param o Observer to add
	 */
	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	/**
	 * Notify all observers.
	 */
	@Override
	public void notifyObservers() {
		for (Observer o : observers) {
			o.update(this);
		}
	}
	
	/**
	 * Remove an observer.
	 * 
	 * @param o Observer to remove
	 */
	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}
}
