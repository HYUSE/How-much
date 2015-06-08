package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
<<<<<<< HEAD
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
=======
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

>>>>>>> 9585fe61db5908631cd92bf80873104f4835f8e9
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

<<<<<<< HEAD
public class HomeFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private ListView list_view;
    private ArrayAdapter<String> array_adapter;
=======
import static com.example.hwang_gyojun.hyu_se.R.id.fragment_layout;

public class HomeFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ListView preference_list;
    private ArrayAdapter<String> list_adapter;
>>>>>>> 9585fe61db5908631cd92bf80873104f4835f8e9

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
=======

>>>>>>> 9585fe61db5908631cd92bf80873104f4835f8e9
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
<<<<<<< HEAD
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
=======
        preference_list = (ListView) view.findViewById(R.id.preference_list);

        // Android에서 제공하는 string 문자열 하나를 출력 가능한 layout으로 어댑터 생성
        list_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);

        // ListView에 어댑터 연결
        preference_list.setAdapter(list_adapter);

        // ListView 아이템 터치 시 이벤트 추가
        preference_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResultFragment newFragment = null;
                newFragment = new ResultFragment();

                // replace fragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("name", list_adapter.getItem(position));
                newFragment.setArguments(bundle);

                transaction.replace(fragment_layout, newFragment);
>>>>>>> 9585fe61db5908631cd92bf80873104f4835f8e9

                // Commit the transaction
                transaction.commit();
            }
        });
<<<<<<< HEAD
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
=======

        // ListView에 아이템 추가
        list_adapter.add("사");
        list_adapter.add("배");
>>>>>>> 9585fe61db5908631cd92bf80873104f4835f8e9

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

<<<<<<< HEAD
    @Override
    public void onClick(View v) {
    }

=======
>>>>>>> 9585fe61db5908631cd92bf80873104f4835f8e9
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
<<<<<<< HEAD
=======
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
>>>>>>> 9585fe61db5908631cd92bf80873104f4835f8e9
    }

}
