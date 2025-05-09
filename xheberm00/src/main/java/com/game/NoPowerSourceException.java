/*
 * authors:
 * 		Matyáš Hebert xhebrm00
 * 		Jan Ostatnický xostatj00
 *
 * The NoPowerSourceException representing the expection when there is no power source in the gamefield.
 */

package com.game;

/**
 * Class representing an expection that gets thrown when a required power source is not present in a new game.
 */
public class NoPowerSourceException extends RuntimeException {
	/**
     * Constructs a new {@code NoPowerSourceException}
	 * with a message: "No power source found!".
     */
	public NoPowerSourceException() {
		super("No power source found!");
	}
}
