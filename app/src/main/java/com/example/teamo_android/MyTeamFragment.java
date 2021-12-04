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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

public class MyTeamFragment extends Fragment {
    private RecyclerView myTeamRV;
    private RecyclerView.LayoutManager layoutManager;
    private int pageNum = 0, total = 0;
    public ArrayList<Team> myTeamsData = new ArrayList<Team>();
    private MyTeamRVAdapter adapter;
    private RequestQueue queue;
    private String token;
    private boolean checkMore = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_team, container, false);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("token", MODE_PRIVATE);
        token = sharedPreferences.getString("Authorization", "");
        myTeamRV = (RecyclerView) view.findViewById(R.id.rv_teams_my_team);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        myTeamRV.setLayoutManager(layoutManager);
        queue = Volley.newRequestQueue(requireContext());

        adapter = new MyTeamRVAdapter(myTeamsData);
        myTeamRV.setAdapter(adapter);

        myTeamRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();
                Log.i("size", String.valueOf(myTeamsData.size()-1));
                if(position == myTeamsData.size()-1 && checkMore) {
                    getMorePost();
                }
            }
        });

        loadData();
        adapter.setItemClickListener(new MyTeamRVAdapter.MyTeamItemClickListener() {
            @Override
            public void onRequestListButtonClick(Team team) {
                Log.d("선택된 팀 정보", team.getTeamId() + " " + team.getTag());

                Intent intent = new Intent(requireContext(), TeamDetailActivity.class);
                intent.putExtra("id", team.getTeamId());
                startActivity(intent);
            }

            @Override
            public void onMenuButtonClick(Team team, int index) {
                ImageView menuButton = requireActivity().findViewById(R.id.btn_menu_my_team);

                final PopupMenu popupMenu = new PopupMenu(getContext(), menuButton);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.edit_menu_popup){
                            Intent intent = new Intent(getContext(), CreateTeamActivity.class);
                            intent.putExtra("team", team);
                            startActivity(intent);
                        } else {
                            myTeamsData.remove(index);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        return view;
    }

    // 실제 서버에서 해당 데이터를 받아오는 파트
    private void loadData() {

        Log.i("token", token);
        String myPostingApi = getString(R.string.url) + "/posting/myPostings/host/" + pageNum;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, myPostingApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int len = (int) response.get("number");
                            JSONArray postings = response.getJSONArray("postings");
                            Log.i("postings", postings.toString());

                            if(len == 0) {
                                Toast.makeText(getContext(), "모든 글을 불러왔습니다", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getContext(), String.valueOf(len), Toast.LENGTH_SHORT).show();
                                for(int i = 0; i<len; i++) {
                                    JSONObject object = postings.getJSONObject(i);
                                    Team team = new Team(object.get("id").toString(), object.getString("title"), Integer.parseInt(object.getString("member_number")),
                                            object.getString("subject"), object.getString("semester"), object.getString("professor"), object.getString("class"),
                                            object.getString("date"), object.getString("content"), object.getString("writer"));
                                    myTeamsData.add(team);
                                }
                                total += len;
                                adapter.notifyItemInserted(myTeamsData.size() - 1);
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
        Log.i("tag", "이 포스트는 " + pageNum + "번째");
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
                                    Log.i("postings", postings.toString());

                                    if(len == 0) {
                                        Toast.makeText(getContext(), "모든 글을 불러왔습니다", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getContext(), String.valueOf(len), Toast.LENGTH_SHORT).show();
                                        for(int i = 0; i<len; i++) {
                                            JSONObject object = postings.getJSONObject(i);
                                            Team team = new Team(object.get("id").toString(), object.getString("title"), Integer.parseInt(object.getString("member_number")),
                                                    object.getString("subject"), object.getString("semester"), object.getString("professor"), object.getString("class"),
                                                    object.getString("date"), object.getString("content"), object.getString("writer"));
                                            Log.i("writer", object.getString("writer"));
                                            myTeamsData.add(team);
                                            // if(i == len - 1) pageNum++;
                                        }
                                        total += len;
                                        adapter.notifyItemInserted(myTeamsData.size() - 1);
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