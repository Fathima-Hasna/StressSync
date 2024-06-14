package com.example.algo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import java.util.List;

public class StressScore extends AppCompatActivity {
    private PieChart pieChart;
    private ProgressBar stressScoreBar;
    private Button back;
    private  TextView score, mesg,mesg2,mesg3;
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

        int water = getIntent().getIntExtra("water", 6);
        int calories = getIntent().getIntExtra("calories", 2500);
        int sleep = getIntent().getIntExtra("sleep", 8);

        setupPieChart();
        updatePieChart(water, calories, sleep);
        stressscoreprogress(water, calories, sleep);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, stressfactor.class);
            startActivity(intent);
        });
    }


    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
    }

    private void updatePieChart(int water, int calories, int sleep) {


        final int targetWater = 8; // cups
        final int targetCalories = 2500; // calories
        final int targetSleep = 8; // hours
        float normalizedWater = (float) water / targetWater * 100;
        float normalizedCalories = (float) calories / targetCalories * 100;
        float normalizedSleep = (float) sleep / targetSleep * 100;
        float total = water + calories + sleep;
        float finalwater = (normalizedWater/total)*100;
        float finalcalories = (normalizedCalories/total)*100;
        float finalsleep = (normalizedSleep/total)*100;
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(finalwater, "Water Intake"));
        entries.add(new PieEntry(finalcalories, "Calories Intake"));
        entries.add(new PieEntry(finalsleep, "Sleep Hours"));

        PieDataSet dataSet = new PieDataSet(entries, "Daily log");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.BLACK);

        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
    }

    private void stressscoreprogress(int water, int calories, int sleep){
        int stressScore = 0;
        score = findViewById(R.id.displayscore);
        mesg = findViewById(R.id.mesg);
        mesg2 = findViewById(R.id.mesg2);
        mesg3 = findViewById(R.id.mesg3);

        if (water > 4 && water < 13) {
            stressScore += 30;
            score.setText("Stress Score =" +" "+ String.valueOf(stressScore));
            mesg.setText("Keep up the healthy limit");
        } else {
            stressScore += 10;
            score.setText("Stress Score =" +" "+ String.valueOf(stressScore));
            mesg.setText("Water intake is below average");
        }

        if (calories > 1200 && calories < 4000) {
            stressScore += 30;
            score.setText("Stress Score =" +" "+ String.valueOf(stressScore));
            mesg2.setText("Keep up the healthy diet");
        } else {
            stressScore += 10;
            score.setText("Stress Score =" +" "+ String.valueOf(stressScore));
            mesg2.setText("please have proper meals");
        }


        if (sleep > 6 && sleep < 10) {
            stressScore += 40;
            score.setText("Stress Score =" +" "+ String.valueOf(stressScore));
            mesg3.setText("Perfect sleep");
        } else {
            stressScore += 10;
            score.setText("Stress Score =" +" "+ String.valueOf(stressScore));
            mesg3.setText("Sleep quality is less, it will affect your health");
        }


        stressScoreBar.setMax(100); // Ensure the ProgressBar's maximum is set to 100
        stressScoreBar.setProgress(stressScore);


        if (stressScore <= 33) {
            stressScoreBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

        } else if (stressScore <= 66) {
            stressScoreBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));

        } else {
            stressScoreBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

        }
    }
}

