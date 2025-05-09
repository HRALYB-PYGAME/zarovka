/*
 * authors:
 * 		Matyáš Hebert xheberm00
 * 		Jan Ostatnický xostatj00
 *
 * The Position class implementing the position of the nodes in the gamefield, overrides the eqauls method.
 */

package com.game;

/**
 * Class representing a position on a game grid with a row and col coordinates.
 */
public class Position {
	int row, col;

	/**
     * Constructs a Position with the specified row and col coordinates.
     *
     * @param row row coordinate
     * @param col col coordinate
     */
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}

	/**
	 * Returns the row coordinate of the Position
	 * 
	 * @return the row coordinate
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns the col coordinate of the Position
	 * 
	 * @return the col coordinate
	 */
	public int getCol() {
		return col;
	}

	/**
     * Indicates whether an object is identical to the Position.
     *
     * @param obj object with which to compare
     * @return {@code true} if objects are identical; {@code false} otherwise
     */
    @Override public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Position position = (Position) obj;
		return row == position.row && col == position.col;
	}
}
