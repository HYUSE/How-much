package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ResultFragment extends Fragment {
    private LineChart chart;
    private ArrayList<String> xVals;
    private static String mStrJson;
    private ArrayList<Entry> valsComp1;
    private ArrayList<Entry> valsComp2;
    private String unit_r;
    private String unit_w;
    private ArrayList<String> grades;


    public ResultFragment() throws JSONException, IOException {
        valsComp1 = new ArrayList<Entry>();
        valsComp2 = new ArrayList<Entry>();
    }

    public String POST(String url, JSONObject obj){
        InputStream inputStream = null;
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = obj;

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json, HTTP.UTF_8);

            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);


            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                mStrJson = convertInputStreamToString(inputStream);
            else
                mStrJson = "Did not work!";

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("AA",mStrJson);

        try {
            JSONObject object = new JSONObject(mStrJson);
            JSONArray data = new JSONArray(object.getString("data"));

            for (int i = 0; i < data.length(); i++) {
                JSONObject insideObject = data.getJSONObject(i);

                Entry retail = new Entry(new Float(insideObject.getString("price_r")).floatValue() ,i);
                Entry wholesale = new Entry(new Float(insideObject.getString("price_w")).floatValue(), i);

                valsComp1.add(wholesale);
                valsComp2.add(retail);

                unit_r = insideObject.getString("unit_r");
                unit_w = insideObject.getString("unit_w");

                xVals.add(insideObject.getString("price_date"));
                grades.add(insideObject.getString("grade"));
            }
        } catch (JSONException e) {
            Log.d("tag", "Parse Error");
        }

        // 11. return result
        return mStrJson;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream,"utf-8"));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            JSONObject obj = new JSONObject();
            JSONObject data = new JSONObject();
            try {
                data.accumulate("sub_id", "11");
                data.accumulate("region_si","서울");

                obj.accumulate("type","result");
                obj.accumulate("data",data);
            } catch (JSONException e) {
                e.printStackTrace();

            }
            Log.d("BB",obj.toString());
            return POST(urls[0],obj);
        }
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

        TextView product_textview = (TextView) view.findViewById(R.id.product);
        product_textview.setText(product_name);

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
                setComp1.setColor(getResources().getColor(R.color.material_blue_grey_800));
                setComp1.setValueFormatter(new MyLabelFormatter());
                ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                dataSets.add(setComp1);

                LineDataSet setComp2 = new LineDataSet(valsComp2, "소매");
                setComp2.setValueFormatter(new MyLabelFormatter());
                setComp2.setColor(getResources().getColor(R.color.material_deep_teal_500));
                dataSets.add(setComp2);



                LineData data = new LineData(xVals, dataSets);
                data.setValueFormatter(new MyLabelFormatter());

                chart.setDescription("단위 : " + unit_w);
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

                chart.setDescription("단위 : " + unit_r);
                chart.setData(data);
                chart.invalidate(); // refresh
            }
        });

        new HttpAsyncTask().execute("http://tams2.info.tm:8000/price/");



        return view;
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
