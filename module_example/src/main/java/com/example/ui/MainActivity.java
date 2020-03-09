package com.example.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.R;
import com.example.ui.core.activity.MvpCoreExampleActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_mvp_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MvpCoreExampleActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_mvp_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
