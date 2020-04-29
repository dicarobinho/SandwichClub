package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.data.AppDatabase;
import com.udacity.sandwichclub.model.Sandwich;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DetailActivity extends AppCompatActivity {

    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    public static final String SELECTED_SANDWICH_POSITION = "extra_position";
    public static final int DEFAULT_TASK_ID = -1;
    public int mTaskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppDatabase mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SELECTED_SANDWICH_POSITION)) {
            if (mTaskId == DEFAULT_TASK_ID) {
                mTaskId = intent.getIntExtra(SELECTED_SANDWICH_POSITION, DEFAULT_TASK_ID);
                DetailActivityViewModelFactory factory = new DetailActivityViewModelFactory(mDb, mTaskId);
                final DetailActivityViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);
                viewModel.getSandwich().observe(this, new Observer<Sandwich>() {
                    @Override
                    public void onChanged(@Nullable Sandwich sandwich) {
                        viewModel.getSandwich().removeObserver(this);
                        assert sandwich != null;
                        populateUI(sandwich);
                    }
                });
            }
        }
    }

    private void populateUI(Sandwich sandwich) {
        ImageView image = findViewById(R.id.image_iv);
        TextView description = findViewById(R.id.description_tv);
        TextView ingredients = findViewById(R.id.ingredients_tv);
        TextView alsoKnown = findViewById(R.id.also_known_tv);
        TextView origin = findViewById(R.id.origin_tv);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(sandwich.getMainName());

        description.setText(sandwich.getDescription());

        if (!sandwich.getPlaceOfOrigin().equals(""))
            origin.setText(sandwich.getPlaceOfOrigin());
        else origin.setText("N/A");

        if (!sandwich.getIngredients().equals(""))
            ingredients.setText(sandwich.getIngredients());
        else ingredients.setText(getResources().getText(R.string.not_specified));

        if (!sandwich.getAlsoKnownAs().equals(""))
            alsoKnown.setText(sandwich.getAlsoKnownAs());
        else alsoKnown.setText(getResources().getText(R.string.not_specified));

        if (sandwich.getImage().contains("http"))
            Picasso.get().load(sandwich.getImage()).into(image);
        else
            Picasso.get().load("file://" + sandwich.getImage()).config(Bitmap.Config.RGB_565).into(image);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
