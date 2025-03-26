package com.game;

import com.game.Side;
import com.game.Position;

public class GameNode{
    public enum Type {EMPTY, LINK, BULB, POWER};

    public Position pos;
    public Side[] connectors;
    public Type type;

    public GameNode(){

    }

    public static void main(String[] args){

    }

    public boolean containsConnector(Side s){
        //System.out.println("if contains: " + s);
        if(s == null || connectors == null){
            System.out.println("false|null");
            return false;
        }
        int i = 0;
        while(i < connectors.length){
            if(s == connectors[i]){
                System.out.println("true");
                return true;
            }
            i++;
        }
        System.out.println("false");
        return false;
    }

    public Position getPosition(){
        return pos;
    }

    public void turn(){
        int i = 0;
        while(i < connectors.length){
            if(Side.valueOf("EAST") == connectors[i]){
                connectors[i] = Side.valueOf("SOUTH");
            }
            else if(Side.valueOf("SOUTH") == connectors[i]){
                connectors[i] = Side.valueOf("WEST");
            }
            else if(Side.valueOf("WEST") == connectors[i]){
                connectors[i] = Side.valueOf("NORTH");
            }
            else if(Side.valueOf("NORTH") == connectors[i]){
                connectors[i] = Side.valueOf("EAST");
            }
            i++;
        }
    }

    public boolean isLink(){
        //System.out.println("type(L): " + type);
        if(type == null){
            return false;
        }
        return type == Type.LINK;
    }

    public boolean isBulb(){
        System.out.println("type(b): " + type.name());
        if(type == null){
            //System.out.println("bulb null");
            return false;
        }
        if(type == Type.BULB){
            //System.out.println("it is bulb..");
            return true;
        }
        return false;
    }

    public boolean isPower(){
        //System.out.println("type(p): " +  type);
        if(type == null){
            return false;
        }
        return type == Type.POWER;
    }
}
