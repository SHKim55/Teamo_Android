package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamo_android.databinding.ActivityTeamDetailBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class TeamDetailActivity extends AppCompatActivity {
    private RequestQueue queue;
    private ActivityTeamDetailBinding binding;
    private Team team;
    private String name = "null", department = "null", std_num = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        initElements();
        initListener();
    }

    private void initListener() {
        binding.postingApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 신청 팝업
//                Intent intent = new Intent(TeamDetailActivity.this, RequestActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void initElements() {
        Intent prev_intent = getIntent();
        team = (Team) prev_intent.getSerializableExtra("team");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_team_detail);
        queue = Volley.newRequestQueue(getApplicationContext());
        //String semester = team.getSemester().par
        String maxMemberNumber = "모집 인원 : " + String.valueOf(team.getMaxMemberNum());
        binding.postingTitle.setText(team.getTitle());
        binding.postingDate.setText(team.getDate().substring(0, 10));
        binding.postingTag.setText(team.getTag());
        binding.postingContent.setText(team.getContent());
        binding.postingMemberNum.setText(maxMemberNumber);
        getWriterInfo();
    }

    private void getWriterInfo() {
        Log.i("team Writer",  team.getWriterId());
        String contentApi = getString(R.string.url) + "/user/profile/" + team.getWriterId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, contentApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            name = response.getString("name");
                            department = response.getString("department");
                            std_num = response.getString("std_num");
                            Log.i("posting", name);
                            Log.i("posting", department);
                            Log.i("posting", std_num);
                            binding.textNameMypage.setText(name);
                            binding.textDepartmentMypage.setText(department);
                            binding.textAdmissionYearMypage.setText(std_num);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "서버 통신 시 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}