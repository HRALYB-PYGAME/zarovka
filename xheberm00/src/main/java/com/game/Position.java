package com.game;

public class Position{
    public int row;
    public int col;
    public static void main(String[] args){

    }

    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getCol(){
        return this.col;
    }

    public int getRow(){
        return this.row;
    }

    @Override public boolean equals(Object obj){
        if(obj.getClass() == this.getClass()){
            Position objP = (Position) obj;
            if(objP.getRow() == this.row && objP.getCol() == this.col){
                return true;
            }
        }
        return false;
    }
}
