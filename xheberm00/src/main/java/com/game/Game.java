package com.game;

import ija.ija2024.tool.common.ToolEnvironment;
import ija.ija2024.tool.common.ToolField;
import ija.ija2024.tool.common.Observable;

public class Game implements ToolEnvironment, Observable.Observer {
	int row, col;
	static GameNode playfield[][];
	boolean power_in_game = false;

	@Override
	public void update(Observable o){
		init();	
	}

	public static Game create(int rows, int cols) {
		if (rows <= 0 || cols <= 0) {
			throw new IllegalArgumentException();
		}
		
		Game game = new Game();
		playfield = new GameNode[rows][cols];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				GameNode node = new GameNode();
				Position p = new Position(i + 1, j + 1);
				node.setPosition(p);
				playfield[i][j] = node;
			}
		}

		game.setRows(rows);
		game.setCols(cols);
		return game;
	}


	public void init() {
		int power_row = -1;
		int power_col = -1;
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < cols(); j++) {
				playfield[i][j].destroyLight();
				if (playfield[i][j].isPower()) {
					power_row = i;
					power_col = j;
				}
			}
		}

		if (power_row == -1) {
			throw new NoPowerSourceException();
		}

		testIfPowered(power_row, power_col);
	}

	public void rotateAll(){
        for(int r = 1; r < this.row; r++){
            for(int c = 1; c < this.col; c++){
                GameNode node = this.node(new Position(r, c));
                if(!node.isEmpty()){
                    int random = (int)(Math.random() * 4);
                    for(int i = 0; i < random; i++){
                        node.turn();
                    }
                }
            }
        }
    }

	public void testIfPowered(int row, int col) {
		if (playfield[row][col].light()) return;
		playfield[row][col].setLight();
		GameNode cell = playfield[row][col];

		if (row != 0) {
			if (cell.north() && playfield[row - 1][col].south()) {
				testIfPowered(row - 1, col);
			}
		}
		
		if (col != 0) {
			if (cell.west() && playfield[row][col - 1].east()) {
				testIfPowered(row, col - 1);
			}
		}
		
		if (row != rows() - 1) {
			if (cell.south() && playfield[row + 1][col].north()) {
				testIfPowered(row + 1, col);
			}
		}
		
		if (col != cols() - 1) {
			if (cell.east() && playfield[row][col + 1].west()) {
				testIfPowered(row, col + 1);
			}
		}
		
	}

	public GameNode createLinkNode(Position p, Side... sides) {
		if (p.getRow() <= 0 || p.getCol() <= 0 || p.getRow() > rows() || p.getCol() > cols() || sides.length < 2 || sides.length > 4) {
			return null;
		}

		GameNode node = new GameNode();
		node.setPosition(p);
		node.link();
		node.resetSides();

		for (Side s : sides) {
			if (s == Side.SOUTH) {
				node.setSide(s);
			} else if (s == Side.NORTH) {
				node.setSide(s);
			} else if (s == Side.EAST) {
				node.setSide(s);
			} else if (s == Side.WEST) {
				node.setSide(s);
			} else {
				return null;
			}
		}

		playfield[p.getRow() - 1][p.getCol() - 1] = node;
		node.addObserver(this);
		return node;
	}

	public GameNode createPowerNode(Position p, Side... sides) {
		if (p.getRow() <= 0 || p.getCol() <= 0 || p.getRow() > row || p.getCol() > col || power_in_game || sides.length < 1 || sides.length > 4) {
			return null;
		}
	
		GameNode node = new GameNode();
		node.setPosition(p);
		node.power();
		node.resetSides();

		for (Side s : sides) {
			if (s == Side.SOUTH) {
				node.setSide(s);
			} else if (s == Side.NORTH) {
				node.setSide(s);
			} else if (s == Side.EAST) {
				node.setSide(s);
			} else if (s == Side.WEST) {
				node.setSide(s);
			} else {
				return null;
			}
		}

		playfield[p.getRow() - 1][p.getCol() - 1] = node;
		power_in_game = true;
		node.addObserver(this);
		return node;
	}

	public GameNode createBulbNode(Position p, Side sides) {
		if (p.getRow() <= 0 || p.getCol() <= 0 || p.getRow() > row || p.getCol() > col) {
			return null;
		}
		
		GameNode node = new GameNode();
		node.setPosition(p);
		node.bulb();
		node.resetSides();

		if (sides == Side.SOUTH) {
			node.setSide(sides);
		} else if (sides == Side.NORTH) {
			node.setSide(sides);
		} else if (sides == Side.EAST) {
			node.setSide(sides);
		} else if (sides == Side.WEST) {
			node.setSide(sides);
		} else {
			return null;
		}

		playfield[p.getRow() - 1][p.getCol() - 1] = node;
		node.addObserver(this);
		return node;
	}

	public void setRows(int row) {
		this.row = row;
	}

	public int rows() {
		return row;
	}

	public void setCols(int col) {
		this.col = col;
	}

	public int cols() {
		return col;
	}

	public GameNode node(Position p) {
		return playfield[p.getRow() - 1][p.getCol() - 1];
	}

	public ToolField fieldAt(int row, int col) {
		return playfield[row][col];
	}
}
