package com.example.abinath.styleomega.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.abinath.styleomega.R;
import com.example.abinath.styleomega.adapters.ProductListAdapter;
import com.example.abinath.styleomega.model.Product;
import com.example.abinath.styleomega.model.User;
import com.example.abinath.styleomega.sql.DatabaseHelper;

import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private User user;

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private ProductListAdapter mAdapter;
    private static String TAG = CartActivity.class.getSimpleName();
    HashMap<Product,Integer> products;
    public final Context mContext = this;
    double total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = getIntent().getParcelableExtra("USER");
        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        products = DatabaseHelper.getInstance(this).getAllCartProducts(user);
        ((TextView) findViewById(R.id.totalPriceTxt)).setText("Total Price: LKR 0.0");
        if(products != null && products.size() > 0) {
            for (Object o : products.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                Product p = (Product) entry.getKey();
                int quantity = (int) entry.getValue();
                total += quantity * p.getPrice();
            }

            ((TextView) findViewById(R.id.totalPriceTxt)).setText("Total Price: LKR " + total);
            mAdapter = new ProductListAdapter(this, R.layout.product_list_item, user, products);
            Product[] productArray = new Product[products.size()];
            products.keySet().toArray(productArray);
            Log.d(TAG, "onCreate: " + productArray[0].getName());

            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public void checkOutBtnClicked(View view){
        if(products == null || products.size() <= 0 )
            return;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Complete Purchase");
        builder1.setMessage("Total Price: "+total);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Buy",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        for (Object o : products.entrySet()) {
                            Map.Entry entry = (Map.Entry) o;
                            Product p = (Product) entry.getKey();
                            DatabaseHelper.getInstance(mContext).checkOut(p,user, products.get(p));
                            products.clear();
                            mRecyclerView.setAdapter(new ProductListAdapter(mContext, R.layout.product_list_item , user, products));
                        }
                        total = 0.0;
                        ((TextView)findViewById(R.id.totalPriceTxt)).setText("Total Price: LKR "+total);
                    }
                });

        builder1.setNegativeButton(
                "Return",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public void updateFromDB(){
        products = DatabaseHelper.getInstance(this).getAllCartProducts(user);
        mAdapter = new ProductListAdapter(this, R.layout.product_list_item , user, products);
        Product[] productArray = new Product[products.size()];
        products.keySet().toArray(productArray);
        Log.d(TAG, "onCreate: "+productArray[0].getName());
        double total = 0;
        for (Object o : products.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Product p = (Product) entry.getKey();
            int quantity = (int) entry.getValue();
            total+=quantity*p.getPrice();
        }
        ((TextView)findViewById(R.id.totalPriceTxt)).setText("Total Price: LKR "+total);
        mRecyclerView.setAdapter(mAdapter);
    }

}
