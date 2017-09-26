package com.example.abinath.styleomega.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.abinath.styleomega.R;
import com.example.abinath.styleomega.adapters.ProductPagerAdapter;
import com.example.abinath.styleomega.model.User;
import com.example.abinath.styleomega.sql.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by Abinath on 26-Aug-17.
 */

public class UsersActivity extends AppCompatActivity {

    private TextView textViewName;
    User user;
    public static UsersActivity activity;

    public static UsersActivity getInstance(){
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        activity = this;


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ArrayList<String> genders = DatabaseHelper.getInstance(this).getProductGender();
        for(String gender : genders)
            tabLayout.addTab(tabLayout.newTab().setText(gender));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        user = getIntent().getParcelableExtra("USER");
        final ProductPagerAdapter adapter = new ProductPagerAdapter
                (getSupportFragmentManager(), genders, user);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Starting login activity
                        Intent intent = new Intent(UsersActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            logout();
        } else if (id == R.id.menuCart) {
            goToCart();
        } else if (id == R.id.menuUser){
            gotoManageAccount();
        }


        return super.onOptionsItemSelected(item);
    }

    public void goToCart() {
        //CartFragment cf = CartFragment.newInstance(user);
        Intent i = new Intent(this, CartActivity.class);
        i.putExtra("USER", user);
        startActivity(i);

        /*FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_layout,cf,"Cart_fragment");
        transaction.addToBackStack("cart");
        transaction.commit();*/
    }

    public void gotoManageAccount() {
        Intent i = new Intent(this, ManageAccountActivity.class);
        i.putExtra("USER", user);
        startActivity(i);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
