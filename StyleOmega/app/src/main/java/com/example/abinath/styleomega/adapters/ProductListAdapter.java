package com.example.abinath.styleomega.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abinath.styleomega.adapters.ViewHolders.ProductViewHolder;
import com.example.abinath.styleomega.model.Product;
import com.example.abinath.styleomega.model.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abinath on 10-Sep-17.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private Context mContext;
    private int layoutresource;
    private User user;
    private boolean isCart;
    private ArrayList<Product> products;
    private HashMap<Product,Integer> cartProducts;
    private static String TAG = ProductListAdapter.class.getSimpleName();

    public ProductListAdapter(Context mContext, int layoutresource, User user,ArrayList<Product> products) {
        this.mContext = mContext;
        this.layoutresource = layoutresource;
        this.user = user;
        this.isCart = false;
        this.products = products;
    }

    public ProductListAdapter(Context mContext, int layoutresource, User user, HashMap<Product,Integer> products) {
        this.mContext = mContext;
        this.layoutresource = layoutresource;
        this.user = user;
        this.isCart = true;
        this.cartProducts = products;
        Log.d(TAG, "ProductListAdapter: "+products.size());
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutresource, parent, false);
        return new ProductViewHolder(view,mContext);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        if(isCart){
            Product[] productArray = new Product[cartProducts.size()];
            cartProducts.keySet().toArray(productArray);
            Log.d(TAG, "onBindViewHolder: "+productArray[position].getId());
            holder.setValues(productArray[position],user,isCart, cartProducts.get(productArray[position]));
        }else {
            holder.setValues(products.get(position), user, isCart,0);
        }
    }

    @Override
    public int getItemCount() {
        return products!=null ? products.size() : cartProducts != null ? cartProducts.size() : 0;
    }
}
