package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamo_android.databinding.ActivityRequestedMessageBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestedMessageActivity extends AppCompatActivity {
    private ActivityRequestedMessageBinding binding;
    private RequestQueue queue;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_requested_message);
        initElements();
        getMessageContent();
    }

    private void initElements() {
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        token = preferences.getString("Authorization", "");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_requested_message);
        queue = Volley.newRequestQueue(getApplicationContext());

        binding.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(RequestedMessageActivity.this, RequestListActivity.class);
                finish();
//                startActivity(intent);
            }
        });

        binding.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendResponseToServer("approval");
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "서버 통신 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
        binding.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendResponseToServer("rejection");
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "서버 통신 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });
    }

    private void getMessageContent() {
        Intent intent= getIntent();
        String messageContentApi = getString(R.string.url) + "/team/message/" + intent.getStringExtra("team_id") + "/" + intent.getStringExtra("member_id");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, messageContentApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String newLine = System.getProperty("line.separator");
                            String senderInfo = response.getString("department") + " " + response.getString("std_num") + newLine + response.getString("name");
                            binding.receiverInfo.setText(senderInfo);
                            binding.acceptText.setText(response.getString("content"));
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


    private void sendResponseToServer(String api) throws JSONException {
        Intent intent = getIntent();
        String responseApi = getString(R.string.url) + "/team/" + api + "/" + intent.getStringExtra("team_id") +  "/" + intent.getStringExtra("member_id");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, responseApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(request);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
