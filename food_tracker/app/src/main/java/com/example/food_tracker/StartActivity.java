package com.example.food_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Collections;

public class StartActivity extends AppCompatActivity{
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private Query mQuery;
    private FirebaseRecyclerAdapter<FoodTracker, FoodTrackerListAdapter.TrackerHolder> mFirebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        RecyclerView recyclerView = findViewById(R.id.tracker_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // !!!! changes order of database   .orderByChild("day");
        mQuery = mFirebaseDatabase.getReference().child("food_tracker");

        FirebaseRecyclerOptions<FoodTracker> options = new FirebaseRecyclerOptions.Builder<FoodTracker>()
                .setQuery(mQuery, FoodTracker.class)
                .build();
        mFirebaseRecyclerAdapter= new FoodTrackerListAdapter(options);
        recyclerView.setAdapter(mFirebaseRecyclerAdapter);
        mFirebaseRecyclerAdapter.startListening();
                ActivityResultLauncher < Intent > launcher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                Intent data = result.getData();
                                int calories = data.getIntExtra("cals", 0);
                                String food = data.getStringExtra("food");
                                String toastString = getResources().getString(R.string.entry_entered);
                                String displayFood = getResources().getString(R.string.display_food, food);
                                toastString += displayFood;
                                Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();
                                String calsString = getResources().getQuantityString(R.plurals.calories_string, calories, calories);
                                toastString += calsString;
                                String day = data.getStringExtra("day");
                                String displayDay = getResources().getString(R.string.display_day, day);
                            }
                        });
        FloatingActionButton floatingActionButton = findViewById(R.id.floating_add_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trackerIntent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(trackerIntent);
            }
        });
        ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                (result) -> {
                    if(result.getResultCode() == RESULT_CANCELED){
                        finish();
                    }
                }
        );
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setTheme(R.style.Theme_Food_Tracker)
                            .setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()))
                            .build();
                    signInLauncher.launch(signInIntent);
                }
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
        }
        if (id==R.id.sign_out){
            AuthUI.getInstance().signOut(this);
        }
        return super.onOptionsItemSelected(item);
    }
}