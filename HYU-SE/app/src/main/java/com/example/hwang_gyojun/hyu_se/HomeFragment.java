package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
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

import static com.example.hwang_gyojun.hyu_se.R.id.fragment_layout;

public class HomeFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ListView preference_list;
    private HomeList list_adapter;
    //private HomeList homelist = new HomeList("test");

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
        preference_list = (ListView) view.findViewById(R.id.preference_list);

        // Android에서 제공하는 string 문자열 하나를 출력 가능한 layout으로 어댑터 생성
        list_adapter = new HomeList();

        // ListView에 어댑터 연결
        preference_list.setAdapter(list_adapter);

        // ListView에 아이템 추가
        list_adapter.add(new home_data("배","3000"+"원",true) );
        list_adapter.add(new home_data("사과","4000"+"원",false));
        list_adapter.add(new home_data("사과","4000"+"원",false));
        list_adapter.add(new home_data("사과","4000"+"원",false));
        list_adapter.add(new home_data("사과","4000"+"원",false));
        list_adapter.add(new home_data("사과","4000"+"원",false));
        list_adapter.add(new home_data("사과","4000"+"원",false));
        list_adapter.add(new home_data("사과","4000"+"원",false));
        list_adapter.add(new home_data("사과","4000"+"원",false));
        list_adapter.add(new home_data("사과","4000"+"원",false));
        list_adapter.add(new home_data("사과","4000"+"원",false));
        list_adapter.add(new home_data("사과","4000"+"원",false));

        return view;
    }
    private void MakeList() {
        //DB에서 읽어 오기


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
