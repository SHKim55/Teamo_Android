package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TeamDetailActivity extends AppCompatActivity {
    private TextView temp;

    private Team team;
    private int tempId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        getDataFromIntent();
        initElements();
    }

    private void getDataFromIntent() {
        Intent prev_intent = getIntent();
        tempId = prev_intent.getIntExtra("id", 0);

        // 넘겨받은 아이디 값과 일치하는 아이디를 가진 Team 정보를 서버로부터 불러와 team에 저장한다.
    }

    private void initElements() {
        temp = (TextView) findViewById(R.id.text_temp_team_detail);
    }
}