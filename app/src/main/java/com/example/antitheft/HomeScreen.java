package com.example.antitheft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {

    private Button applicationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        applicationBtn = findViewById(R.id.view_applications);

        applicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToApplicationsScreen();
            }
        });

    }

    private void moveToApplicationsScreen() {

        Intent intent  = new Intent(HomeScreen.this, PhoneApplications.class);
        startActivity(intent);
    }
}