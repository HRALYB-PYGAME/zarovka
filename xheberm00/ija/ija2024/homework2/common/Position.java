package ija.ija2024.homework2.common;

public class Position {
	int row, col;

	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Position position = (Position) obj;
		return row == position.row && col == position.col;
	}
}
