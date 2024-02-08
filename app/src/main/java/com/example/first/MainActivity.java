package com.example.first;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.first.Logic.GameManager;
import com.google.android.material.imageview.ShapeableImageView;


public class MainActivity extends AppCompatActivity {


    private ImageView main_img_background;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_img_background = findViewById(R.id.main_background);
        Glide
                .with(this)
                .load(R.drawable.space_background)
                .centerCrop()
                .into(main_img_background);

        GameManager gameManger = new GameManager(
                findViewById(R.id.gameGrid),
                findViewById(R.id.lives_layout),
                this

        );


        gameManger.initializeGame();

        // Add onClickListeners for the buttons
        findViewById(R.id.btnLeft).setOnClickListener(view -> gameManger.movePlayerLeft());
        findViewById(R.id.btnRight).setOnClickListener(view -> gameManger.movePlayerRight());

    }




}




