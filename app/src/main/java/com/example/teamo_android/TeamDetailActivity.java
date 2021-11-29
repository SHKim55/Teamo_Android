package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TeamDetailActivity extends AppCompatActivity {
    private TextView temp;

    private Team team;
    private String tempText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        getDataFromIntent();
        initElements();
    }

    private void getDataFromIntent() {
        Intent prev_intent = getIntent();
        tempText = prev_intent.getStringExtra("temp");
    }

    private void initElements() {
        temp = (TextView) findViewById(R.id.text_temp_team_detail);
        temp.setText(tempText);
    }
}