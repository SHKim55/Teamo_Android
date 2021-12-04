package com.example.teamo_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class MypageActivity extends AppCompatActivity {
    private ImageView backButton;
    private TextView name, department, admissionYear;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private String token = null;
    private static SharedPreferences preferences;
    private static MypageActivity _instance;

    public User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        _instance = this;
        initElements();
        initVP();
    }

    private void initElements() {
        backButton = (ImageView) findViewById(R.id.btn_back_mypage);
        name = (TextView) findViewById(R.id.text_name_mypage);
        department = (TextView) findViewById(R.id.text_department_mypage);
        admissionYear = (TextView) findViewById(R.id.text_admission_year_mypage);

        // 임시 유저 정보 입력. 현재 로그인 한 유저 정보를 받아와야함


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MypageActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void initVP() {
        tabLayout = (TabLayout) findViewById(R.id.tab_content_mypage);
        viewPager2 = (ViewPager2) findViewById(R.id.vp_content_mypage);
        ArrayList<String> menus = new ArrayList<String>();
        menus.add("내가 만든 팀");
        menus.add("가입 요청한 팀");

        MypageVPAdapter mypageVPAdapter = new MypageVPAdapter(this);
        viewPager2.setAdapter(mypageVPAdapter);

        TabLayoutMediator layoutMediator = new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(menus.get(position));
                    }
                });
        layoutMediator.attach();
    }
}