package com.udacity.sandwichclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.udacity.sandwichclub.data.DatabaseOperations;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.BitmapUtils;
import com.udacity.sandwichclub.utils.FieldsRestrictions;

import java.io.File;
import java.io.IOException;

public class AddSandwichActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String FILE_PROVIDER_AUTHORITY = "com.udacity.sandwichclub.fileprovider";

    ImageView image_iv;
    EditText name;
    EditText description;
    EditText origin;
    EditText ingredients;
    EditText alsoKnown;
    Button saveButton;

    private String mTempPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sandwich);

        image_iv = findViewById(R.id.image_iv);
        name = findViewById(R.id.edit_text_name);
        description = findViewById(R.id.edit_text_description);
        origin = findViewById(R.id.edit_text_origin);
        ingredients = findViewById(R.id.edit_text_ingredients);
        alsoKnown = findViewById(R.id.edit_text_also_known);
        saveButton = findViewById(R.id.save_button);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveSandwich(View view) {

        if (FieldsRestrictions.editTextIsEmpty(name))
            Toast.makeText(this, R.string.name_mandatory, Toast.LENGTH_LONG).show();
        else if (FieldsRestrictions.editTextIsEmpty(description))
            Toast.makeText(this, R.string.description_mandatory, Toast.LENGTH_LONG).show();
        else if (BitmapUtils.resamplePic(this, mTempPhotoPath) == null)
            Toast.makeText(this, R.string.image_mandatory, Toast.LENGTH_LONG).show();
        else {
            saveButton.setEnabled(false);
            Sandwich sandwich = new Sandwich(name.getText().toString(),
                    alsoKnown.getText().toString(),
                    origin.getText().toString(),
                    description.getText().toString(),
                    BitmapUtils.saveImage(getApplicationContext(), BitmapUtils.resamplePic(this, mTempPhotoPath)),
                    //"https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Grilled_ham_and_cheese_014.JPG/800px-Grilled_ham_and_cheese_014.JPG",
                    ingredients.getText().toString());
            DatabaseOperations.saveSandwichToDataBase(sandwich, this);
            Toast.makeText(this, R.string.sandwich_added, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {
            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        }
    }

    /**
     * Method for processing the captured image and setting it to the TextView.
     */
    private void processAndSetImage() {

        // Resample the saved image to fit the ImageView
        Bitmap mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);

        // Set the new bitmap to the ImageView
        image_iv.setImageBitmap(mResultsBitmap);
    }

    public void addNewPhoto(View view) {
        launchCamera();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_add_sandwich, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (id == R.id.addRandomSandwich) {
            DatabaseOperations.parsareRandomJsonLocal(this);
            onBackPressed();
            return true;
        } else if (id == R.id.clearFieldsContent) {
            image_iv.setImageResource(R.drawable.add_photo);
            name.setText("");
            name.setHint(getString(R.string.name_hint));
            name.setHintTextColor(alsoKnown.getHintTextColors());

            description.setText("");
            description.setHint(getString(R.string.description_hint));
            description.setHintTextColor(alsoKnown.getHintTextColors());

            alsoKnown.setText("");
            origin.setText("");
            ingredients.setText("");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
