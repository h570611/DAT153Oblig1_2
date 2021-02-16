package com.jonas.dat153v2.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.jonas.dat153v2.R;
import com.jonas.dat153v2.database.GameObject;
import com.jonas.dat153v2.utils.GameObjectViewModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddImage extends AppCompatActivity {

    public static final String EXTRA_IMAGE_BYTES =
            "com.jonas.dat153v2.activities.EXTRA_IMAGE_NAME";
    public static final String EXTRA_IMAGE_NAME =
            "com.jonas.dat153v2.activities.EXTRA_IMAGE_NAME";

    EditText imageName;
    Button addImageButton;
    ImageView addedImage;
    Bitmap imageBitmap;

    GameObjectViewModel viewModel;

    List<GameObject> allGameObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        //Init all variables
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(GameObjectViewModel.class);
        viewModel.getAllGameObjects().observe(this, gameObjects -> {
            allGameObjects = new ArrayList<>(gameObjects);
        });
        imageName = findViewById(R.id.editTextImageName);
        addImageButton = findViewById(R.id.addImageButton);
        addedImage = findViewById(R.id.addedImageView);

        //Set goBackButton
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //On addImageButton.click() go to camera
        addImageButton.setOnClickListener(v -> {

            // Show camera and take a picture
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                this.startActivityForResult(takePictureIntent, 2);

            }
        });
    }

    //When back from gallery, handle image.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2) {
            Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
            imageBitmap = scaledBM(selectedImage);
            addedImage.setImageBitmap(imageBitmap);
        }
    }


    //Check if any field is empty. If not, save GameObject to DB.
    private void saveGameObject(){
        String name = imageName.getText().toString();
        Bitmap image = imageBitmap;

        if (name.trim().isEmpty() || image == null){
            Toast.makeText(this, "Please insert a name and image", Toast.LENGTH_SHORT).show();
            return;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        GameObject newGameObject = new GameObject(name, byteArray);

        viewModel.insert(newGameObject);

        setResult(RESULT_OK);
        finish();
    }

    //Set SaveButton on actionMenu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_gameobject_menu, menu);
        return true;
    }

    //Set onclick on SaveButton
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_gameObject:
                saveGameObject();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Bitmap scaledBM(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int maxWidth = 500;

        int maxHeight = 500;
        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }
        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }


}