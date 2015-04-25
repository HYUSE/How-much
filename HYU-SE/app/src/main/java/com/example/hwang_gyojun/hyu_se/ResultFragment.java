package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
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
    private OnFragmentInteractionListener mListener;
    private LineChart chart;
    private ArrayList<String> xVals;
    private String mStrJson;
    private ArrayList<Entry> valsComp1;
    private ArrayList<Entry> valsComp2;

    public ResultFragment() {
        // Required empty public constructor
        mStrJson =
                "{\n" +
                        "    \"type\":\"result_screen\",\n" +
                        "    \"data\":[\n" +
                        "\t{\n" +
                        "\t    \"price_r\":\"1000\",\n" +
                        "\t    \"price_w\":\"150\",\n" +
                        "\t    \"unit\":\"1kg\",\n" +
                        "\t    \"price_date\":\"20150404\"\n" +
                        "\t},\n" +
                        "\t{\n" +
                        "\t    \"price_r\":\"1500\",\n" +
                        "\t    \"price_w\":\"100\",\n" +
                        "\t    \"unit\":\"1kg\",\n" +
                        "\t    \"price_date\":\"20150405\"\n" +
                        "\t}\n" +
                        "    ]\n" +
                        "}";
        valsComp1 = new ArrayList<Entry>();
        valsComp2 = new ArrayList<Entry>();
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
        String product_name = bundle.getString("product", "NULL");

        TextView procut_textview = (TextView) view.findViewById(R.id.product);
        procut_textview.setText(product_name);

        chart = (LineChart) view.findViewById(R.id.chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new MyLabelFormatter());

        xVals = new ArrayList<String>();

        Button button_wholesale = (Button) view.findViewById(R.id.button_wholesale);
        Button button_retail = (Button) view.findViewById(R.id.button_retail);

        button_wholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineDataSet setComp1 = new LineDataSet(valsComp1, "도매");
                setComp1.setValueFormatter(new MyLabelFormatter());
                ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                dataSets.add(setComp1);

                LineData data = new LineData(xVals, dataSets);
                data.setValueFormatter(new MyLabelFormatter());
                chart.setData(data);
                chart.invalidate(); // refresh


            }
        });

        button_retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineDataSet setComp2 = new LineDataSet(valsComp2, "소매");
                setComp2.setValueFormatter(new MyLabelFormatter());
                ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                dataSets.add(setComp2);

                LineData data = new LineData(xVals, dataSets);
                data.setValueFormatter(new MyLabelFormatter());
                chart.setData(data);
                chart.invalidate(); // refresh

            }
        });

        try {
            JSONObject object = new JSONObject(mStrJson);
            JSONArray data = new JSONArray(object.getString("data"));

            if(object.getString("type").equals("result_screen"))
                Log.d("TYPE","RESULT_SCREEN");

            for (int i = 0; i < data.length(); i++) {
                JSONObject insideObject = data.getJSONObject(i);

                Entry retail = new Entry(new Float(insideObject.getString("price_r")).floatValue() ,i);
                Entry wholesale = new Entry(new Float(insideObject.getString("price_w")).floatValue(), i);

                valsComp1.add(wholesale);
                valsComp2.add(retail);

                chart.setDescription("단위 : " + insideObject.getString("unit"));
                xVals.add(insideObject.getString("price_date"));
            }

        } catch (JSONException e) {
            Log.d("tag", "Parse Error");
        }


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
