package com.example.abinath.styleomega.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.abinath.styleomega.R;
import com.example.abinath.styleomega.adapters.ProductListAdapter;
import com.example.abinath.styleomega.model.User;
import com.example.abinath.styleomega.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GENDER_TEXT = "gender";
    private static final String USER = "user";

    // TODO: Rename and change types of parameters
    private String gender;
    private User user;

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private ProductListAdapter mAdapter;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gender The gender of the products to display.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String gender, User user) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(GENDER_TEXT, gender);
        args.putParcelable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gender = getArguments().getString(GENDER_TEXT);
            user = getArguments().getParcelable(USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.listView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ProductListAdapter(getContext(), R.layout.product_list_item , user, DatabaseHelper.getInstance(getContext()).getProductForGender(gender));
        mRecyclerView.setAdapter(mAdapter);

        final EditText lowerPrice = (EditText) view.findViewById(R.id.lowerPrice);
        final EditText higherPrice = (EditText) view.findViewById(R.id.higherPrice);

        List<String> types = new ArrayList<>();
        types.add("");
        types.addAll(DatabaseHelper.getInstance(getContext()).getProductTypes(gender));

        final Spinner typeSpinner = (Spinner) view.findViewById(R.id.typeSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(dataAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapter = new ProductListAdapter(getContext(),
                        R.layout.product_list_item ,
                        user,  DatabaseHelper.getInstance(getContext()).getProductForArgs(null,
                                gender,
                                (String)typeSpinner.getSelectedItem(),
                                !lowerPrice.getText().toString().isEmpty() ? Double.parseDouble(lowerPrice.getText().toString()) : 0,
                                !higherPrice.getText().toString().isEmpty() ? Double.parseDouble(higherPrice.getText().toString()) : 0));
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
}
