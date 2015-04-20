package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class ResultFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private LineChart chart;
    private ArrayList<String> xVals;

    public ResultFragment() {
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
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        chart = (LineChart) view.findViewById(R.id.chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        xVals = new ArrayList<String>();
        xVals.add("1.Q"); xVals.add("2.Q"); xVals.add("3.Q"); xVals.add("4.Q");


        Button button_wholesale = (Button) view.findViewById(R.id.button_wholesale);
        Button button_retail = (Button) view.findViewById(R.id.button_retail);

        button_wholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Entry> valsComp1 = new ArrayList<Entry>();

                Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
                valsComp1.add(c1e1);
                Entry c1e2 = new Entry(50.000f, 1); // 1 == quarter 2 ...
                valsComp1.add(c1e2);


                LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");

                ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                dataSets.add(setComp1);

                LineData data = new LineData(xVals, dataSets);
                chart.setDescription("");
                chart.setData(data);
                chart.invalidate(); // refresh

            }
        });

        button_retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Entry> valsComp2 = new ArrayList<Entry>();
                Entry c2e1 = new Entry(120.000f, 0); // 0 == quarter 1
                valsComp2.add(c2e1);
                Entry c2e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
                valsComp2.add(c2e2);
                LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");
                ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                dataSets.add(setComp2);

                LineData data = new LineData(xVals, dataSets);
                chart.setDescription("");
                chart.setData(data);
                chart.invalidate(); // refresh

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
