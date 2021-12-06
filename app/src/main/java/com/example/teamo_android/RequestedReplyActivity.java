package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamo_android.databinding.ActivityRequestedReplyBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestedReplyActivity extends AppCompatActivity {
    private RequestQueue queue;
    private ActivityRequestedReplyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_requested_reply);
        initElements();
    }

    private void initElements() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_requested_reply);
        queue = Volley.newRequestQueue(getApplicationContext());
        getReply();

        binding.receiverExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getReply() {
        Intent intent = getIntent();
        Team team = (Team) intent.getSerializableExtra("team");
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = preferences.getString("Authorization", "");

        String replyApi = getString(R.string.url) + "/team/message/" + team.getTeamId() + "/" + team.getWriterId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, replyApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String writerInfo = response.getString("department") + " " + response.getString("std_num") + " " + response.getString("name");
                            String content = response.getString("content");
                            binding.replyWriterInfo.setText(writerInfo);
                            binding.replyText.setText(content);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("Authorization", "Bearer " + token);
                return heads;
            }
        };
        queue.add(request);
    }
}