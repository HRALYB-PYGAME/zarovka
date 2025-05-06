package ija.ija2024.homework2.common;

public class NoPowerSourceException extends RuntimeException {
	public NoPowerSourceException() {
		super("No power source found!");
	}
}
