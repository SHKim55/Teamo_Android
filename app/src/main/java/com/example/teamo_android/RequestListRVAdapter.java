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

public class RequestListRVAdapter extends RecyclerView.Adapter<RequestListRVAdapter.RequestListViewHolder> {
    interface RequestListItemClickListener {
        void onItemClick(User user);
        void onApprovalButtonClick(User user);
        void onWaitButtonClick(int index);
    }

    private RequestListRVAdapter.RequestListItemClickListener itemClickListener;
    private ArrayList<User> requestListData;

    public RequestListRVAdapter(ArrayList<User> data) { this.requestListData = data; }

    @NonNull
    @Override
    public RequestListRVAdapter.RequestListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_request_list, parent, false);
        return new RequestListRVAdapter.RequestListViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestListRVAdapter.RequestListViewHolder viewHolder, int position) {
        viewHolder.bind(requestListData.get(position));

        viewHolder.itemView.findViewById(R.id.layout_info_request_list).setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(requestListData.get(selectedPosition));
            }
        });

        viewHolder.itemView.findViewById(R.id.text_waiting_request_list).setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onWaitButtonClick(selectedPosition);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() { return requestListData.size(); }

    public void setItemClickListener(RequestListRVAdapter.RequestListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class RequestListViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, departmentTv, studentNumTv, dateTv, timeTv;
        TextView approveTv, denyTv, waitTv;

        public RequestListViewHolder(@NonNull View itemView, RequestListRVAdapter.RequestListItemClickListener itemClickListener) {
            super(itemView);
        }

        public void bind(User user) {
            nameTv = (TextView) itemView.findViewById(R.id.text_name_request_list);
            departmentTv = (TextView) itemView.findViewById(R.id.text_department_request_list);
            studentNumTv = (TextView) itemView.findViewById(R.id.text_student_num_request_list);
            dateTv = (TextView) itemView.findViewById(R.id.text_date_request_list);
            timeTv = (TextView) itemView.findViewById(R.id.text_time_request_list);
            approveTv = (TextView) itemView.findViewById(R.id.text_approval_request_list);
            denyTv = (TextView) itemView.findViewById(R.id.text_denial_request_list);
            waitTv = (TextView) itemView.findViewById(R.id.text_waiting_request_list);

            //String updateText = "방금 전";

            nameTv.setText(user.getUserName());
            departmentTv.setText(user.getDepartment());
            studentNumTv.setText(user.getStudentNum());
            //recentUpdateTv.setText("");

            switch(checkRequestStatus("approved")) {
                case -1:
                    waitTv.setVisibility(View.INVISIBLE);
                    approveTv.setVisibility(View.INVISIBLE);
                    denyTv.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    waitTv.setVisibility(View.VISIBLE);
                    approveTv.setVisibility(View.INVISIBLE);
                    denyTv.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    waitTv.setVisibility(View.INVISIBLE);
                    approveTv.setVisibility(View.VISIBLE);
                    denyTv.setVisibility(View.INVISIBLE);
                    break;
            }
        }

        private int checkRequestStatus(String requestMessage) {
            if(requestMessage.equals("approved"))
                return 1;
            else if(requestMessage.equals("denied"))
                return -1;
            else
                return 0;
        }
    }

}
