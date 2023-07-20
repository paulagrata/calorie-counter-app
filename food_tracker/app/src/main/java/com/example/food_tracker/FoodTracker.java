package com.example.food_tracker;

public class FoodTracker {
    private String day;
    private String mealTime;
    private String food;
    private int calories;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getCals() {
        return calories;
    }

    public void setCals(int calories) {
        this.calories = calories;
    }



    public FoodTracker() {
        day = "";
        mealTime = "";
        calories = 0;
        food = "";
    }

    public FoodTracker(String day, String mealTime, String food, int calories) {
        this.day = day;
        this.mealTime = mealTime;
        this.food = food;
        this.calories = calories;

    }

    @Override
    public String toString() {
        return "food=" + food + '\n' +
                "calories='" + calories + '\n' +
                "meal time=" + mealTime + '\n' +
                "day=" +day;
    }

}
