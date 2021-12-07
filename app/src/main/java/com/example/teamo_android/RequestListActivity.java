package com.example.teamo_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestListActivity extends AppCompatActivity {
    private RecyclerView requestListRV;
    private RecyclerView.LayoutManager layoutManager;
    private RequestListRVAdapter adapter;
    private RequestQueue queue;
    private int pageNum = 0, total = 0;
    private boolean checkAgain = false;
    public ArrayList<Member> requestUsersData = new ArrayList<Member>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        queue = Volley.newRequestQueue(this);
        requestListRV = (RecyclerView) findViewById(R.id.rv_requests_request_list);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        requestListRV.setLayoutManager(layoutManager);
        adapter = new RequestListRVAdapter(requestUsersData);
        requestListRV.setAdapter(adapter);

        adapter.setItemClickListener(new RequestListRVAdapter.RequestListItemClickListener() {
            @Override
            public void onItemClick(Member member) {
                Intent intent = new Intent(RequestListActivity.this, RequestedMessageActivity.class);
                intent.putExtra("team_id", member.getTeamId());
                intent.putExtra("member_id", member.getMemberId());
                int prevSize = requestUsersData.size();
                requestUsersData.clear();
                adapter.notifyDataSetChanged();
                startActivity(intent);
            }

            @Override
            public void onApprovalButtonClick(Member member) {
                Intent intent = new Intent(RequestListActivity.this, RequestReplyActivity.class);
                intent.putExtra("team_id", member.getTeamId());
                intent.putExtra("sender_id", member.getMemberId());
                int prevSize = requestUsersData.size();
                requestUsersData.clear();
                adapter.notifyDataSetChanged();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        Intent intent = getIntent();
        String teamId = intent.getStringExtra("id");
        String requestedListApi = getString(R.string.url) + "/team/member/" + teamId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, requestedListApi, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i<response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                Member member = new Member(teamId, object.getString("name"), object.getString("department"), object.getString("std_num"),
                                        object.getString("state"), object.getString("memberId"));
                                requestUsersData.add(member);
                            }
                            adapter.notifyItemInserted(requestUsersData.size() - 1);
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
}
