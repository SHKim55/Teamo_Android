package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tempText;
    private String idText, passwordText, stringData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDataFromIntent();
        initElements();
    }

    private void getDataFromIntent() {
        Intent prev_intent = getIntent();
        idText = prev_intent.getStringExtra("id");
        passwordText = prev_intent.getStringExtra("password");

        stringData = idText + " " + passwordText;
    }

    private void initElements() {
        tempText = (TextView) findViewById(R.id.text_temp_main);
        tempText.setText(stringData);

        tempText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}