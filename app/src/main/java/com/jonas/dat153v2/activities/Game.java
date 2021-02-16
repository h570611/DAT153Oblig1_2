package com.jonas.dat153v2.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jonas.dat153v2.R;
import com.jonas.dat153v2.database.GameObject;
import com.jonas.dat153v2.utils.GameObjectViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends AppCompatActivity {

    ImageView gameImage;
    EditText gameNameGuess;
    Button tryButton, resetButton;
    TextView scoreText, finalScoreText;
    int score;
    int max;
    int min;
    int position;
    int maxScore;
    Random random;
    GameObjectViewModel viewModel;

    GameObject currentGameObject;

    byte[] imageAsByte;
    Bitmap bmp;

    List<GameObject> allImages;
    List<GameObject> defaultGameObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Init variables
        gameImage = findViewById(R.id.gameImage);
        gameNameGuess = findViewById(R.id.guessEditText);
        tryButton = findViewById(R.id.guessButton);
        scoreText = findViewById(R.id.scoreTextView);
        finalScoreText = findViewById(R.id.finalScoreText);
        resetButton = findViewById(R.id.tryAgainBtn);

        //Retrieve viewmodel
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(GameObjectViewModel.class);

        //Run game inside observer to use all gameobjects.
        viewModel.getAllGameObjects().observe(this, gameObjects -> {

            //If no Gameobjects -> Hide content and show error-dialog.
            if (gameObjects.size() < 1){
                noGameObjects();
                AlertDialog alertDialog = new AlertDialog.Builder(Game.this).create();
                alertDialog.setTitle("OBS!");
                alertDialog.setMessage("Du må legge til eit bilde for å spille. Gå til databasen for å gjøre dette.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> finish());
                alertDialog.show();
                return;
            }

            defaultGameObjects = new ArrayList<>(gameObjects);
            allImages = new ArrayList<>(defaultGameObjects);
            random = new Random();
            min = 0;
            max = allImages.size() - 1;
            maxScore = allImages.size();
            score = 0;

            position = random.nextInt(max - min + 1) + min;
            imageAsByte = allImages.get(position).getImage();

            currentGameObject = allImages.get(position);

            bmp = BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);

            gameImage.setImageBitmap(bmp);

            //Set onclick on tryButton. Checks if answer is correct and sets new image.
            tryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Check if answer is correct
                    if (gameNameGuess.getText().toString().equals(allImages.get(position).getName())) {
                        correct();
                    } else {
                        incorrect();
                    }
                    gameNameGuess.setText("");

                    if (allImages.size() > 1) { //Game is still going. Change to new random image.
                        allImages.remove(position);
                        max = allImages.size() - 1;

                        position = random.nextInt(max - min + 1) + min;
                        imageAsByte = allImages.get(position).getImage();
                        currentGameObject = allImages.get(position);

                        bmp = BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);

                        gameImage.setImageBitmap(bmp);
                    } else { //Game is finished. Show only final-score and reset-button.
                        finalScoreText.setText("Din score blei: " + score + " av " + maxScore);
                        finalScoreText.setVisibility(View.VISIBLE);
                        resetButton.setVisibility(View.VISIBLE);
                        scoreText.setVisibility(View.INVISIBLE);
                        tryButton.setVisibility(View.INVISIBLE);
                        gameNameGuess.setVisibility(View.INVISIBLE);
                        gameImage.setVisibility(View.INVISIBLE);
                    }
                }
            });

            //Set onclick on resetbutton
            resetButton.setOnClickListener(v -> {
                resetGame();
            });
        });
    }

    public void correct(){
        Toast.makeText(Game.this, "Korrekt!!", Toast.LENGTH_SHORT).show();
        score++;
        scoreText.setText("Din score er: " + score);
    }
    public void incorrect(){
        Toast.makeText(Game.this, "Feil.. Korrekt var: " + allImages.get(position).getName(), Toast.LENGTH_SHORT).show();
    }

    //Resets game
    public void resetGame() {
        allImages = new ArrayList<>(defaultGameObjects);
        max = allImages.size() - 1;
        position = random.nextInt(max - min + 1) + min;
        imageAsByte = allImages.get(position).getImage();
        bmp = BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);
        gameImage.setImageBitmap(bmp);
        score = 0;
        scoreText.setText("Din score er: " + score);
        finalScoreText.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
        scoreText.setVisibility(View.VISIBLE);
        tryButton.setVisibility(View.VISIBLE);
        gameNameGuess.setVisibility(View.VISIBLE);
        gameNameGuess.setText("");
        gameImage.setVisibility(View.VISIBLE);
    }

    public void noGameObjects(){
        scoreText.setVisibility(View.INVISIBLE);
        tryButton.setVisibility(View.INVISIBLE);
        gameNameGuess.setVisibility(View.INVISIBLE);
        gameImage.setVisibility(View.INVISIBLE);
    }

    public int getScore(){
        return score;
    }

    public GameObject getCurrentGameObject(){
        return currentGameObject;
    }
}