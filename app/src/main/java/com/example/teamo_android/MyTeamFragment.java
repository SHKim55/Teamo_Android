package com.example.teamo_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ComponentActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MyTeamFragment extends Fragment {
    private RecyclerView myTeamRV;
    private RecyclerView.LayoutManager layoutManager;

    public ArrayList<Team> myTeamsData = new ArrayList<Team>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_team, container, false);
        initTempDB();
        //loadData();

        myTeamRV = (RecyclerView) view.findViewById(R.id.rv_teams_my_team);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        myTeamRV.setLayoutManager(layoutManager);

        MyTeamRVAdapter adapter = new MyTeamRVAdapter(myTeamsData);
        myTeamRV.setAdapter(adapter);

        adapter.setItemClickListener(new MyTeamRVAdapter.MyTeamItemClickListener() {
            @Override
            public void onItemClick(Team team) {
                Log.d("선택된 팀 정보", team.getTeamId() + " " + team.getContent());

                Intent intent = new Intent(requireContext(), TeamDetailActivity.class);
                intent.putExtra("id", team.getTeamId());
                startActivity(intent);
            }

            @Override
            public void onEditButtonClick(Team team) {
                // 글 수정 기능 추가
            }

            @Override
            public void onDeleteButtonClick(int index) {
                myTeamsData.remove(index);

                Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT);
            }
        });

        return view;
    }

    private void initTempDB() {
        if (!myTeamsData.isEmpty())
            myTeamsData.clear();

        myTeamsData.add(new Team("1", "객지프 팀플 버스 태워드립니다.", "김은솔", 4,
                "객체지향프로그래밍", "2021-2", "손봉수", "2"));

        myTeamsData.add(new Team("3", "방탈출 같이 하실 분?", "김은솔", 4,
                "프로그래밍", "2021-2", "조용진", "2"));
    }

    // 실제 서버에서 해당 데이터를 받아오는 파트
    private void loadData() {

    }
}