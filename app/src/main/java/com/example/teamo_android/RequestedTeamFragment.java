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
        requestedTeamRV.setLayoutManager(layoutManager);

        RequestedTeamRVAdapter adapter = new RequestedTeamRVAdapter(requestedTeamsData);
        requestedTeamRV.setAdapter(adapter);

        adapter.setItemClickListener(new RequestedTeamRVAdapter.RequestedTeamItemClickListener() {
            @Override
            public void onItemClick(Team team) {
                Log.d("선택된 팀 정보", team.getTeamId() + " " + team.getTag());


            }

            @Override
            public void onApprovalButtonClick(Team team) {
                Intent intent = new Intent(requireContext(), TeamDetailActivity.class);
                intent.putExtra("team", team);
                startActivity(intent);
            }

            @Override
            public void onCancelButtonClick(int index) {
                requestedTeamsData.remove(index);

                Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT);
            }
        });

        return view;
    }

    private void initTempDB() {
        if (!requestedTeamsData.isEmpty())
            requestedTeamsData.clear();

    }

    // 실제 서버에서 해당 데이터를 받아오는 파트
    private void loadData() {

    }
}