package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private ListView list_view;
    private ArrayAdapter<String> array_adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        list_view = (ListView) view.findViewById(R.id.listView);

        array_adapter = new ArrayAdapter <String> (getActivity(), android.R.layout.simple_list_item_1);
        list_view.setAdapter(array_adapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment newFragment = new ResultFragment();

                // replace fragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_layout, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
        array_adapter.add("사과");
        array_adapter.add("배");
        array_adapter.add("사과");
        array_adapter.add("배");
        array_adapter.add("사과");
        array_adapter.add("배");
        array_adapter.add("사과");
        array_adapter.add("배");
        array_adapter.add("사과");
        array_adapter.add("배");
        array_adapter.add("사과");
        array_adapter.add("배");
        array_adapter.add("사과");
        array_adapter.add("배");
        array_adapter.add("사과");
        array_adapter.add("배");
        array_adapter.add("사과");
        array_adapter.add("배");
        array_adapter.add("사과");
        array_adapter.add("배");
        array_adapter.add("사과");
        array_adapter.add("배");
        array_adapter.add("사과");
        array_adapter.add("배");

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }

}
