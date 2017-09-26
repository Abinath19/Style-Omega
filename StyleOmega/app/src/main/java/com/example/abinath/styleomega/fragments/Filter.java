package com.example.abinath.styleomega.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.abinath.styleomega.model.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Filter.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Filter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Filter extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GENDER = "gender_param";

    // TODO: Rename and change types of parameters
    private String gender;

    private OnFragmentInteractionListener mListener;

    public Filter() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gender The Gender to apply the filter.
     * @return A new instance of fragment Filter.
     */
    // TODO: Rename and change types and number of parameters
    public static Filter newInstance(String gender) {
        Filter fragment = new Filter();
        Bundle args = new Bundle();
        args.putString(GENDER, gender);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gender = getArguments().getString(GENDER);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Fragment fragment, ArrayList<Product> products);
    }
}
