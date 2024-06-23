package com.example.algo;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class games extends AppCompatActivity {
   private FrameLayout bubble_pop, color_switch, pictionary, quick_draw, music_tap, tower_of_hanoi;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_games);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bubble_pop=findViewById(R.id.bubble_pop_game);
        color_switch=findViewById(R.id.color_switch_game);
        pictionary=findViewById(R.id.pictionary_game);
        quick_draw=findViewById(R.id.quick_draw_game);
        music_tap=findViewById(R.id.music_tap_game);
        tower_of_hanoi=findViewById(R.id.tower_of_hanoi_game);

        bubble_pop.setOnClickListener(View -> {
            String url = "https://play.google.com/store/apps/details?id=com.bitmango.go.bubblepop&hl=en&pli=1";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No browser found to open the link.", Toast.LENGTH_SHORT).show();
            }

        });
        color_switch.setOnClickListener(View ->{
            String url = "https://play.google.com/store/apps/details?id=com.colorswitch.switch2&hl=en";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No browser found to open the link.", Toast.LENGTH_SHORT).show();
            }
        });
        pictionary.setOnClickListener(View ->{
            String url = "https://play.google.com/store/apps/details?id=com.nt.pictionary&hl=en";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No browser found to open the link.", Toast.LENGTH_SHORT).show();
            }
        });
        quick_draw.setOnClickListener(View ->{
            String url = "https://quickdraw.withgoogle.com";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No browser found to open the link.", Toast.LENGTH_SHORT).show();
            }
        });
        music_tap.setOnClickListener(View ->{
            String url = "https://play.google.com/store/apps/details?id=com.music.tap.tap.tile&hl=en";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No browser found to open the link.", Toast.LENGTH_SHORT).show();
            }
        });
        tower_of_hanoi.setOnClickListener(View ->{
            String url = "https://play.google.com/store/apps/details?id=com.atagan.tower_of_hanoi&hl=en";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No browser found to open the link.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}