package com.example.teamo_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class RequestedTeamFragment extends Fragment {
    private RecyclerView requestedTeamRV;
    private RecyclerView.LayoutManager layoutManager;

    public ArrayList<Team> requestedTeamsData = new ArrayList<Team>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requested_team, container, false);
        initTempDB();
        //loadData();

        requestedTeamRV = (RecyclerView) view.findViewById(R.id.rv_teams_requested_team);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        RequestedTeamRVAdapter adapter = new RequestedTeamRVAdapter(requestedTeamsData);
        requestedTeamRV.setAdapter(adapter);

        adapter.setItemClickListener(new RequestedTeamRVAdapter.RequestedTeamItemClickListener() {
            @Override
            public void onItemClick(Team team) {
                Log.d("선택된 팀 정보", team.getTeamId() + " " + team.getContent());

                Intent intent = new Intent(requireContext(), TeamDetailActivity.class);
                intent.putExtra("id", team.getTeamId());
                startActivity(intent);
            }

            @Override
            public void onRequestButtonClick(Team team) {

            }

            @Override
            public void onCancelButtonClick(int index) {
                requestedTeamsData.remove(index);

                Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT);
            }
        });

        requestedTeamRV.setLayoutManager(layoutManager);

        return view;
    }

    private void initTempDB() {
        if (!requestedTeamsData.isEmpty())
            requestedTeamsData.clear();

        requestedTeamsData.add(new Team(2, "백엔드 킹갓이 DB 버스 태워드립니다.", "김재훈", 3,
                "데이터베이스설계", "2021-2", "강현철", 3));

        requestedTeamsData.add(new Team(4, "캡디 팀원 구합니다.", "이수민", 6,
                "캡스톤디자인 I", "2021-2", "김은우", 1));

        requestedTeamsData.add(new Team(5, "말하는 감자들 환영합니다.", "김선호", 5,
                "기초컴퓨터프로그래밍", "2021-2", "이창하", 1));

    }

    // 실제 서버에서 해당 데이터를 받아오는 파트
    private void loadData() {

    }
}