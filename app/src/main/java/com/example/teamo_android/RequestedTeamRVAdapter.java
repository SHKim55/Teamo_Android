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

public class RequestedTeamRVAdapter extends RecyclerView.Adapter<RequestedTeamRVAdapter.RequestedTeamViewHolder> {
    interface RequestedTeamItemClickListener {
        void onItemClick(Team team);
        void onApprovalButtonClick(Team team);
        void onCancelButtonClick(int index);
    }

    private RequestedTeamItemClickListener itemClickListener;
    private ArrayList<Team> requestedTeamsData;

    public RequestedTeamRVAdapter(ArrayList<Team> data) { this.requestedTeamsData = data; }

    @NonNull
    @Override
    public RequestedTeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_requested_team, parent, false);
        return new RequestedTeamViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedTeamViewHolder viewHolder, int position) {
        viewHolder.bind(requestedTeamsData.get(position));

        viewHolder.itemView.findViewById(R.id.layout_info_requested_team).setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(requestedTeamsData.get(selectedPosition));
            }
        });

        viewHolder.itemView.findViewById(R.id.btn_cancel_requested_team).setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onCancelButtonClick(selectedPosition);
                notifyDataSetChanged();
            }
        });

        viewHolder.itemView.findViewById(R.id.btn_approval_requested_team).setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onApprovalButtonClick(requestedTeamsData.get(selectedPosition));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() { return requestedTeamsData.size(); }

    public void setItemClickListener(RequestedTeamItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class RequestedTeamViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv, descriptionTv, memberCountTv, recentUpdateTv;
        ImageView approveButton, denyButton, cancelButton, newNotificationIv;

        public RequestedTeamViewHolder(@NonNull View itemView, RequestedTeamItemClickListener itemClickListener) {
            super(itemView);
        }

        public void bind(Team team) {
            titleTv = (TextView) itemView.findViewById(R.id.text_title_requested_team);
            descriptionTv = (TextView) itemView.findViewById(R.id.text_description_requested_team);
            memberCountTv = (TextView) itemView.findViewById(R.id.text_member_count_requested_team);
            recentUpdateTv = (TextView) itemView.findViewById(R.id.text_recent_update_requested_team);
            cancelButton = (ImageView) itemView.findViewById(R.id.btn_cancel_requested_team);
            approveButton = (ImageView) itemView.findViewById(R.id.btn_approval_requested_team);
            denyButton = (ImageView) itemView.findViewById(R.id.btn_denial_requested_team);
            newNotificationIv = (ImageView) itemView.findViewById(R.id.btn_new_notification_requested_team);

            // 팀 인원 현황 추후 반영
            String memberNumText = "1 / " + team.getMaxMemberNum() + "  모집 중";

            // 최근 업데이트 시간 반영 기능 추후 추가
            //String updateText = "방금 전";

            titleTv.setText(team.getTitle());
            descriptionTv.setText(team.getTag());
            memberCountTv.setText(memberNumText);
            //recentUpdateTv.setText("");

            switch(checkRequestStatus()) {
                case -1:
                    cancelButton.setVisibility(View.INVISIBLE);
                    approveButton.setVisibility(View.INVISIBLE);
                    denyButton.setVisibility(View.VISIBLE);
                    newNotificationIv.setVisibility(View.GONE);
                    break;
                case 0:
                    cancelButton.setVisibility(View.VISIBLE);
                    approveButton.setVisibility(View.INVISIBLE);
                    denyButton.setVisibility(View.INVISIBLE);
                    newNotificationIv.setVisibility(View.GONE);
                    break;
                case 1:
                    cancelButton.setVisibility(View.INVISIBLE);
                    approveButton.setVisibility(View.VISIBLE);
                    denyButton.setVisibility(View.INVISIBLE);
                    newNotificationIv.setVisibility(View.VISIBLE);
                    break;
            }
        }

        private int checkRequestStatus() {
//            if(팀 가입이 승인되었다면)
//                return 1;
//            else if(팀 가입이 반려되었다면)
//                return -1;
//            else
                return 1;
        }
    }
}
