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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MypageActivity extends AppCompatActivity {
    private ImageView backButton;
    private TextView name, department, admissionYear;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private String token = null;
    private RequestQueue queue;

    public User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        initElements();
        initVP();
    }

    private void initElements() {
        backButton = (ImageView) findViewById(R.id.btn_back_mypage);
        name = (TextView) findViewById(R.id.text_name_mypage);
        department = (TextView) findViewById(R.id.text_department_mypage);
        admissionYear = (TextView) findViewById(R.id.text_admission_year_mypage);
        queue = Volley.newRequestQueue(getApplicationContext());

        // 임시 유저 정보 입력. 현재 로그인 한 유저 정보를 받아와야함
        getUser();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MypageActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void getUser() {
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = preferences.getString("Authorization", "");
        String getUserApi = getString(R.string.url) + "/user/profile";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUserApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            name.setText(response.getString("name"));
                            department.setText(response.getString("department"));
                            admissionYear.setText(response.getString("std_num"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "서버 통신 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("Authorization", "Bearer " + token);
                return heads;
            }
        };
        queue.add(request);
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