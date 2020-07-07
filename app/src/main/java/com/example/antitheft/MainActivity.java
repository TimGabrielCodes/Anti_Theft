package com.example.antitheft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Button  signInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    signInBtn = findViewById(R.id.btn_signin);

    signInBtn.setOnClickListener(new View.OnClickListener(){

        public void onClick(View v){
            moveToHomeScreen();
        }

    });

    }
    private void  moveToHomeScreen(){

        Intent intent = new Intent(MainActivity.this, HomeScreen.class);
        startActivity(intent);
        Toast.makeText(MainActivity.this, "Welcome!.", Toast.LENGTH_SHORT).show();

    }
}