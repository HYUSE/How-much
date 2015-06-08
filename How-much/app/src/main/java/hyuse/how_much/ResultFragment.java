package hyuse.how_much;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
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
    private TextView price_name;
    private TextView current_price;
    private DBOpenHelper db;
    private CheckBox button_preference;
    private Spinner spinner_do;
    private Spinner spinner_si;

    /* Kyojun Hwang  code */
    public ResultFragment() {
        db = new DBOpenHelper(getActivity());
        retail_data_set_list = new ArrayList<LineDataSet>();
        wholesale_data_set_list = new ArrayList<LineDataSet>();
        post_json = new PostJSON();
        post_json.setType("result");
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
        price_name = (TextView) view.findViewById(R.id.price_name);
        current_price = (TextView) view.findViewById(R.id.current_price);

        Bundle bundle = this.getArguments();
        final String name = bundle.getString("name", "NULL");
        final String sub_id = bundle.getString("sub_id","NULL");

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
        button_preference = (CheckBox) view.findViewById(R.id.button_preference);
        spinner_do = (Spinner) view.findViewById(R.id.spinner_do);
        spinner_si = (Spinner) view.findViewById(R.id.spinner_si);

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
        button_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {db.insertPreference(sub_id, name);
            }
        });

        ArrayAdapter<CharSequence> do_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.do_list, android.R.layout.simple_spinner_item);
        do_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_do.setAdapter(do_adapter);
        spinner_do.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                (String)adapterView.getItemAtPosition(position)
            }
        });

        ArrayAdapter<CharSequence> si_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.si_list, android.R.layout.simple_spinner_item);
        si_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_si.setAdapter(si_adapter);
        spinner_si.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });

        try {
            post_json.setType("result");
            post_json.send(sub_id,"서울");
            result = post_json.returnResult();
            while(result == null) { result = post_json.returnResult(); }

            retail();
            wholesale();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(wholesale_data_set_list.size() == 0)
            button_wholesale.setVisibility(View.GONE);
        if(retail_data_set_list.size() == 0)
            button_retail.setVisibility(View.GONE);

        db.insertSearch(sub_id, name);

        if(db.checkPreference(sub_id))
            button_preference.setChecked(true);

        return view;
    }

    public void retail() {
        try {
            JSONObject object = new JSONObject(result);
            JSONArray data = new JSONArray(object.getString("data"));
            int average = 0;
            int num_of_item = 0;
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

                    if(j == price.length() - 1) {
                        average += new Integer(price_object.getString("price_r")).intValue();
                        num_of_item += 1;
                    }
                }

                LineDataSet setComp = new LineDataSet(retail_list, inside_object.getString("grade"));
                setComp.setValueFormatter(new MyLabelFormatter());
                if(retail_list.size() > 0)
                    retail_data_set_list.add(setComp);
            }

            if(num_of_item != 0) {
                price_name.setText("소매가 : ");
                current_price.setText(current_price.getText() + "" + average / num_of_item + " / " + unit_r);
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
            int average = 0;
            int num_of_item = 0;
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

                    if(j == price.length() - 1) {
                        average += new Integer(price_object.getString("price_w")).intValue();
                        num_of_item += 1;
                    }
                }

                LineDataSet setComp = new LineDataSet(wholesale_list, inside_object.getString("grade"));
                setComp.setValueFormatter(new MyLabelFormatter());
                if(wholesale_list.size() > 0)
                    wholesale_data_set_list.add(setComp);
            }

            if(num_of_item != 0) {
                price_name.setText(price_name.getText() + "\n도매가 : ");
                current_price.setText(current_price.getText() + "\n" + average / num_of_item + " / " + unit_w);
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
    /* Kyojun Hwang  code end */
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
