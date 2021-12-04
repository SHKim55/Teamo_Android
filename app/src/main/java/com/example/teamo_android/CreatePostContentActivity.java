package com.example.teamo_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamo_android.databinding.ActivityCreatePostContentBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreatePostContentActivity extends AppCompatActivity {
    private RequestQueue queue;
    private boolean checkTitle = false, checkPeopleNum = false, checkContent = false;
    private int peopleNum;
    private ActivityCreatePostContentBinding binding;
    private ArrayAdapter numAdapter;
    private Team newTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post_content);

        initElements();
        initOnClickListener();
    }

    private void initElements() {
        // intent로 넘긴 Team 객체 받아옴
        Intent intent = getIntent();
        newTeam = (Team) intent.getSerializableExtra("team");
        Log.i("logging", newTeam.getSemester());

        // binding 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post_content);

        // 스피너 adapter 초기화
        numAdapter = ArrayAdapter.createFromResource(this, R.array.people_num, R.layout.spinner_item);
        numAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.peopleNumSpinner.setAdapter(numAdapter);
        queue = Volley.newRequestQueue(CreatePostContentActivity.this);
    }

    private void initOnClickListener(){
        binding.peopleNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                checkPeopleNum = i != 0;    // 0이면 false, 나머지는 true
                String maxPeopleNum = binding.peopleNumSpinner.getItemAtPosition(i).toString();
                if(checkPeopleNum) peopleNum = Integer.parseInt(maxPeopleNum);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.inputSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewTeam();
                if(checkValidTeam()) {  // 모두 입력되었을 때
                    sendToServer();
                    Intent intent = new Intent(CreatePostContentActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(CreatePostContentActivity.this, "모든 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setNewTeam() {
        String title = binding.teamTitleEdit.getText().toString();
        if(!title.isEmpty() && !(title == null)) {
            checkTitle = true;
            newTeam.setTitle(title);
        }

        if(checkPeopleNum) {
            newTeam.setMaxMemberNum(peopleNum);
        }

        String content = binding.postingContentEdit.getText().toString();
        if(!content.isEmpty() && !(content == null)) {
            checkContent = true;
            newTeam.setTag(content);
        }
    }

    private void sendToServer() {
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        JSONObject posting = new JSONObject();
        String postingApi = getString(R.string.url) + "/posting";
        try {
            posting.put("title", newTeam.getTitle());
            posting.put("content", newTeam.getTag());
            posting.put("member_number", newTeam.getMaxMemberNum());
            posting.put("subject", newTeam.getSubject());
            posting.put("semester", newTeam.getSemester());
            posting.put("professor", newTeam.getProfessor());
            posting.put("class", newTeam.getCourseClass());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("posting", newTeam.getSemester());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postingApi, posting,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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

    private boolean checkValidTeam() {
        return checkContent && checkTitle && checkPeopleNum;
    }
}