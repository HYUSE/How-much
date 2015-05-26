package com.example.hwang_gyojun.hyu_se;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by hwang-gyojun on 2015. 5. 10..
 */
public class AutoCompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList<RetrieveItem> result_list;
    private ArrayList<RetrieveItem> result_name_list;
    private PostJSON post_json;
    public AutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public int getCount() {
        return result_list.size();
    }


    public RetrieveItem getItem(int index) {
        return result_list.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    result_list = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults

                    filterResults.values = result_list;
                    filterResults.count = result_list.size();
               //     Log.i(String.valueOf(filterResults.values.getClass().getName()), "hi");
               //     Log.i(result_list.get(0).getName(), "hi");

                    Log.d("ASDFSADFASDF",""+filterResults.values);
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if ( results == null || results.count == 0 )
                    notifyDataSetInvalidated();
                else
                    notifyDataSetChanged();
            }};
        return filter;
    }

    private ArrayList<RetrieveItem> autocomplete(String input) {
        ArrayList<RetrieveItem> result_list = null;
        post_json = new PostJSON();

        JSONObject obj = new JSONObject();

        try {
            obj.accumulate("type","auto_complete");
            obj.accumulate("data",input);

            post_json.sendJson(obj.toString());

            JSONObject object = new JSONObject(post_json.returnResult());
            JSONArray data = object.getJSONArray("data");

            // Extract the Place descriptions from the results
            result_list = new ArrayList<RetrieveItem>(data.length());
            result_name_list = new ArrayList<RetrieveItem>(data.length());
            for (int i = 0; i < data.length(); i++) {
                RetrieveItem item = new RetrieveItem(data.getJSONObject(i).getString("name"),data.getJSONObject(i).getString("sub_id"));
                result_list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result_list;
    }
}