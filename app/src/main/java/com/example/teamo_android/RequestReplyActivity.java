package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.teamo_android.databinding.ActivityRequestReplyBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestReplyActivity extends AppCompatActivity {
    private RequestQueue queue;
    private ActivityRequestReplyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_request_reply);
        initElements();
    }

    private void initElements() {
        queue = Volley.newRequestQueue(getApplicationContext());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_reply);

        getMessageContent();

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReplyToServer();
                finish();
            }
        });

        binding.receiverExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getMessageContent() {
        Intent intent= getIntent();
        String messageContentApi = getString(R.string.url) + "/user/profile/" + intent.getStringExtra("sender_id");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, messageContentApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String newLine = System.getProperty("line.separator");
                            String senderInfo = response.getString("department") + " " + response.getString("std_num") + newLine + response.getString("name");
                            binding.receiverInfo.setText(senderInfo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(request);
    }


    private void sendReplyToServer() {
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = preferences.getString("Authorization", "");
        Intent intent = getIntent();
        String leaderReplyApi = getString(R.string.url) + "/team/message";
        JSONObject reply = new JSONObject();
        try {
            reply.put("team_id", intent.getStringExtra("team_id"));
            reply.put("receiver", intent.getStringExtra("sender_id"));
            reply.put("content", binding.receiverEdit.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, leaderReplyApi, reply,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "전송에 성공했습니다", Toast.LENGTH_SHORT).show();
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