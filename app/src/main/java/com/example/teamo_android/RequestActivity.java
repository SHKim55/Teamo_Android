package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamo_android.databinding.ActivityRequestBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {
    private ActivityRequestBinding binding;
    private RequestQueue queue;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_request);
        initElements();
        initListener();
    }

    private void initElements() {
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        token = preferences.getString("Authorization", "");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_request);
        queue = Volley.newRequestQueue(getApplicationContext());
        getUserInfo();

    }

    private void initListener() {
        binding.requestExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.requestSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestToServer();
                finish();
            }
        });
    }

    private void sendRequestToServer() {
        String sendRequestApi = getString(R.string.url) + "/team/message";
        Intent intent = getIntent();
        JSONObject requestContent = new JSONObject();
        try {
            requestContent.put("team_id", intent.getStringExtra("team_id"));
            requestContent.put("receiver", intent.getStringExtra("writer_id"));
            requestContent.put("content", binding.requestEdit.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, sendRequestApi, requestContent,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "요청을 보내지 못했습니다", Toast.LENGTH_SHORT).show();
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
        String sendRequestTeamApi = getString(R.string.url) + "/team/request/" + intent.getStringExtra("team_id");
        request = new JsonObjectRequest(Request.Method.GET, sendRequestTeamApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "가입 요청을 성공적으로 보냈습니다", Toast.LENGTH_SHORT).show();
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

    private void getUserInfo() {
        String getUserInfoApi = getString(R.string.url) + "/user/profile";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUserInfoApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String receiverInfo =response.getString("department") + " " + response.getString("std_num") + " " + response.getString("name");
                            binding.receiverInfo.setText(receiverInfo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "서버 통신 시 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
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