package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchIndexFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private ListView main_category;
    private ArrayAdapter<String> main_category_adapter;
    private ListView sub_category;
    private ArrayAdapter<String> sub_category_adapter;
    private ListView ssub_category;
    private ArrayAdapter<String> ssub_category_adapter;

    public SearchIndexFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search_index, container, false);
        main_category = (ListView) view.findViewById(R.id.main_category);

        main_category_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        main_category.setAdapter(main_category_adapter);

        sub_category = (ListView) view.findViewById(R.id.sub_category);

        sub_category_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        sub_category.setAdapter(sub_category_adapter);

        ssub_category = (ListView) view.findViewById(R.id.ssub_category);

        ssub_category_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        ssub_category.setAdapter(ssub_category_adapter);

        main_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(sub_category_adapter.getCount() != 0) {
                    sub_category_adapter.clear();
                }

                    sub_category_adapter.add("쌀");
                    sub_category_adapter.add("보리");
                    sub_category_adapter.add("토마토");
                    sub_category_adapter.add("상추");

            }
        });

        sub_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(ssub_category_adapter.getCount() != 0) {
                    ssub_category_adapter.clear();
                }

                    ssub_category_adapter.add("국내산");
                    ssub_category_adapter.add("수입산");

            }
        });

        ssub_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        main_category_adapter.add("곡식류");
        main_category_adapter.add("채소류");


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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
