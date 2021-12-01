package com.example.teamo_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MypageVPAdapter extends FragmentStateAdapter {
    public MypageVPAdapter(AppCompatActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyTeamFragment();
            case 1:
                return new RequestedTeamFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() { return 2; }
}
