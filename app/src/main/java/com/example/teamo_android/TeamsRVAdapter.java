package com.example.teamo_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamsRVAdapter extends RecyclerView.Adapter<TeamsRVAdapter.TeamsViewHolder> {
    interface TeamsItemClickListener { void onItemClick(Team team); }

    private TeamsItemClickListener itemClickListener;
    private ArrayList<Team> teamsData;

    public TeamsRVAdapter(ArrayList<Team> data) { this.teamsData = data; }

    @NonNull
    @Override
    public TeamsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_team, parent, false);
        return new TeamsViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsViewHolder viewHolder, int position) {
        viewHolder.bind(teamsData.get(position));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(teamsData.get(selectedPosition));
            }
        });
    }

    @Override
    public int getItemCount() { return teamsData.size(); }

    public void setItemClickListener(TeamsItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class TeamsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv, descriptionTv, memberCountTv, recentUpdateTv;

        public TeamsViewHolder(@NonNull View itemView, TeamsItemClickListener itemClickListener) {
            super(itemView);
        }

        public void bind(Team team) {
            titleTv = (TextView) itemView.findViewById(R.id.text_title_team);
            descriptionTv = (TextView) itemView.findViewById(R.id.text_description_team);
            memberCountTv = (TextView) itemView.findViewById(R.id.text_member_count_team);
            recentUpdateTv = (TextView) itemView.findViewById(R.id.text_recent_update_team);

            // 팀 인원 현황 추후 반영
            String memberNumText = "1 / " + team.getMaxNumber() + "  모집 중";

            // 최근 업데이트 시간 반영 기능 추후 추가
            //String updateText = "방금 전";

            titleTv.setText(team.getTitle());
            descriptionTv.setText(team.getTag());
            memberCountTv.setText(memberNumText);
            //recentUpdateTv.setText("");
        }
    }
}
