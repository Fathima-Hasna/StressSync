package com.example.algo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;

import java.util.List;

public class StressScore extends AppCompatActivity {
    private PieChart pieChart;
    private ProgressBar stressScoreBar;
    private Button back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stress_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        pieChart = findViewById(R.id.pieChart);
        stressScoreBar = findViewById(R.id.stressScoreBar);

        int water = getIntent().getIntExtra("water", 0);
        int calories = getIntent().getIntExtra("calories", 0);
        int sleep = getIntent().getIntExtra("sleep", 0);

        updatepiechart(water, calories, sleep);
        stressscoreprogress(water, calories, sleep);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, stressfactor.class);
            startActivity(intent);
        });
    }


    private void updatepiechart(int water, int calories, int sleep){
       List<PieEntry> entries = new ArrayList<>(); // Create a list to hold pie chart entries

        // Add a pie chart entry for water , calore intake, and sleep hr  with the labels
       entries.add(new PieEntry(water, "Water Intake"));
       entries.add(new PieEntry(calories, "Calories Intake"));
       entries.add(new PieEntry(sleep, "Sleep Hours"));

       // Create a PieDataSet object with the entries and a label "Daily log"
       PieDataSet dataSet= new PieDataSet(entries, "Daily log");

       // Set the colors for each slice of the pie chart
       dataSet.setColors(new int[]{Color.parseColor("#1DA8D8"), Color.parseColor("#D39DF7"), Color.parseColor("#990099")});

        // Create a PieData object with the data set
       PieData piedata = new PieData(dataSet);

        // Set the PieData object to the pie chart
        pieChart.setData(piedata);

        // Refresh the pie chart with the new data
       pieChart.invalidate();
    }

    private void stressscoreprogress(int water, int calories, int sleep){
        int stressScore = 0; // Initialize the stress score to 0

        // Check if the values are within the specified range and adjust the stress score accordingly
        if (water < 4 || water > 13) {
            stressScore += 30;
        }
        if (calories < 1200 || calories > 4000) {
            stressScore += 30;
        }
        if (sleep < 6 || sleep > 12) {
            stressScore += 40;
        }
        // Set the progress bar's progress to the calculated stress score
        stressScoreBar.setProgress(stressScore);
    }
}

