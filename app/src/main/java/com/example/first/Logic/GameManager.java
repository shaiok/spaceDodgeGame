package com.example.first.Logic;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.LinearLayoutCompat;


import com.example.first.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameManager {

    private final int NUM_ROWS= 6 ;
    private final int NUM_COLS= 3 ;
    private final Context context;

    private int playerColumn; // Initial player position in the middle column
    private List<Astroid> obstacleList ;
    private int lives; // Number of lives remaining

    private final GridLayout gameGrid;
    private final LinearLayoutCompat livesLayout;

    private int asteroidCreationCounter = 0;
    private Handler handler;



    public GameManager(GridLayout grid, LinearLayoutCompat livesView, Context context) {

        gameGrid = grid;
        livesLayout = livesView;
        this.context = context; // Add this field
    }

    public void initializeGame() {
        // Initialize player and obstacle positions
        playerColumn = 1;
        obstacleList = new ArrayList<>();
        this.lives = 3; // Set initial number of lives

        // Initialize handler for updating the game at regular intervals
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // Handle messages to update the game state
                updateGame();
                handler.sendEmptyMessageDelayed(0, 1000); // Update every 1000 milliseconds (adjust as needed)
            }
        };

        // Start the game loop
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private void updateGame() {
        // moving existing Obstacles values
        updateObstacles();
        //adding new Obstacle
        if (asteroidCreationCounter %2 ==0) addAstroid();
        // updating the Obstacles images
        updateGridImages();

        checkCollision();
        asteroidCreationCounter++;

    }

    private void addAstroid() {
        // Generate a random column for the asteroid
        int randomColumn = new Random().nextInt(NUM_COLS);

        // Create an asteroid object (assuming Astroid is your class)
        Astroid asteroid = new Astroid(0, randomColumn); // Assuming 0 is the starting row

        // Add the asteroid to the list
        obstacleList.add(asteroid);

    }



    public void clearGrid() {
        int startIndex = 0; // Start index of the range
        int endIndex = Math.min(18, gameGrid.getChildCount()); // End index of the range

        for (int i = startIndex; i < endIndex; i++) {
            ImageView cellImageView = (ImageView) gameGrid.getChildAt(i);
            cellImageView.setImageResource(0); // Clear the image
        }
    }
    private void updateGridImages() {
        clearGrid();
        for (Astroid astroid : obstacleList) {
            int row = astroid.getRow();
            int col = astroid.getCol();
            int cellId = context.getResources().getIdentifier("cell" + ( row* NUM_COLS + col + 1), "id", context.getPackageName());
            ImageView astroidImageView = ((Activity) context).findViewById(cellId);
            astroidImageView.setImageResource(R.drawable.astroid);
        }
    }


    private void updatePlayerImage() {
        // Clear all player images in the grid before updating
        clearPlayerImages();
            int playerId = context.getResources().getIdentifier("player" + (playerColumn + 1), "id", context.getPackageName());
            ImageView cellImageView = ((Activity) context).findViewById(playerId);
            cellImageView.setVisibility(View.VISIBLE);
    }

    private void clearPlayerImages() {
        for (int c = 0; c < NUM_COLS; c++) {
            int playerId = context.getResources().getIdentifier("player" + (c + 1), "id", context.getPackageName());
            ImageView cellImageView = ((Activity) context).findViewById(playerId);
            cellImageView.setVisibility(View.INVISIBLE);

        }
    }
    private void checkCollision() {
        // Check for collision with player
        for (Astroid astroid : obstacleList) {
            // update lives, vibrate , toast message
                if (astroid.getRow() == NUM_ROWS - 1 && astroid.getCol() == playerColumn)  collision();
        }
    }

    private void collision() {
        // Collision detected, reduce lives
        lives--;
        // Update UI to reflect the reduced lives
        updateLivesUI();

        // Vibrate for 500 milliseconds
        vibrate();

        // Show a "BOOM !!!" toast message
        showToast("BOOM !!! Lives remaining: " + lives);
    }
    private void updateLivesUI() {
        // Assuming livesLayout is your LinearLayout containing heart1, heart2, and heart3
        switch (lives) {
            case 0:
                // Make the third heart (index 2) invisible
                livesLayout.getChildAt(0).setVisibility(View.INVISIBLE);
                break;
            case 1:
                // Make the second heart (index 1) invisible
                livesLayout.getChildAt(1).setVisibility(View.INVISIBLE);
                break;
            case 2:
                // Make the first heart (index 0) invisible
                livesLayout.getChildAt(2).setVisibility(View.INVISIBLE);
                break;
            // You can add more cases if needed
        }
    }

    private void vibrate( ) {

        // vibrate phone in case of crash
            Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.EFFECT_HEAVY_CLICK));
            } else {
                v.vibrate(200);
            }
        }




    private void showToast(String message) {
        Toast.makeText(context,
                message,
                Toast.LENGTH_SHORT).show();
    }
    // Move existing obstacles down
    private void updateObstacles() {
        Iterator<Astroid> iterator = obstacleList.iterator();
        while (iterator.hasNext()) {
            Astroid asteroid = iterator.next();

            // Check if the asteroid is in the last row
            if (asteroid.getRow() == NUM_ROWS - 1) {
                // Remove the asteroid
                iterator.remove();
            } else {
                int row = asteroid.getRow();
                asteroid.setRow(row + 1);
            }
        }
    }


    public void movePlayerRight() {
        if (this.playerColumn < NUM_COLS - 1) {
            this.playerColumn += 1;


            checkCollision(); // Check for collision after moving
            updatePlayerImage();
        }
    }
    public void movePlayerLeft() {
        if (this.playerColumn > 0) {
            this.playerColumn -= 1;

            checkCollision(); // Check for collision after moving
            updatePlayerImage();

        }
    }

}
