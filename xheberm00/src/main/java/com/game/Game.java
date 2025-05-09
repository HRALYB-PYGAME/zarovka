package com.game;

import ija.ija2024.tool.common.ToolEnvironment;
import ija.ija2024.tool.common.ToolField;
import ija.ija2024.tool.common.Observable;

/**
 * Class representing a game board.
 * Including the board dimensions, 2D list of GameNodes,
 * 2D list of integers representing number of rotations from a correct rotation
 * and a boolean whether a power node is present on the game board.
 */
public class Game implements ToolEnvironment, Observable.Observer {
	int row, col;
	static GameNode playfield[][];
	int[][] rotations;
	boolean power_in_game = false;

	/**
	 * Updates a state of the game board.
	 */
	@Override
	public void update(Observable o){
		init();	
	}

	/**
	 * Creates a new game board with given dimensions.
	 * @param rows number of rows.
	 * @param cols number of columns.
	 * @return a new instance of Game.
	 */
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

		game.rotations = new int[rows][cols];
		return game;
	}


	/**
	 * Function that updates all the nodes and whether they are
	 * powered or not.
	 */
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

	/**
	 * Rotates all GameNodes that arent empty 0-3 times
	 * to ensure a random starting game state
	 */
	public void rotateAll(){
        for(int r = 1; r < this.row; r++){
            for(int c = 1; c < this.col; c++){
                GameNode node = this.node(new Position(r, c));
                if(!node.isEmpty()){
                    int random = (int)(Math.random() * 4);
					this.rotations[r-1][c-1] = random;
                    for(int i = 0; i < random; i++){
                        node.turn();
                    }
                }
            }
        }
    }

	/**
	 * Recursive function that turns all nodes on if they are connected
	 * to the power node. 
	 * 
	 * @param row number of the power node
	 * @param col number of the power node
	 */
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

	/**
	 * Creates a new link node on a specified position with given connectors.
	 * @param p Position of the new node
	 * @param sides where the conenctors should be placed
	 * @return GameNode if valid arguments; null otherwise
	 */
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

	/**
	 * Creates a new power node on a specified position with given connectors.
	 * @param p Position of the new node
	 * @param sides where the conenctors should be placed
	 * @return GameNode if valid arguments; null otherwise
	 */
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

	/**
	 * Creates a new bulb node on a specified position with given connectors.
	 * @param p Position of the new node
	 * @param sides where the conenctors should be placed
	 * @return GameNode if valid arguments; null otherwise
	 */
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

	/**
	 * Sets number of rows of this game board.
	 * @param row number of rows
	 */
	public void setRows(int row) {
		this.row = row;
	}

	/**
	 * @return number of rows
	 */
	public int rows() {
		return row;
	}

	/**
	 * Sets number of columns of this game board.
	 * @param col number of columns
	 */
	public void setCols(int col) {
		this.col = col;
	}

	/**
	 * @return number of columns
	 */
	public int cols() {
		return col;
	}

	/**
	 * Returns a GameNode at a specified position.
	 * 
	 * @param p position of the node
	 * @return the node at that position
	 */
	public GameNode node(Position p) {
		return playfield[p.getRow() - 1][p.getCol() - 1];
	}

	/**
	 * Returns a ToolField at a specified position.
	 * 
	 * @param row row
	 * @param col column
	 * @return ToolField at specified location
	 */
	public ToolField fieldAt(int row, int col) {
		return playfield[row][col];
	}
}
