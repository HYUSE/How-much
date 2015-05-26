package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.hwang_gyojun.hyu_se.R.id.fragment_layout;


public class SeachIndexFragment extends Fragment {
    private ListView main_category;
    private ArrayAdapter<RetrieveItem> main_adapter;
    private ListView sub_category;
    private ArrayAdapter<RetrieveItem> sub_adapter;
    private ListView ssub_category;
    private ArrayAdapter<RetrieveItem> ssub_adapter;
    private PostJSON post_json;

    private OnFragmentInteractionListener mListener;

    public SeachIndexFragment() {
        post_json = new PostJSON();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /* 2012036774 Haryeong Kim */
    public class RetrieveItemAdapter extends ArrayAdapter<RetrieveItem> {
        private ArrayList<RetrieveItem> items;

        public RetrieveItemAdapter(Context context, int textViewResourceId, ArrayList<RetrieveItem> items) {
            super(context, textViewResourceId, items);
            this.items = items;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (v == null) {

                v = inflater.inflate(R.layout.custom_retrieve_item, null);

            }
            RetrieveItem item = items.get(position);
            if (items != null) {
                TextView tt = (TextView) v.findViewById(R.id.text_view);
                tt.setText(item.getName());

            }
            return v;
        }
    }

    /* 2012036774 Haryeong Kim end */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seach_index, container, false);

        main_category = (ListView) view.findViewById(R.id.main_category);
        sub_category = (ListView) view.findViewById(R.id.sub_category);
        ssub_category = (ListView) view.findViewById(R.id.ssub_category);
        // Android에서 제공하는 string 문자열 하나를 출력 가능한 layout으로 어댑터 생성
        //main_adapter = new ArrayAdapter<RetrieveItem>(getActivity(), android.R.layout.simple_list_item_1);

        // ListView에 어댑터 연결
        //main_category.setAdapter(main_adapter);

        /* 2012036774 Haryeong Kim */
        final ArrayList<RetrieveItem> main_adapter = new ArrayList<RetrieveItem>();
        main_category.setAdapter(new RetrieveItemAdapter(getActivity(), R.layout.custom_retrieve_item, main_adapter));
        final ArrayList<RetrieveItem> sub_adapter = new ArrayList<RetrieveItem>();
        sub_category.setAdapter(new RetrieveItemAdapter(getActivity(), R.layout.custom_retrieve_item, sub_adapter));
        final ArrayList<RetrieveItem> ssub_adapter = new ArrayList<RetrieveItem>();
        ssub_category.setAdapter(new RetrieveItemAdapter(getActivity(), R.layout.custom_retrieve_item, ssub_adapter));
        /* 2012036774 Haryeong Kim end */
        //sub_category = (ListView) view.findViewById(R.id.sub_category);

        // Android에서 제공하는 string 문자열 하나를 출력 가능한 layout으로 어댑터 생성
        //sub_adapter = new ArrayAdapter<RetrieveItem>(getActivity(), android.R.layout.simple_list_item_1);

        // ListView에 어댑터 연결
        //sub_category.setAdapter(sub_adapter);


        //ssub_category = (ListView) view.findViewById(R.id.ssub_category);

        // Android에서 제공하는 string 문자열 하나를 출력 가능한 layout으로 어댑터 생성
        //ssub_adapter = new ArrayAdapter<RetrieveItem>(getActivity(), android.R.layout.simple_list_item_1);

        // ListView에 어댑터 연결
        //ssub_category.setAdapter(ssub_adapter);

        // ListView 아이템 터치 시 이벤트 추가
        main_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(sub_adapter.size() != 0) {
                    sub_adapter.clear();
                }

                RetrieveItem clicked_item = (RetrieveItem) parent.getItemAtPosition(position);

                JSONObject object = null;
                try {
                    object = new JSONObject(getCategory(clicked_item.getSubID(),"main"));

                    JSONArray data = object.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        RetrieveItem item = new RetrieveItem(data.getJSONObject(i).getString("name"),data.getJSONObject(i).getString("main_id"));
                        sub_adapter.add(item);
                    }
                    sub_category.setAdapter(new RetrieveItemAdapter(getActivity(), R.layout.custom_retrieve_item, sub_adapter));
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                sub_adapter.add("AA");
            }
        });

        // ListView 아이템 터치 시 이벤트 추가
        sub_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(ssub_adapter.size() != 0) {
                    ssub_adapter.clear();
                }

                RetrieveItem clicked_item = (RetrieveItem) parent.getItemAtPosition(position);

                JSONObject object = null;
                try {
                    object = new JSONObject(getCategory(clicked_item.getSubID(), "sub"));
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        RetrieveItem item = new RetrieveItem(data.getJSONObject(i).getString("name"),data.getJSONObject(i).getString("sub_id"));
                        ssub_adapter.add(item);
                    }
                    ssub_category.setAdapter(new RetrieveItemAdapter(getActivity(), R.layout.custom_retrieve_item, ssub_adapter));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //              ssub_adapter.add("BB");
            }
        });

        // ListView 아이템 터치 시 이벤트 추가
        ssub_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                // Commit the transaction
                transaction.commit();
            }
        });

//        main_adapter.add("CC");

        JSONObject object = null;
        try {
            object = new JSONObject(getCategory(null, "category"));
            JSONArray data = object.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                RetrieveItem item = new RetrieveItem(data.getJSONObject(i).getString("name"),data.getJSONObject(i).getString("cate_id"));
                main_adapter.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    public String getCategory(String id, String type) {
        String result = "";

        try {
            JSONObject obj = new JSONObject();

            obj.accumulate("type", type);
            if(id != null) {
                obj.accumulate("data", id);
                post_json.sendJson(obj.toString());
                result = post_json.returnResult();
            }
            else {
                post_json.sendJson(obj.toString());
                result = post_json.returnResult();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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
