package com.example.abinath.styleomega.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.abinath.styleomega.fragments.ProductFragment;
import com.example.abinath.styleomega.model.User;

import java.util.ArrayList;

/**
 * Created by Abinath on 10-Sep-17.
 */

public class ProductPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<String> genders;
    private User user;

    public ProductPagerAdapter(FragmentManager fm, ArrayList<String> names, User user) {
        super(fm);
        this.genders = names;
        this.user = user;
    }

    @Override
    public Fragment getItem(int position) {
        return ProductFragment.newInstance(genders.get(position), user);
    }

    @Override
    public int getCount() {
        return genders.size();
    }
}
