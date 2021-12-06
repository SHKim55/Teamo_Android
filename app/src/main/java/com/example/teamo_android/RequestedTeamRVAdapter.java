package com.example.teamo_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestedTeamRVAdapter extends RecyclerView.Adapter<RequestedTeamRVAdapter.RequestedTeamViewHolder> {
    interface RequestedTeamItemClickListener {
        void onItemClick(Team team);
        void onApprovalButtonClick(Team team);
    }

    private RequestedTeamItemClickListener itemClickListener;
    private final ArrayList<Team> requestedTeamsData;
    private static Context mContext;
    private static String token;

    public RequestedTeamRVAdapter(ArrayList<Team> data, Context context, String token) {
        this.requestedTeamsData = data;
        mContext = context;
        RequestedTeamRVAdapter.token = token;
    }

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
            final int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(requestedTeamsData.get(selectedPosition));
            }
        });

        viewHolder.itemView.findViewById(R.id.btn_approval_requested_team).setOnClickListener(new View.OnClickListener() {
            final int selectedPosition = viewHolder.getBindingAdapterPosition();

            @Override
            public void onClick(View view) {
                itemClickListener.onApprovalButtonClick(requestedTeamsData.get(selectedPosition));
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
            cancelButton = (ImageView) itemView.findViewById(R.id.btn_wait_requested_team);
            approveButton = (ImageView) itemView.findViewById(R.id.btn_approval_requested_team);
            denyButton = (ImageView) itemView.findViewById(R.id.btn_denial_requested_team);
            newNotificationIv = (ImageView) itemView.findViewById(R.id.btn_new_notification_requested_team);

            // 팀 인원 현황 추후 반영
            String memberNumText = team.getMaxMemberNum() + "명 " + "모집 중";

            titleTv.setText(team.getTitle());
            descriptionTv.setText(team.getTag());
            memberCountTv.setText(memberNumText);
            RequestQueue queue = Volley.newRequestQueue(mContext);
            String userStateApi = mContext.getString(R.string.url) + "/team/state/" + team.getTeamId();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, userStateApi, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String state = response.getString("state");
                                switch(checkRequestStatus(state)) {
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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
