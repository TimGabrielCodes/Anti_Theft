package com.example.antitheft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import java.util.List;
import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {

    private Button signInBtn;
    String saved_pattern_key = "pattern_code";
    PatternLockView mPatternLockView;
    String final_pattern = "final_pattern";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Paper.init(this);
        final String saved_pattern = Paper.book().read(saved_pattern_key);

        if (saved_pattern != null && !saved_pattern.equals("null")) {

            setContentView(R.layout.pattern_screen);
            mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
            mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {

                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {
                    final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
                    if (final_pattern.equals(saved_pattern)){
                        Toast.makeText(MainActivity.this, "Pattern Correct!", Toast.LENGTH_SHORT).show();
                        moveToHomeScreen();
                    }

                    else {
                        Toast.makeText(MainActivity.this, "Pattern Incorrect", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onCleared() {

                }
            });
        } else {
            setContentView(R.layout.activity_lock_screen);
            mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
            mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {
                    Toast.makeText(MainActivity.this, "Set  Lock Screen Pattern" , Toast.LENGTH_SHORT);
                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {

                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {
                    final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
                }

                @Override
                public void onCleared() {

                }
            });

            Button btnSetup = (Button) findViewById(R.id.btnSetPattern);

            btnSetup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Paper.book().write(saved_pattern_key, final_pattern);
                    Toast.makeText(MainActivity.this, "Pattern Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        }


    }

    private void moveToHomeScreen() {

        Intent intent = new Intent(MainActivity.this, HomeScreen.class);
        startActivity(intent);
        Toast.makeText(MainActivity.this, "Welcome!.", Toast.LENGTH_SHORT).show();

    }
}