package com.example.abinath.styleomega.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.abinath.styleomega.R;
import com.example.abinath.styleomega.model.User;
import com.example.abinath.styleomega.sql.DatabaseHelper;

public class ManageAccountActivity extends AppCompatActivity {

    private User user;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText address;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        user = getIntent().getParcelableExtra("USER");
        name = (EditText) findViewById(R.id.nameTxt);
        email = (EditText) findViewById(R.id.emailTxt);
        password = (EditText) findViewById(R.id.passwordTxt);
        address = (EditText) findViewById(R.id.addressTxt);
        phone = (EditText) findViewById(R.id.phoneNumberTxt);

        name.setText(user.getName());
        email.setText(user.getEmail());
        password.setText(user.getPassword());
        address.setText(user.getAddress());
        phone.setText(user.getPhone());
    }

    public void updateAccount(View view) {
        user.setAddress(address.getText().toString());
        user.setName(name.getText().toString());
        user.setPassword(password.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setEmail(email.getText().toString());

        UsersActivity.getInstance().setUser(user);

        DatabaseHelper.getInstance(this).updateUser(user);

    }
}
