package com.example.algo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class stressfactor extends AppCompatActivity {
       private EditText weight,height,age,calories;
       private TextView  logmsg1,logmsg2,logmsg3;
       private Button savelog, scorepage;
       private Spinner water,sleep;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stressfactor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        calories=findViewById(R.id.input_calories_had);
        savelog=findViewById(R.id.saveLog);
        scorepage=findViewById(R.id.scorepage);
        water=findViewById(R.id.spinner_water_drank);
        sleep=findViewById(R.id.spinner_sleep);
        logmsg1=findViewById(R.id.msg1);
        logmsg2=findViewById(R.id.msg2);
        logmsg3=findViewById(R.id.msg3);

        /*scorepage.setOnClickListener(v -> {
            Intent intent = new Intent(this, StressScore.class);
            startActivity(intent);

        });*/

        savelog.setOnClickListener(v -> {
            calculateWaterIntake();
            calculateCalories();
           dailyLog();
        });

    }
    //for the daily log
    private void dailyLog() {
        // Get values from Spinners
        Spinner waterSpinner = findViewById(R.id.spinner_water_drank);
        Spinner sleepSpinner = findViewById(R.id.spinner_sleep);
        calories=findViewById(R.id.input_calories_had); // Assuming there's an EditText for calorie input

        TextView logmsg1 = findViewById(R.id.msg1);
        TextView logmsg2 = findViewById(R.id.msg2);
        TextView logmsg3 = findViewById(R.id.msg3);

        String waterStr = waterSpinner.getSelectedItem().toString();
        String sleepStr = sleepSpinner.getSelectedItem().toString();
        String calorieStr = calories.getText().toString();

        if (!waterStr.isEmpty() && !sleepStr.isEmpty() && !calorieStr.isEmpty()) {
            // Convert values to int
            int water = Integer.parseInt(waterStr);
            int sleep = Integer.parseInt(sleepStr);
            int calories = Integer.parseInt(calorieStr);

            //to send the date to the stress score
            Intent intent = new Intent(this, StressScore.class);
            intent.putExtra("water", water);
            intent.putExtra("calories", calories);
            intent.putExtra("sleep", sleep);
            startActivity(intent);

            // Check water intake
            if (water < 4) {
                logmsg1.setText("Water intake is below average");
                logmsg1.setTextColor(Color.RED);
            } else if (water > 6 && water < 10) {
                logmsg1.setText("Keep up the healthy limit");
                logmsg1.setTextColor(Color.GREEN);
            } else if(water <= 6 && water > 4){
                logmsg1.setText("Water intake is average");
                logmsg1.setTextColor(Color.YELLOW);
            } else {
                logmsg1.setText("Water intake is excessive");
                logmsg1.setTextColor(Color.RED);
            }

            // Check calorie intake
            if (calories < 1200) {
                logmsg2.setText("Poor diet, please have proper meals");
                logmsg2.setTextColor(Color.RED);
            } else if (calories >= 1200 && calories <= 2700) {
                logmsg2.setText("The diet is average");
                logmsg2.setTextColor(Color.YELLOW);
            } else if (calories > 2700 && calories < 4000) {
                logmsg2.setText("Keep up the healthy diet");
                logmsg2.setTextColor(Color.GREEN);
            } else {
                logmsg2.setText("Poor diet, please reduce food intake");
                logmsg2.setTextColor(Color.RED);
            }

            // Check sleep hours
            if (sleep < 6) {
                logmsg3.setText("Sleep quality is less, it will affect your health");
                logmsg3.setTextColor(Color.RED);
            } else if (sleep >= 6 && sleep < 9) {
                logmsg3.setText("Average hours of sleep");
                logmsg3.setTextColor(Color.YELLOW);
            } else if (sleep >= 9 && sleep <= 12) {
                logmsg3.setText("Perfect sleep");
                logmsg3.setTextColor(Color.GREEN);
            } else if(sleep > 12){
                logmsg3.setText("you made sleeping beauty seem normal");
                logmsg3.setTextColor(Color.RED);
            }
        } else {
            // If any input field is empty, show a message or handle it as needed
            Toast.makeText(this, "Please enter all values", Toast.LENGTH_SHORT).show();
        }
    }



    //to display the calorie intake
    private void calculateCalories() {
        // Get weight from EditText
        weight=findViewById(R.id.input_weight);
        height=findViewById(R.id.input_height);
        age=findViewById(R.id.input_age);

        String weightStr = weight.getText().toString();
        String heightStr = height.getText().toString();
        String ageStr = age.getText().toString();

        if (!weightStr.isEmpty() && !heightStr.isEmpty() && !ageStr.isEmpty()) {
            // Convert weight, height, and age to double
            double weight2 = Double.parseDouble(weightStr);
            double height1 = Double.parseDouble(heightStr);
            double age1 = Double.parseDouble(ageStr);

            // Calculate calories
            double calories = (10 * weight2) + (6.25 * height1) - (5 * age1) + 5;
            //display the calories
            TextView caloriesTextView = findViewById(R.id.calorie_dis);
            caloriesTextView.setText(String.valueOf(calories) + " calories");

        }
        else {
            // If weight field is empty, show a message or handle it as needed
            Toast.makeText(this, "Please enter your weight", Toast.LENGTH_SHORT).show();
        }


    }
    // to display the amount of water intake
    @SuppressLint("WrongViewCast")
    private void calculateWaterIntake() {
        // Get weight from EditText
        weight=findViewById(R.id.input_weight);

        String weightStr = weight.getText().toString();

        if (!weightStr.isEmpty()) {
            // Convert weight to double
            double weight1 = Double.parseDouble(weightStr);

            // Calculate water intake
            double waterIntake = weight1 * 0.5;

            // Convert water intake to glasses (assuming 8oz per glass)
            int glasses = (int) Math.ceil(waterIntake / 8);

            // Display the water intake in the TextView
            TextView waterIntakeTextView = findViewById(R.id.water_dis);
            waterIntakeTextView.setText(String.valueOf(glasses) + " glasses");
        } else {
            // If weight field is empty, show a message or handle it as needed
            Toast.makeText(this, "Please enter your weight", Toast.LENGTH_SHORT).show();
        }
    }

}