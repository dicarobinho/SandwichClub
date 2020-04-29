package com.udacity.sandwichclub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.udacity.sandwichclub.data.DatabaseOperations;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.EmptyRecyclerView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

public class MainActivity extends AppCompatActivity implements SandwichAdapter.ListItemClickListener, SandwichAdapter.ListItemLongClickListener {

    //    // Extra for the task ID to be received in the intent
//    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    SandwichAdapter mAdapter;
    EmptyRecyclerView appRecyclerView;
    View loadingIndicator;

    public static final int DEFAULT_TASK_ID = -1;
    public int mTaskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        loadingIndicator = findViewById(R.id.loading_spinner);
        appRecyclerView = findViewById(R.id.my_recycler_view);
        TextView mEmptyStateTextView = findViewById(R.id.empty_view);

        appRecyclerView.setEmptyView(mEmptyStateTextView);
        mAdapter = new SandwichAdapter(MainActivity.this, this, this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        appRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        appRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));

        // specify an adapter (see also next example
        appRecyclerView.setAdapter(mAdapter);

        //parsareRandomJsonLocal();
        setupViewModel();
    }

    public void deleteSandwich(final Sandwich sandwich) {
        DatabaseOperations.deleteSandwich(sandwich, this);
    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.SELECTED_SANDWICH_POSITION, position);
        startActivity(intent);
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getSandwiches().observe(this, new Observer<List<Sandwich>>() {
            @Override
            public void onChanged(@Nullable List<Sandwich> taskEntries) {
                mAdapter.setSandwiches(null);
                mAdapter.setSandwiches(taskEntries);
                loadingIndicator.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClickListener(int viewIndex) {
        launchDetailActivity(viewIndex);
    }

    @Override
    public void onItemClickListener(Sandwich sandwich) {
        deleteSandwich(sandwich);
    }

    public void addAppFloatingActionButton(View view) {
        Intent intent = new Intent(this, AddSandwichActivity.class);
        startActivity(intent);
    }
}
