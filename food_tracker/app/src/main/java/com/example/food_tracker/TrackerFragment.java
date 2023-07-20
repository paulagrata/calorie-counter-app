package com.example.food_tracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class TrackerFragment extends Fragment {
    private static final String ARG_TRACKER_id = "trackerId";
    private FoodTracker mFoodTracker;
    private EditText mFoodEditText;
    private TextView mTextView;
    private EditText mCalEditText;
    private Spinner mDaySpinner;
    private Spinner mMealTimeSpinner;
    private NumberPicker mNumberPicker;
    private Button mSubmitButton;
    private SharedPreferences mPreferences;
    private boolean mAdding = false;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    public TrackerFragment() {
        // Required empty public constructor
    }

    public static TrackerFragment newInstance(int trackerId) {
        TrackerFragment fragment = new TrackerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRACKER_id, trackerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("food_tracker");
        int caloriesId = 0;
        if (getArguments() != null) {
            caloriesId = getArguments().getInt(ARG_TRACKER_id);
        }
        if (caloriesId != 0) {
        } else {
            mAdding = true;
            mFoodTracker = new FoodTracker();
            mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String caloriesString = mPreferences.getString(getString(R.string.pref_calories_key),"0");
            int default_calories = Integer.parseInt(caloriesString);
            mFoodTracker.setCals(default_calories);
            String defaultFood = mPreferences.getString("food","");
            mFoodTracker.setFood(defaultFood);
            String defaultMealTime = mPreferences.getString("mealTime","");
            mFoodTracker.setFood(defaultMealTime);
            String defaultMealDay = mPreferences.getString("day","");
            mFoodTracker.setFood(defaultMealDay);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);
        // food text
        mFoodEditText = view.findViewById(R.id.foodEditText);
        mFoodEditText.addTextChangedListener(new NameTextListener(mFoodEditText));
        //creates textview for calorie display
        mTextView = view.findViewById(R.id.textView);
        //calorie picker
        mNumberPicker = view.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(2000);
        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldValue, int newValue) {
                mTextView.setText(String.format("Calories Entered: %s",newValue));
                mFoodTracker.setCals((int)newValue);
            }
        });
        //day spinner
        mDaySpinner = view.findViewById(R.id.daySpinner);
        mDaySpinner.setOnItemSelectedListener(new DaySelectedListener());
        List<String> day = Arrays.asList(getResources().getStringArray(R.array.day));
        int indexDay = day.indexOf(mFoodTracker.getDay());
        mDaySpinner.setSelection(indexDay);
        //meal spinner
        mMealTimeSpinner = view.findViewById(R.id.mealTimeSpinner);
        mMealTimeSpinner.setOnItemSelectedListener(new MealTimeSelectedListener());
        mFoodEditText.setText(mFoodTracker.getFood());
        List<String> mealTime = Arrays.asList(getResources().getStringArray(R.array.mealTime));
        int indexMealTime = mealTime.indexOf(mFoodTracker.getMealTime());
        mMealTimeSpinner.setSelection(indexMealTime);
        //submit button
        mSubmitButton = view.findViewById(R.id.submitButton);
        mSubmitButton.setOnClickListener(new SubmitButtonListener());
        return view;
    }

    private class SubmitButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(mAdding){
                mDatabaseReference.push().setValue(mFoodTracker);
            } else {
            }
        }
    }

    private class MealTimeSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String mealTime = (String)adapterView.getItemAtPosition(i);
            if (i!=0){
                mFoodTracker.setMealTime(mealTime);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private class DaySelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String day = (String)adapterView.getItemAtPosition(i);
            if (i!=0){
                mFoodTracker.setDay(day);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }


    private class NameTextListener implements TextWatcher {
        private View editText;

        public NameTextListener(View editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (editText == mFoodEditText) {
                mFoodTracker.setFood(charSequence.toString());
            }else if (editText == mCalEditText){
                mFoodTracker.setCals(Integer.parseInt(charSequence.toString()));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}