package com.example.teamo_android;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyTeamRVAdapter extends RecyclerView.Adapter<MyTeamRVAdapter.MyTeamViewHolder> {
    interface MyTeamItemClickListener {
        void onItemClick(Team team);
        void onEditButtonClick(Team team);
        void onDeleteButtonClick(int index);
    }

    private MyTeamItemClickListener itemClickListener;
    private ArrayList<Team> myTeamsData;

    public MyTeamRVAdapter(ArrayList<Team> data) { this.myTeamsData = data; }

    @NonNull
    @Override
    public MyTeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_my_team, parent, false);
        return new MyTeamViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTeamViewHolder viewHolder, int position) {
        viewHolder.bind(myTeamsData.get(position));

        viewHolder.itemView.findViewById(R.id.layout_info_my_team).setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(myTeamsData.get(selectedPosition));
            }
        });

        viewHolder.itemView.findViewById(R.id.btn_edit_my_team).setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onEditButtonClick(myTeamsData.get(selectedPosition));
            }
        });

        viewHolder.itemView.findViewById(R.id.btn_delete_my_team).setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onDeleteButtonClick(selectedPosition);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() { return myTeamsData.size(); }

    public void setItemClickListener(MyTeamItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class MyTeamViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv, descriptionTv, memberCountTv, recentUpdateTv;
        ImageView editButton, deleteButton;

        public MyTeamViewHolder(@NonNull View itemView, MyTeamItemClickListener itemClickListener) {
            super(itemView);
        }

        public void bind(Team team) {
            titleTv = (TextView) itemView.findViewById(R.id.text_title_my_team);
            descriptionTv = (TextView) itemView.findViewById(R.id.text_description_my_team);
            memberCountTv = (TextView) itemView.findViewById(R.id.text_member_count_my_team);
            recentUpdateTv = (TextView) itemView.findViewById(R.id.text_recent_update_my_team);
            editButton = (ImageView) itemView.findViewById(R.id.btn_edit_my_team);
            deleteButton = (ImageView) itemView.findViewById(R.id.btn_delete_my_team);

            // 팀 인원 현황 추후 반영
            String memberNumText = "1 / " + team.getMaxNumber() + "  모집 중";

            // 최근 업데이트 시간 반영 기능 추후 추가
            //String updateText = "방금 전";

            titleTv.setText(team.getTitle());
            descriptionTv.setText(team.getContent());
            memberCountTv.setText(memberNumText);
            //recentUpdateTv.setText("");
        }
    }
}
