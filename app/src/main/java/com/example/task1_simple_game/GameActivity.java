package com.example.task1_simple_game;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private Timer timer;
    private Toast toast;
    private final int LIVES = 3;
    private GManager gameManager;
    private final int DELAY = 320;
    private enum Side {LEFT, RIGHT;}
    private final int ROW = 5, COL = 3;
    private MaterialButton game_BTN_left;
    private MaterialButton game_BTN_right;
    private ShapeableImageView[] elonDisplayPath;
    private ShapeableImageView[] livesDisplayBoard;
    private ShapeableImageView playBoard_background;
    private LottieAnimationView[] smokeDisplayBoard;
    private LottieAnimationView[][] gameDisplayBoard;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        hideSystemUI(this);
        timer = new Timer();
        findViewBy();
        Glide
                .with(GameActivity.this)
                .load(R.drawable.img_game_background)
                .into(playBoard_background);

        /*1*/setBoardGame();
        /*2*/Click();
        gameManager = new GManager();
        startTimer();

    }
    private void findViewBy(){
        playBoard_background = findViewById(R.id.game_img_gameBoard_background);
    }


    private void startTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameManager.UpdateMeteorLogicPos();
                        if(gameManager.hitDetection()) {
                            vibrate();
                            getToastMSG();
                        }
                        refreshUI();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, DELAY);
    }



    private void Click() {
        game_BTN_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked(Side.RIGHT);
            }
        });

        game_BTN_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked(Side.LEFT);
            }
        });
    }


    private void clicked(Side size) {
        if (size.equals(Side.LEFT))
            gameManager.left();

        if (size.equals(Side.RIGHT))
            gameManager.right();

        refreshUI();
    }


    private void refreshUI() {
        //player life
        for (int i = 0; i < 3; i++) {
            if (gameManager.getLivesLogicArr()[i] != 1) {
                livesDisplayBoard[i].setVisibility(View.INVISIBLE);
            }
        }
        //player moves
        for (int i = 0; i < COL; i++) {
            if (gameManager.getElonPath()[i] == 1) {
                elonDisplayPath[i].setVisibility(View.VISIBLE);
            }
            if (gameManager.getElonPath()[i] == 0) {
                elonDisplayPath[i].setVisibility(View.INVISIBLE);
            }
        }

        //meteor move
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW - 1; j++) {
                if (gameManager.getMeteorsLogicPosition()[j][i] == 1) {
                    gameDisplayBoard[j][i].setVisibility(View.VISIBLE);
                } else {
                    gameDisplayBoard[j][i].setVisibility(View.INVISIBLE);
                }
            }
        }

        //Explosion on the ground
        for (int i = 0; i < COL; i++) {
            if (gameManager.getLastRow()[i] == 1) {
                smokeDisplayBoard[i].setVisibility(View.VISIBLE);
            } else {
                smokeDisplayBoard[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void getToastMSG(){
        toast.makeText(this, "ouch!", Toast.LENGTH_SHORT).show();
    }


    public void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(1000);
        }
    }


    private void setBoardGame() {
        game_BTN_left = findViewById(R.id.game_BTB_left);
        game_BTN_right = findViewById(R.id.game_BTB_right);

        String str = "meteor";
        gameDisplayBoard = new LottieAnimationView[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                gameDisplayBoard[i][j] = findViewById(getResources().getIdentifier("game_img_" + str + i + "" + j, "id", getPackageName()));
                gameDisplayBoard[i][j].setVisibility(View.INVISIBLE);
            }
        }//

        String str2 = "elon4";
        elonDisplayPath = new ShapeableImageView[COL];
        for (int i = 0; i < COL; i++) {
            elonDisplayPath[i] = findViewById(getResources().getIdentifier("game_img_" + str2 + i + "", "id", getPackageName()));
            if (i != 1) {
                elonDisplayPath[i].setVisibility(View.INVISIBLE);
            }
        }


        String str3 = "smoke4";
        smokeDisplayBoard = new LottieAnimationView[COL];
        for (int i = 0; i < COL; i++) {
            smokeDisplayBoard[i] = findViewById(getResources().getIdentifier("game_img_" + str3 + i + "", "id", getPackageName()));
            if (i != 1) {
                smokeDisplayBoard[i].setVisibility(View.INVISIBLE);
            }
        }


        String str4 = "rocket_heart";
        livesDisplayBoard = new ShapeableImageView[LIVES];
        for (int j = 0; j < livesDisplayBoard.length; j++) {
            livesDisplayBoard[j] = findViewById(getResources().getIdentifier("game_img_" + str4 + (j + 1), "id", getPackageName()));
            livesDisplayBoard[j].setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI(this);
        }
    }


    public static void hideSystemUI(Activity activity) {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        // Dim the Status and Navigation Bars
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);

        // Without - cut out display
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }

}





