package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.hwang_gyojun.hyu_se.R.id.fragment_layout;

public class RetrieveFragment extends Fragment implements AdapterView.OnItemClickListener {
    private OnFragmentInteractionListener mListener;
    private ListView list;
    private AutoCompleteAdapter adapter;
    private AutoCompleteTextView auto_search;
    private boolean doubleBackToExitPressedOnce;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_retrieve, container, false);
        list = (ListView) view.findViewById(R.id.retrieve_list);

        auto_search = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        adapter =  new AutoCompleteAdapter(getActivity(),android.R.layout.simple_list_item_1);
        auto_search.setAdapter(adapter);
        auto_search.setOnItemClickListener(this);

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (doubleBackToExitPressedOnce) {
                            getActivity().finish();
                            return true;
                        }

                        doubleBackToExitPressedOnce = true;
                        Toast.makeText(getActivity(), "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                doubleBackToExitPressedOnce = false;
                            }
                        }, 2000);

                        return true;
                    }
                }
                return false;
            }
        });

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RetrieveItem item = (RetrieveItem) parent.getItemAtPosition(position);

        ResultFragment newFragment = new ResultFragment();

        // replace fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("name", item.getName());
        bundle.putString("sub_id", item.getSubID());
        newFragment.setArguments(bundle);

        transaction.replace(fragment_layout, newFragment);

        InputMethodManager imm= (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(auto_search.getWindowToken(), 0);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
