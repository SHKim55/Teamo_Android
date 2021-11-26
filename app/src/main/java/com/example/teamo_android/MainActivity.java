package com.example.teamo_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ImageView tempButton, notificationButton;
    private BottomNavigationView mainBnv;
    private String idText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDataFromIntent();
        initElements();
        initNavigation();
    }

    private void getDataFromIntent() {
        Intent prev_intent = getIntent();
        idText = prev_intent.getStringExtra("id");
        passwordText = prev_intent.getStringExtra("password");
    }

    private void initNavigation() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new SearchFragment()).commit();
    }

    private void initElements() {
        tempButton = (ImageView) findViewById(R.id.img_teamo_main);
        notificationButton = (ImageView) findViewById(R.id.btn_notification_main);
        mainBnv = (BottomNavigationView) findViewById(R.id.bnv_main);

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "알림 설정 기능은 추후 추가될 예정입니다.", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
//                finish();
//                startActivity(intent);
            }
        });

        mainBnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.searchFragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new SearchFragment()).commit();
                        break;

                    case R.id.mypageFragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new MypageFragment()).commit();
                        break;

                    case R.id.createProjectActivity:
                        Intent intent = new Intent(MainActivity.this, CreateTeamActivity.class);
                        startActivity(intent);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new SearchFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }
}