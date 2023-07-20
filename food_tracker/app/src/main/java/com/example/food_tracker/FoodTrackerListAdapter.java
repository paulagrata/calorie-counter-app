package com.example.food_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class FoodTrackerListAdapter
        extends FirebaseRecyclerAdapter<FoodTracker, FoodTrackerListAdapter.TrackerHolder> {
    private List<FoodTracker> mFoodTracker;
    private Context context;

    public FoodTrackerListAdapter(@NonNull FirebaseRecyclerOptions<FoodTracker> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TrackerHolder holder, int position, @NonNull FoodTracker model) {
        holder.mRowFoodNameView.setText(model.getFood());
        int calories = model.getCals();
        String calsString = context.getResources().getQuantityString(R.plurals.calories_string, calories, calories);
        holder.mRowCalorieView.setText(calsString);
        holder.mRowMealTimeView.setText(model.getMealTime());
        holder.mRowMealDayView.setText(model.getDay());
    }

    @NonNull
    @Override
    public TrackerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tracker_row,parent,false);
        return new FoodTrackerListAdapter.TrackerHolder(view);
    }

    class TrackerHolder extends RecyclerView.ViewHolder{
        private final TextView mRowMealDayView;
        private final TextView mRowMealTimeView;
        private final TextView mRowFoodNameView;
        private final TextView mRowCalorieView;


        public TrackerHolder(@NonNull View itemView) {
            super(itemView);
            mRowMealDayView = itemView.findViewById(R.id.row_meal_day_view);
            mRowMealTimeView = itemView.findViewById(R.id.row_meal_time_view);
            mRowFoodNameView = itemView.findViewById(R.id.row_food_name_view);
            mRowCalorieView = itemView.findViewById(R.id.row_calorie_view);

        }

    }

}