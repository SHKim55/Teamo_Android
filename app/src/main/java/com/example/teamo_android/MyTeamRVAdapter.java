package com.example.teamo_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyTeamRVAdapter extends RecyclerView.Adapter<MyTeamRVAdapter.MyTeamViewHolder> {
    interface MyTeamItemClickListener {
        void onRequestListButtonClick(Team team);
        void onMenuButtonClick(Team team, int index);
        void onTeamDetailClick(Team team);
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

        // 가입 요청 목록 페이지로 이동
        viewHolder.itemView.findViewById(R.id.btn_request_list_my_team).setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onRequestListButtonClick(myTeamsData.get(selectedPosition));
            }
        });

        // 팝업 메뉴
        viewHolder.itemView.findViewById(R.id.btn_menu_my_team).setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onMenuButtonClick(myTeamsData.get(selectedPosition), selectedPosition);
            }
        });

        viewHolder.itemView.findViewById(R.id.layout_info_my_team).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onTeamDetailClick(myTeamsData.get(viewHolder.getBindingAdapterPosition()));
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
        ImageView menuButton, requestListButton;

        public MyTeamViewHolder(@NonNull View itemView, MyTeamItemClickListener itemClickListener) {
            super(itemView);
        }

        public void bind(Team team) {
            titleTv = (TextView) itemView.findViewById(R.id.text_title_my_team);
            descriptionTv = (TextView) itemView.findViewById(R.id.text_description_my_team);
            memberCountTv = (TextView) itemView.findViewById(R.id.text_member_count_my_team);
            recentUpdateTv = (TextView) itemView.findViewById(R.id.text_recent_update_my_team);
            menuButton = (ImageView) itemView.findViewById(R.id.btn_menu_my_team);
            requestListButton = (ImageView) itemView.findViewById(R.id.btn_request_list_my_team);

            String memberNumText = team.getMaxMemberNum() + " 명 모집 중";

            titleTv.setText(team.getTitle());
            descriptionTv.setText(team.getTag());
            memberCountTv.setText(memberNumText);
        }
    }
}
