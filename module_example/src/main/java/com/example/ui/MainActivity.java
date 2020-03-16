package com.example.kernel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.R;
import com.example.kernel.ui.core.activity.MvpCoreExampleActivity;
import com.yiciyuan.apt.annotation.Router;

import androidx.appcompat.app.AppCompatActivity;

@Router("dasdada")
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
