package com.example.teamo_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MypageVPAdapter extends FragmentStateAdapter {
    private AppCompatActivity activity;
    public MypageVPAdapter(AppCompatActivity fragment) {
        super(fragment);
        this.activity = fragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        SharedPreferences preferences = activity.getSharedPreferences("token", Context.MODE_PRIVATE);
        String token = preferences.getString("Authorization", "");
        switch (position) {
            case 0:
                return new MyTeamFragment();
            case 1:
                Bundle bundle = new Bundle();
                bundle.putString("token", token);
                RequestedTeamFragment fragment = new RequestedTeamFragment();
                fragment.setArguments(bundle);
                return fragment;
        }
        return null;
    }

    @Override
    public int getItemCount() { return 2; }
}
