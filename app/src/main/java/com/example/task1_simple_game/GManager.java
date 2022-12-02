package com.example.task1_simple_game;
import android.app.Activity;
import java.util.Arrays;
import java.util.Random;

public class GManager extends Activity {

    private Random random;
    private int[] elonPath;
    boolean zeroRow = false;
    private int livesLeft = 3;
    private final int ROW = 4;
    private final int COL = 3;
    private int[] livesLogicArr;
    private int[][] meteorsLogicPosition;

    public GManager() {
        livesLogicArr = new int[]{1, 1, 1};
        elonPath = new int[]{0, 1, 0};
        meteorsLogicPosition = new int[ROW][COL];
    }


    private void updateLive(int livesLeft){
        int[] array = nullifyPath(livesLogicArr);
        for (int i = 0; i < livesLeft; i++) {
            array[i] = 1;
        }
        livesLogicArr = array;
    }


    private int lastRowIndicate(int i){
        int[] temp = getLastRow();
        if(temp[i] == 1)
            return i;

        return -1;
    }


    public int[] getLastRow(){
        return meteorsLogicPosition[ROW].clone();
    }


    public boolean hitDetection() {
        for (int i = 0; i < COL; i++){
            if(lastRowIndicate(i) == getElonPos()){
                if(--livesLeft >= 0){
                    updateLive(livesLeft);
                    return true;
                }
            }
        }
        return false;
    }


    public void UpdateMeteorLogicPos() {
        int[][] tempMat = new int[5][3];
        for (int i = 0; i < ROW; i++) {
            int nextRow = i + 1;
            tempMat[nextRow] = meteorsLogicPosition[i].clone();
        }
        meteorsLogicPosition = tempMat;
        initNewRow();
    }


    private void initNewRow(){
        int[] row;
        if(!zeroRow) {
            row = randomPos(meteorsLogicPosition[0].clone());
        }else{
            row = nullifyPath(meteorsLogicPosition[0]);
        }
        zeroRow = !zeroRow;
        meteorsLogicPosition[0] = row;
    }


    private int[] randomPos(int[] row){
        int rand = getRandomNumber();
        row[rand] = 1;
        return row;
    }


    private int[] nullifyPath(int[] row){
        Arrays.fill(row, 0);  //reset array
        return row;
    }


    private int getRandomNumber(){
        random = new Random();
        return random.nextInt((3 + 1) - 1);
    }


    private int getElonPos() {
        for (int i = 0; i < elonPath.length; i++) {
            if (elonPath[i] == 1)
                return i;
        }
        return -1;
    }


    public boolean right() {
        int temp = getElonPos();
        if (getElonPos() >= 0 && getElonPos() < elonPath.length) {
            nullifyPath(elonPath);
            elonPath[temp + 1] = 1;
        }
        return true;
    }


    public boolean left() {
        int temp = getElonPos();
        if (getElonPos() > 0 && getElonPos() <= elonPath.length) {
            nullifyPath(elonPath);

            elonPath[temp - 1] = 1;
        }
        return true;
    }


    public int[][] getMeteorsLogicPosition() {
        return meteorsLogicPosition;
    }

    public int[] getElonPath() {
        return elonPath;
    }

    public int[] getLivesLogicArr() {
        return livesLogicArr;
    }
}


