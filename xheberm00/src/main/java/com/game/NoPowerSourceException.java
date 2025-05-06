package com.game;

public class NoPowerSourceException extends RuntimeException {
	public NoPowerSourceException() {
		super("No power source found!");
	}
}
