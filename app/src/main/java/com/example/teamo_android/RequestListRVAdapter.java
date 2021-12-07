package com.example.teamo_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestListRVAdapter extends RecyclerView.Adapter<RequestListRVAdapter.RequestListViewHolder> {
    interface RequestListItemClickListener {
        void onItemClick(Member member);
        void onApprovalButtonClick(Member member);
    }

    private RequestListRVAdapter.RequestListItemClickListener itemClickListener;
    private ArrayList<Member> requestListData;

    public RequestListRVAdapter(ArrayList<Member> data) { this.requestListData = data; }

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

        viewHolder.itemView.findViewById(R.id.text_approval_request_list).setOnClickListener(new View.OnClickListener() {
            int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onApprovalButtonClick(requestListData.get(selectedPosition));
            }
        });
    }

    @Override
    public int getItemCount() { return requestListData.size(); }

    public void setItemClickListener(RequestListRVAdapter.RequestListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class RequestListViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, departmentTv, studentNumTv, infoTv;
        TextView approveTv, denyTv, waitTv;

        public RequestListViewHolder(@NonNull View itemView, RequestListRVAdapter.RequestListItemClickListener itemClickListener) {
            super(itemView);
        }

        public void bind(Member member) {
            nameTv = (TextView) itemView.findViewById(R.id.text_name_request_list);
            departmentTv = (TextView) itemView.findViewById(R.id.text_department_request_list);
            studentNumTv = (TextView) itemView.findViewById(R.id.text_student_num_request_list);
            infoTv = (TextView) itemView.findViewById(R.id.text_info_request_list);
            approveTv = (TextView) itemView.findViewById(R.id.text_approval_request_list);
            denyTv = (TextView) itemView.findViewById(R.id.text_denial_request_list);
            waitTv = (TextView) itemView.findViewById(R.id.text_waiting_request_list);

            nameTv.setText(member.getName());
            departmentTv.setText(member.getDepartment());
            studentNumTv.setText(member.getStdNum());

            switch(checkRequestStatus(member.getState())) {
                case -1:
                    waitTv.setVisibility(View.INVISIBLE);
                    approveTv.setVisibility(View.INVISIBLE);
                    denyTv.setVisibility(View.VISIBLE);
                    infoTv.setText("해당 멤버의 가입을 거절하였습니다.");
                    break;
                case 0:
                    waitTv.setVisibility(View.VISIBLE);
                    approveTv.setVisibility(View.INVISIBLE);
                    denyTv.setVisibility(View.INVISIBLE);
                    infoTv.setText("오른쪽 버튼을 눌러 해당 유저의 팀 가입 여부를 설정해주세요.");
                    break;
                case 1:
                    waitTv.setVisibility(View.INVISIBLE);
                    approveTv.setVisibility(View.VISIBLE);
                    denyTv.setVisibility(View.INVISIBLE);
                    infoTv.setText("해당 멤버의 가입을 승인하였습니다.");
                    break;
            }
        }

        private int checkRequestStatus(String requestMessage) {
            if(requestMessage.equals("approve"))
                return 1;
            else if(requestMessage.equals("reject"))
                return -1;
            else
                return 0;
        }
    }

}
