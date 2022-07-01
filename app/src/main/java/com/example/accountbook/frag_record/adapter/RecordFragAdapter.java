package com.example.accountbook.frag_record.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecordFragAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragments;
    private String[] tabNames = {"支出","收入"};

    public RecordFragAdapter(@NonNull FragmentActivity fragmentActivity,ArrayList<Fragment> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;

    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return this.fragments.size();
    }

    // 获取tabName
    public String getTabName(int position) {
        return tabNames[position];
    }
}
