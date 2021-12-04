package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamo_android.databinding.ActivityTeamDetailBinding;

public class TeamDetailActivity extends AppCompatActivity {
    private RequestQueue queue;
    private ActivityTeamDetailBinding binding;
    private Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        getDataFromIntent();
        initElements();
        getWriterInfo();
    }

    private void getDataFromIntent() {

    }

    private void initElements() {
        Intent prev_intent = getIntent();
        team = (Team) prev_intent.getSerializableExtra("team");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_team_detail);
        queue = Volley.newRequestQueue(getApplicationContext());

        binding.postingTitle.setText(team.getTitle());
        binding.postingContent.setText(team.getTag());
        binding.postingDate.setText(team.getDate());
        binding.postingTag.setText(team.getTag());
        binding.postingContent.setText(team.getContent());
        binding.postingMemberNum.setText(team.getMaxMemberNum());
    }

    private void getWriterInfo() {
        String contentApi = getString(R.string.url) + "user/profile/" + team.getWriterId();
    }
}