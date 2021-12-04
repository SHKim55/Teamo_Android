package com.example.teamo_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class RequestListActivity extends AppCompatActivity {
    private RecyclerView requestListRV;
    private RecyclerView.LayoutManager layoutManager;
    private RequestListRVAdapter adapter;
    private RequestQueue queue;
    private int pageNum = 0, total = 0;
    public ArrayList<User> usersData = new ArrayList<User>();

    public ArrayList<User> requestUsersData = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);
        requestListRV = (RecyclerView) findViewById(R.id.rv_requests_request_list);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        requestListRV.setLayoutManager(layoutManager);
        adapter = new RequestListRVAdapter(requestUsersData);
        requestListRV.setAdapter(adapter);

        loadData();

        adapter.setItemClickListener(new RequestListRVAdapter.RequestListItemClickListener() {
            @Override
            public void onItemClick(User user) {

            }

            @Override
            public void onApprovalButtonClick(User user) {

            }

            @Override
            public void onWaitingButtonClick(int index) {

            }
        });

    }

    private void loadData() {

    }
}
