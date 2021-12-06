package com.example.teamo_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private ImageView logoButton, createButton, mypageButton;
    private TextView logoutButton;
    private RecyclerView teamsRV;
    private RecyclerView.LayoutManager layoutManager;
    private TeamsRVAdapter adapter;
    private int pageNum = 0, total = 0;
    private User currentUser;
    private String idText, passwordText, writerId;
    private boolean checkMore = true;

    public ArrayList<Team> teamsData = new ArrayList<Team>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDataFromIntent();
        initElements();
        initRV();
        getPostList();
    }

    private void getDataFromIntent() {
        Intent prev_intent = getIntent();
        idText = prev_intent.getStringExtra("id");
        passwordText = prev_intent.getStringExtra("password");
    }

    private void getPostList() {
        String postingGetApi = getString(R.string.url) + "/posting/allPostings/" + pageNum;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, postingGetApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int len = (int) response.get("number");
                            JSONArray postings = response.getJSONArray("postings");
                            Log.i("postings", postings.toString());

                            if(len == 0) {
                                Toast.makeText(MainActivity.this, "모든 글을 불러왔습니다", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                for(int i = 0; i<len; i++) {
                                    JSONObject object = postings.getJSONObject(i);
                                    Team team = new Team(object.get("id").toString(), object.getString("title"), Integer.parseInt(object.getString("member_number")),
                                            object.getString("subject"), object.getString("semester"), object.getString("professor"), object.getString("class"),
                                            object.getString("date"), object.getString("content"), object.getString("writer"));
                                    teamsData.add(team);
                                }
                                total += len;
                                adapter.notifyItemInserted(teamsData.size() - 1);
                                pageNum++;
                            }
                            if(len != 20) checkMore = false;
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "서버 통신 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    void getMorePost() {
        String postingGetApi = getString(R.string.url) + "/posting/allPostings/" + pageNum;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, postingGetApi, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int len = (int) response.get("number");
                                    JSONArray postings = response.getJSONArray("postings");

                                    if(len == 0) {
                                        Toast.makeText(MainActivity.this, "모든 글을 불러왔습니다", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        for(int i = 0; i<len; i++) {
                                            JSONObject object = postings.getJSONObject(i);
                                            Team team = new Team(object.get("id").toString(), object.getString("title"), Integer.parseInt(object.getString("member_number")),
                                                    object.getString("subject"), object.getString("semester"), object.getString("professor"), object.getString("class"),
                                                    object.getString("date"), object.getString("content"), object.getString("writer"));
                                            Log.i("writer", object.getString("writer"));
                                            teamsData.add(team);
                                            // if(i == len - 1) pageNum++;
                                        }
                                        total += len;
                                        adapter.notifyItemInserted(teamsData.size() - 1);
                                        pageNum++;
                                    }
                                    if(len != 20) checkMore = false;
                                } catch(JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "서버 통신 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(request);
            }
        }, 500);

    }

    private void initElements() {
        queue = Volley.newRequestQueue(MainActivity.this);
        logoButton = (ImageView) findViewById(R.id.img_teamo_main);
        createButton = (ImageView) findViewById(R.id.btn_create_main);
        mypageButton = (ImageView) findViewById(R.id.btn_mypage_main);
        logoutButton = (TextView) findViewById(R.id.text_logout_main);

        logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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

        teamsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();
                Log.i("size", String.valueOf(teamsData.size()-1));
                if(position == teamsData.size()-1 && checkMore) {
                    getMorePost();
                }
            }
        });

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        adapter = new TeamsRVAdapter(teamsData);
        teamsRV.setAdapter(adapter);

        adapter.setItemClickListener(new TeamsRVAdapter.TeamsItemClickListener() {
            @Override
            public void onItemClick(Team team) {
                Log.d("선택된 팀 정보", team.getTeamId() + " " + team.getTag());

                Intent intent = new Intent(MainActivity.this, TeamDetailActivity.class);
                intent.putExtra("team", team);
                startActivity(intent);
            }
        });

        teamsRV.setLayoutManager(layoutManager);
    }
}