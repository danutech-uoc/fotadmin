package com.example.fotadmin.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fotadmin.fragments.NewsCategoryFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private String[] categories;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String[] categories) {
        super(fragmentActivity);
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return NewsCategoryFragment.newInstance(categories[position]);
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }
}
