package com.example.abinath.styleomega.adapters.ViewHolders;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abinath.styleomega.R;
import com.example.abinath.styleomega.activities.CartActivity;
import com.example.abinath.styleomega.model.Product;
import com.example.abinath.styleomega.model.User;
import com.example.abinath.styleomega.sql.DatabaseHelper;
import com.squareup.picasso.Picasso;

/**
 * Created by Abinath on 10-Sep-17.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public TextView productName;
    public ImageView productImage;
    private Context mContext;
    private View itemView;
    private static String TAG = ProductViewHolder.class.getSimpleName();

    public ProductViewHolder(View itemView, Context context) {
        super(itemView);
        this.itemView = itemView;

        productName = (TextView) itemView.findViewById(R.id.product_name);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        mContext = context;
    }

    public void setValues(final Product product, final User user, final boolean isCart, final int quantityAmt){
        productName.setText(product.getName());
        Picasso.with(mContext).load(product.getImage()).into(productImage);
        Log.d(TAG, "setValues: "+product.getImage());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialog = LayoutInflater.from(mContext).inflate(R.layout.product_details_dialog,null);
                //dialog.setContentView(R.layout.product_details_dialog);

                // set the custom dialog components - text, image and button
                TextView name = (TextView) dialog.findViewById(R.id.product_name);
                ImageView image = (ImageView) dialog.findViewById(R.id.product_image);
                TextView price = (TextView) dialog.findViewById(R.id.product_price);
                TextView gender = (TextView) dialog.findViewById(R.id.product_gender);
                TextView type = (TextView) dialog.findViewById(R.id.product_type);
                TextView quantity = (TextView) dialog.findViewById(R.id.product_quantity);
                final TextView purchaseQuantity = (TextView) dialog.findViewById(R.id.purchase_quantity);

                name.setText(product.getName());
                Picasso.with(mContext).load(product.getImage()).into(image);
                price.setText(Double.toString(product.getPrice()));
                gender.setText(product.getGender());
                type.setText(product.getType());
                quantity.setText("Quantity: "+product.getQuantity());
                purchaseQuantity.setText(Integer.toString(quantityAmt));

                Button plusButton = (Button) dialog.findViewById(R.id.PlusButton);
                Button minusButton = (Button) dialog.findViewById(R.id.MinusButton);

                plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int curr = Integer.parseInt(purchaseQuantity.getText().toString());
                        if(curr<product.getQuantity()){
                            curr++;
                            purchaseQuantity.setText(Integer.toString(curr));
                        }
                    }
                });

                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int curr = Integer.parseInt(purchaseQuantity.getText().toString());
                        if(curr>0){
                            curr--;
                            purchaseQuantity.setText(Integer.toString(curr));
                        }
                    }
                });

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                // this is set the view from XML inside AlertDialog
                alert.setView(dialog);

                alert.setCancelable(true);

                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alert.setPositiveButton(isCart ? "Update Cart" : "Add To Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper.getInstance(mContext).addToCart(user, product, Integer.parseInt(purchaseQuantity.getText().toString()));
                        if(isCart){
                            ((CartActivity)mContext).updateFromDB();
                        }
                    }
                });

                AlertDialog dialogBox = alert.create();
                dialogBox.show();
            }
        });
    }
}
