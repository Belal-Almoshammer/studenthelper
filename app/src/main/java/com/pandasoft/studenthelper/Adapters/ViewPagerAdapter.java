package com.pandasoft.studenthelper.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

;

import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> mList;

    public ViewPagerAdapter(ArrayList<Fragment> list,  FragmentManager fragmentManager,  Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        mList = list;
    }


    @NonNull

    @Override
    public Fragment createFragment(int position) {
        return Objects.requireNonNull(mList).get(position);
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(mList).size();
    }
}
