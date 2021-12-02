package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView logoButton, notificationButton, searchButton, createButton, mypageButton;
    private RecyclerView teamsRV;
    private RecyclerView.LayoutManager layoutManager;

    private User currentUser;
    private String idText, passwordText;

    public ArrayList<Team> teamsData = new ArrayList<Team>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDataFromIntent();
        initTempDB();
        initElements();
        initRV();
    }

    private void getDataFromIntent() {
        Intent prev_intent = getIntent();
        idText = prev_intent.getStringExtra("id");
        passwordText = prev_intent.getStringExtra("password");
    }

    // 리사이클러뷰 테스트를 위한 임시 DB 생성
    private void initTempDB() {
        if (!teamsData.isEmpty())
            teamsData.clear();

        teamsData.add(new Team("1", "객지프 팀플 버스 태워드립니다.", "김은솔", 4,
                "객체지향프로그래밍", "2021-2", "손봉수", "2"));

        teamsData.add(new Team("2", "백엔드 킹갓이 DB 버스 태워드립니다.", "김재훈", 3,
                "데이터베이스설계", "2021-2", "강현철", "3"));

        teamsData.add(new Team("3", "방탈출 같이 하실 분?", "김은솔", 4,
                "프로그래밍", "2021-2", "조용진", "2"));

        teamsData.add(new Team("4", "캡디 팀원 구합니다.", "이수민", 6,
                "캡스톤디자인 I", "2021-2", "김은우", "1"));

        teamsData.add(new Team("5", "말하는 감자들 환영합니다.", "김선호", 5,
                "기초컴퓨터프로그래밍", "2021-2", "이창하", "1"));
    }

    private void initElements() {
        logoButton = (ImageView) findViewById(R.id.img_teamo_main);
        notificationButton = (ImageView) findViewById(R.id.btn_notification_main);
        searchButton = (ImageView) findViewById(R.id.btn_search_main);
        createButton = (ImageView) findViewById(R.id.btn_create_main);
        mypageButton = (ImageView) findViewById(R.id.btn_mypage_main);

        logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateTeamActivity.class);
                startActivity(intent);
            }
        });

        mypageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRV() {
        teamsRV = (RecyclerView) findViewById(R.id.rv_teams_main);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        TeamsRVAdapter adapter = new TeamsRVAdapter(teamsData);
        teamsRV.setAdapter(adapter);

        adapter.setItemClickListener(new TeamsRVAdapter.TeamsItemClickListener() {
            @Override
            public void onItemClick(Team team) {
                Log.d("선택된 팀 정보", team.getTeamId() + " " + team.getContent());

                Intent intent = new Intent(MainActivity.this, TeamDetailActivity.class);
                intent.putExtra("id", team.getTeamId());
                startActivity(intent);
            }
        });

        teamsRV.setLayoutManager(layoutManager);
    }
}