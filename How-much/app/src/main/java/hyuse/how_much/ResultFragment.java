package hyuse.how_much;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;

import android.content.DialogInterface;

import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.LinearLayout;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ResultFragment extends Fragment {
    private LineChart chart;
    private ArrayList<String> x_values;
    private String result;
    private String[] name;
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

    private LinearLayout retail;
    private LinearLayout wholesale;
    private TextView wholesale_price;
    private TextView retail_price;
    private TextView wholesale_unit;
    private TextView retail_unit;
    private TextView product_textview;

    private Spinner spinner_do;
    private Spinner spinner_si;
    private ArrayAdapter<CharSequence> do_adapter;
    private ArrayAdapter<CharSequence> si_adapter;
    private boolean check;
    private String sub_id;

    /* Kyojun Hwang  code */
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


        db = new DBOpenHelper(getActivity());
        post_json = new PostJSON();
        post_json.setType("result");
        check = false;

        retail = (LinearLayout) view.findViewById(R.id.retail);
        wholesale = (LinearLayout) view.findViewById(R.id.wholesale);
        wholesale_price = (TextView) view.findViewById(R.id.wholesale_price);
        retail_price = (TextView) view.findViewById(R.id.retail_price);
        retail_unit = (TextView) view.findViewById(R.id.retail_unit);
        wholesale_unit = (TextView) view.findViewById(R.id.wholesale_unit);

        Bundle bundle = this.getArguments();
        sub_id = bundle.getString("sub_id","NULL");

        product_textview = (TextView) view.findViewById(R.id.product);

        chart = (LineChart) view.findViewById(R.id.chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(8f);
        xAxis.setAvoidFirstLastClipping(true);

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

        wholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wholesale.setBackground(getResources().getDrawable(R.drawable.button_selected));
                retail.setBackground(getResources().getDrawable(R.drawable.button_normal));
                drawWholesale();
            }
        });
        retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wholesale.setBackground(getResources().getDrawable(R.drawable.button_normal));
                retail.setBackground(getResources().getDrawable(R.drawable.button_selected));
                drawRetail();
            }
        });

        button_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.insertPreference(sub_id, (name[1] + "-" + name[2] + "-"));
            }
        });

        do_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.do_list, android.R.layout.simple_spinner_item);
        do_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_do.setAdapter(do_adapter);
        spinner_do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                setSiAdapter(position);
                si_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_si.setAdapter(si_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] region = db.selectRegion().split(" ");
        String[] do_list = getResources().getStringArray(R.array.do_list);

        int index = 0;
        for(int i = 0; i < do_list.length; i++) {
            if(do_list[i].equals(region[0])) {
                index = i;
                break;
            }
        }

        spinner_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    String region = (String) adapterView.getItemAtPosition(position);

                    post_json.setType("result");
                    if(post_json.send(sub_id, region)) {
                        disconnectInternet();
                        return;
                    }
                    result = post_json.returnResult();
                    while (result == null) {
                        result = post_json.returnResult();
                    }

                    retail();
                    wholesale();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(wholesale_data_set_list.size() == 0)
                    button_wholesale.setVisibility(View.GONE);
                if(retail_data_set_list.size() == 0)
                    button_retail.setVisibility(View.GONE);

                if(db.checkPreference(sub_id))
                    button_preference.setChecked(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_si.setSelection(index);
        String[] si_list = setSiAdapter(index);

        for(int i = 0; i < si_list.length; i++) {
            if(si_list[i].equals(region[1])) {
                index = i;
                break;
            }
        }
        spinner_do.setSelection(index);

        return view;
    }

    public String[] setSiAdapter(int position) {

        int si_list[] = {  R.array.si_1,R.array.si_2,R.array.si_3,R.array.si_4,R.array.si_5,R.array.si_6,
                R.array.si_7,R.array.si_8,R.array.si_9,R.array.si_10,R.array.si_11,R.array.si_12,
                R.array.si_13,R.array.si_14,R.array.si_15 };
        si_adapter = ArrayAdapter.createFromResource(getActivity(), si_list[(int)position],    android.R.layout.simple_spinner_item);
        return getResources().getStringArray(si_list[(int)position]);
    }

    public void retail() {
        try {

            price_name.setText("");
            current_price.setText("");
            retail_data_set_list = new ArrayList<LineDataSet>();

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
                String[] s = inside_object.getString("grade").trim().split(">");

                if(check == false) {
                    name = s;
                    db.insertSearch(sub_id.trim(), (name[1] + "-" + name[2] + "-"));
                }
                check = true;
                product_textview.setText(s[0] + ">" + s[1] + ">" + s[2]);
                LineDataSet setComp = new LineDataSet(retail_list, inside_object.getString("grade"));
                setComp.setValueFormatter(new MyLabelFormatter());
                if(retail_list.size() > 0)
                    retail_data_set_list.add(setComp);
            }

            if(num_of_item != 0) {
                retail.setVisibility(View.VISIBLE);
                retail_price.setText(NumberFormat.getNumberInstance(Locale.US).format(average / num_of_item));
                //retail_price.setText(average / num_of_item + "");
                retail_unit.setText(unit_r+"");
                price_name.setText("소매가 : ");
                current_price.setText(current_price.getText() + "" + average / num_of_item + " / " + unit_r);
                drawRetail();
            }
            else
                retail.setVisibility(View.GONE);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void wholesale() {
        try {
            wholesale_data_set_list = new ArrayList<LineDataSet>();

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
                String[] s = inside_object.getString("grade").trim().split(">");
                if(check == false) {
                    name = s;
                    db.insertSearch(sub_id.trim(), (name[1] + "-" + name[2] + "-"));
                }
                check = true;
                product_textview.setText(s[0] + ">" + s[1] + ">" + s[2]);

                LineDataSet setComp = new LineDataSet(wholesale_list, inside_object.getString("grade"));
                setComp.setValueFormatter(new MyLabelFormatter());
                if(wholesale_list.size() > 0)
                    wholesale_data_set_list.add(setComp);
            }

            if(num_of_item != 0) {

                wholesale.setVisibility(View.VISIBLE);
                wholesale_price.setText(NumberFormat.getNumberInstance(Locale.US).format(average / num_of_item));
                wholesale_unit.setText(unit_w+"");
                if(price_name.getText().length() == 0) {
                    current_price.setText(average / num_of_item + " / " + unit_w);
                }
                else {
                    price_name.setText(price_name.getText() + "\n도매가 : ");
                    current_price.setText(current_price.getText() + "\n" + average / num_of_item + " / " + unit_w);
                }
            }
            else
                wholesale.setVisibility(View.GONE);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void drawRetail() {
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        wholesale.setBackground(getResources().getDrawable(R.drawable.button_normal));
        retail.setBackground(getResources().getDrawable(R.drawable.button_selected));

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

        wholesale.setBackground(getResources().getDrawable(R.drawable.button_selected));
        retail.setBackground(getResources().getDrawable(R.drawable.button_normal));

        for (int i = 0; i < wholesale_data_set_list.size(); i++) {
            dataSets.add(wholesale_data_set_list.get(i));
        }

        LineData data = new LineData(x_values, dataSets);
        data.setValueFormatter(new MyLabelFormatter());

        chart.setDescription("단위 : " + unit_w);
        chart.setData(data);
        chart.invalidate();
    }

    public void disconnectInternet() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("연결 끊김");
        alert.setMessage("서버와의 연결이 끊겼습니다.\n다시 시도해주십시오.");
        alert.setNeutralButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        alert.show();
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