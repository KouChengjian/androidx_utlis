package com.example.test.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_recycler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , RecyclerActivity.class);
                startActivity(intent);
            }
        });
    }
}
