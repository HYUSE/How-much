package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultFragment extends Fragment {
    private LineChart chart;
    private ArrayList<String> x_values;
    private String result;
    private ArrayList<LineDataSet> retail_data_set_list;
    private ArrayList<LineDataSet> wholesale_data_set_list;
    private String unit_r;
    private String unit_w;
    private PostJSON post_json;
    private Button button_wholesale;
    private Button button_retail;

    public ResultFragment() {
        retail_data_set_list = new ArrayList<LineDataSet>();
        wholesale_data_set_list = new ArrayList<LineDataSet>();
        post_json = new PostJSON();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        Bundle bundle = this.getArguments();
        String name = bundle.getString("name", "NULL");
        String sub_id = bundle.getString("sub_id","NULL");

        TextView product_textview = (TextView) view.findViewById(R.id.product);
        product_textview.setText(name);

        chart = (LineChart) view.findViewById(R.id.chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(8f);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new MyLabelFormatter());
        leftAxis.setTextSize(8f);

        x_values = new ArrayList<String>();

        button_wholesale = (Button) view.findViewById(R.id.button_wholesale);
        button_retail = (Button) view.findViewById(R.id.button_retail);

        button_wholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawWholesale();
            }
        });

        button_retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawRetail();
            }
        });

        try {
            JSONObject obj = new JSONObject();
            JSONObject data = new JSONObject();

            data.accumulate("sub_id", sub_id);
            data.accumulate("region_si","서울");

            obj.accumulate("type","result");
            obj.accumulate("data",data);

            post_json.sendJson(obj.toString());
            result = post_json.returnResult();

            while(!result.isEmpty()) {
                wholesale();
                retail();
                break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(wholesale_data_set_list.size() == 0)
            button_wholesale.setVisibility(View.GONE);
        if(retail_data_set_list.size() == 0)
            button_retail.setVisibility(View.GONE);

        return view;
    }

    public void retail() {
        try {
            JSONObject object = new JSONObject(result);
            JSONArray data = new JSONArray(object.getString("data"));
            for (int i = 0; i < data.length(); i++) {
                JSONObject inside_object = data.getJSONObject(i);
                JSONArray price = new JSONArray(inside_object.getString("price"));

                ArrayList<Entry> retail_list = new ArrayList<Entry>();
                for (int j = 0; j < price.length(); j++) {
                    JSONObject price_object = price.getJSONObject(j);

                    if(price_object.getString("price_r").equals("null")) {
                        continue;
                    }


                    Entry retail = new Entry(new Float(price_object.getString("price_r")).floatValue() ,j);

                    retail_list.add(retail);
                    unit_r = price_object.getString("unit_r");
                    if(x_values.size() != 5) {
                        x_values.add(price_object.getString("date"));
                    }
                }

                LineDataSet setComp = new LineDataSet(retail_list, inside_object.getString("grade"));
                setComp.setValueFormatter(new MyLabelFormatter());
                if(retail_list.size() > 0)
                    retail_data_set_list.add(setComp);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void wholesale() {
        try {
            JSONObject object = new JSONObject(result);
            JSONArray data = new JSONArray(object.getString("data"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject inside_object = data.getJSONObject(i);
                JSONArray price = new JSONArray(inside_object.getString("price"));

                ArrayList<Entry> wholesale_list = new ArrayList<Entry>();
                for (int j = 0; j < price.length(); j++) {
                    JSONObject price_object = price.getJSONObject(j);

                    if(price_object.getString("price_w").equals("null")) {
                        continue;
                    }


                    Entry wholesale = new Entry(new Float(price_object.getString("price_w")).floatValue() ,j);

                    wholesale_list.add(wholesale);
                    unit_w = price_object.getString("unit_w");

                    if(x_values.size() != 5)
                        x_values.add(price_object.getString("date"));
                }

                LineDataSet setComp = new LineDataSet(wholesale_list, inside_object.getString("grade"));
                setComp.setValueFormatter(new MyLabelFormatter());
                if(wholesale_list.size() > 0)
                    wholesale_data_set_list.add(setComp);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void drawRetail() {
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        if(retail_data_set_list.size() == 0)
            button_retail.setVisibility(View.GONE);
        else
            button_retail.setVisibility(View.VISIBLE);

        for (int i = 0; i < retail_data_set_list.size(); i++) {
            dataSets.add(retail_data_set_list.get(i));
        }

        LineData data = new LineData(x_values, dataSets);
        data.setValueFormatter(new MyLabelFormatter());

        chart.setDescription("단위 : " + unit_r);
        chart.setData(data);
        chart.invalidate();
    }

    public void drawWholesale() {
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        if(wholesale_data_set_list.size() == 0)
            button_wholesale.setVisibility(View.GONE);
        else
            button_wholesale.setVisibility(View.VISIBLE);

        for (int i = 0; i < wholesale_data_set_list.size(); i++) {
            dataSets.add(wholesale_data_set_list.get(i));
        }


        LineData data = new LineData(x_values, dataSets);
        data.setValueFormatter(new MyLabelFormatter());

        chart.setDescription("단위 : " + unit_w);
        chart.setData(data);
        chart.invalidate();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
