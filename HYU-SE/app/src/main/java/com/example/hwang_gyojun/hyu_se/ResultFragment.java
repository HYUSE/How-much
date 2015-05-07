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
import java.util.concurrent.ExecutionException;

public class ResultFragment extends Fragment {
    private LineChart chart;
    private ArrayList<String> x_values;
    private String result;
    private ArrayList<LineDataSet> retail_data_set_list;
    private ArrayList<LineDataSet> wholesale_data_set_list;
    private String unit_r;
    private String unit_w;

    public ResultFragment() {
        retail_data_set_list = new ArrayList<LineDataSet>();
        wholesale_data_set_list = new ArrayList<LineDataSet>();
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
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Result",result);
        return result;
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
                data.accumulate("sub_id", "2");
                data.accumulate("region_si","서울");

                obj.accumulate("type","result");
                obj.accumulate("data",data);
            }
            catch (JSONException e) {
                e.printStackTrace();

            }

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
        xAxis.setTextSize(8f);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new MyLabelFormatter());
        leftAxis.setTextSize(8f);

        x_values = new ArrayList<String>();

        Button button_wholesale = (Button) view.findViewById(R.id.button_wholesale);
        Button button_retail = (Button) view.findViewById(R.id.button_retail);

        button_wholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                for (int i = 0; i < wholesale_data_set_list.size(); i++) {
                    dataSets.add(wholesale_data_set_list.get(i));
                }


                LineData data = new LineData(x_values, dataSets);
                data.setValueFormatter(new MyLabelFormatter());

                chart.setDescription("단위 : " + unit_w);
                chart.setData(data);
                chart.invalidate(); // refresh
            }
        });

        button_retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                for (int i = 0; i < retail_data_set_list.size(); i++) {
                    dataSets.add(retail_data_set_list.get(i));
                }

                LineData data = new LineData(x_values, dataSets);
                data.setValueFormatter(new MyLabelFormatter());

                chart.setDescription("단위 : " + unit_r);
                chart.setData(data);
                chart.invalidate(); // refresh
            }
        });

        try {
            JSONObject obj = new JSONObject();
            JSONObject data = new JSONObject();

            data.accumulate("sub_id", "11");
            data.accumulate("region_si","서울");

            obj.accumulate("type","result");
            obj.accumulate("data",data);
            result = new PostJSON().returnResult();
            while(!result.isEmpty()) {
                wholesale();
                retail();
                break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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
                    Entry retail = new Entry(new Float(price_object.getString("price_r")).floatValue() ,j);

                    retail_list.add(retail);
                    unit_r = price_object.getString("unit_r");
                    if(x_values.size() != 5) {
                        x_values.add(price_object.getString("date"));
                        Log.d("ASDFASF",price_object.getString("date"));
                    }
                }

                LineDataSet setComp = new LineDataSet(retail_list, inside_object.getString("grade"));
                setComp.setValueFormatter(new MyLabelFormatter());
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
                    Entry wholesale = new Entry(new Float(price_object.getString("price_w")).floatValue() ,j);

                    wholesale_list.add(wholesale);
                    unit_w = price_object.getString("unit_w");

                    if(x_values.size() != 4)
                        x_values.add(price_object.getString("date"));
                }

                LineDataSet setComp = new LineDataSet(wholesale_list, inside_object.getString("grade"));
                setComp.setValueFormatter(new MyLabelFormatter());
                wholesale_data_set_list.add(setComp);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
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
