package com.example.task1_simple_game;


public class Elon_Musk {

    private int currentPos;
    private int previousPos;

    public Elon_Musk(int initPos){
        this.currentPos = initPos;
        this.previousPos = initPos;
    }


    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int side) {
        setPreviousPos(currentPos);
        this.currentPos += side;
    }

    public int getPreviousPos() {
        return previousPos;
    }

    public void setPreviousPos(int previousPos) {
        this.previousPos = previousPos;
    }
}



