package com.example.teamo_android;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestedTeamFragment extends Fragment {
    private RecyclerView requestedTeamRV;
    private RecyclerView.LayoutManager layoutManager;
    private RequestedTeamRVAdapter adapter;
    private RequestQueue queue;
    private int pageNum = 0, total = 0;
    public ArrayList<Team> requestedTeamsData = new ArrayList<Team>();
    private String token;
    private boolean checkMore = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requested_team, container, false);
        queue = Volley.newRequestQueue(requireContext());
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("token", MODE_PRIVATE);
        token = sharedPreferences.getString("Authorization", "");
        requestedTeamRV = (RecyclerView) view.findViewById(R.id.rv_teams_requested_team);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        requestedTeamRV.setLayoutManager(layoutManager);
        adapter = new RequestedTeamRVAdapter(requestedTeamsData, getActivity().getApplicationContext(), token);
        requestedTeamRV.setAdapter(adapter);


        requestedTeamRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();
                if(position == requestedTeamsData.size()-1 && checkMore) {
                    getMorePost();
                }
            }
        });

        adapter.setItemClickListener(new RequestedTeamRVAdapter.RequestedTeamItemClickListener() {
            @Override
            public void onItemClick(Team team) {
                Intent intent = new Intent(requireContext(), TeamDetailActivity.class);
                intent.putExtra("team", team);
                startActivity(intent);
                Log.d("선택된 팀 정보", team.getTeamId() + " " + team.getTag());
            }

            @Override
            public void onApprovalButtonClick(Team team) {
                Intent intent = new Intent(requireContext(), RequestedReplyActivity.class);
                intent.putExtra("team", team);
                startActivity(intent);
            }
        });

        loadData();

        return view;
    }

    // 실제 서버에서 해당 데이터를 받아오는 파트
    private void loadData() {
        String myPostingApi = getString(R.string.url) + "/posting/myPostings/member/" + pageNum;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, myPostingApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int len = (int) response.get("number");
                            JSONArray postings = response.getJSONArray("postings");

                            if(len == 0) {
                                Toast.makeText(getContext(), "모든 글을 불러왔습니다", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                for(int i = 0; i<len; i++) {
                                    JSONObject object = postings.getJSONObject(i);
                                    Team team = new Team(object.get("id").toString(), object.getString("title"), Integer.parseInt(object.getString("member_number")),
                                            object.getString("subject"), object.getString("semester"), object.getString("professor"), object.getString("class"),
                                            object.getString("date"), object.getString("content"), object.getString("writer"));
                                    requestedTeamsData.add(team);
                                }
                                total += len;
                                adapter.notifyItemInserted(requestedTeamsData.size() - 1);
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
                Toast.makeText(getContext(), "서버 통신 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
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

    private void getMorePost() {
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
                                        Toast.makeText(getContext(), "모든 글을 불러왔습니다", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        for(int i = 0; i<len; i++) {
                                            JSONObject object = postings.getJSONObject(i);
                                            Team team = new Team(object.get("id").toString(), object.getString("title"), Integer.parseInt(object.getString("member_number")),
                                                    object.getString("subject"), object.getString("semester"), object.getString("professor"), object.getString("class"),
                                                    object.getString("date"), object.getString("content"), object.getString("writer"));
                                            requestedTeamsData.add(team);
                                        }
                                        total += len;
                                        adapter.notifyItemInserted(requestedTeamsData.size() - 1);
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
                        Toast.makeText(getContext(), "서버 통신 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(request);
            }
        }, 500);
    }
}