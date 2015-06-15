package hyuse.how_much;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GpsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    public GetGPS GPS;
    public TextView GPS_info;
    private DBOpenHelper db;
    Spinner si_spinner;
    Object do_name;
    Object si_name;
    int si_list[] = {  R.array.si_1,R.array.si_2,R.array.si_3,R.array.si_4,R.array.si_5,R.array.si_6,
            R.array.si_7,R.array.si_8,R.array.si_9,R.array.si_10,R.array.si_11,R.array.si_12,
            R.array.si_13,R.array.si_14,R.array.si_15 };
    ArrayAdapter<CharSequence> do_adapter;
    ArrayAdapter<CharSequence> si_adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gps, container, false);

        final Spinner do_spinner = (Spinner) view.findViewById(R.id.do_spinner);
        si_spinner = (Spinner) view.findViewById(R.id.si_spinner);
        Button auto_find = (Button) view.findViewById(R.id.button_confirm);
        GPS_info = (TextView) view.findViewById(R.id.GPS_info);

        GPS = new GetGPS(this.getActivity());
        db = new DBOpenHelper(this.getActivity());

        /* Eunjae Lss Code*/
        auto_find.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String addr = GPS.get_location();
                GPS_info.setText(addr);

<<<<<<< HEAD
                String[] region = addr.split(" ");
                //db.insertRegion(region[0], region[1]);
                Toast.makeText(getActivity(), "가장 가까운 위치을 찾습니다.", Toast.LENGTH_LONG).show();
=======
                Toast.makeText(getActivity(), "가장 가까운 위치을 찾습니다.", Toast.LENGTH_LONG);
>>>>>>> 391e44ad2a0c2cd97d43cd3eaa221b11cca20921

                int k=0;
                String[] res = getResources().getStringArray(R.array.location);
                float loc[] = {999,999};
                //거리가 가장 가까운 지역 위치를 가져온다.
                for(int i=0;i<res.length;i++){
                    String[] x = res[i].split("-");
                    String[] y = (x[1]+addr).split(",");

                    float a = (Float.parseFloat(y[0])-Float.parseFloat(y[2]));
                    float b = (Float.parseFloat(y[1])-Float.parseFloat(y[3]));
                    if(Math.pow(loc[0],2)+Math.pow(loc[1],2)>Math.pow(a,2)+Math.pow(b,2)){
                        loc[0] = a;
                        loc[1] = b;
                        k = i;
                    }
                }
                addr = res[k].split("-")[0].replace(":"," ");
<<<<<<< HEAD
                String[] do_list = getResources().getStringArray(R.array.do_list);

                int index1 = 0;
                int index2 = 0;
                for(int i = 0; i < do_list.length; i++) {
                    if(do_list[i].equals(addr.split(" ")[0])) {
                        index1 = i;
                        break;
                    }
                }
                String[] si_data = getResources().getStringArray(si_list[index1]);
                for(int i =0; i<si_data.length;i++){
                    if(si_data[i].equals(addr.split(" ")[0])) {
                        index2 = i;
                        break;
                    }
                }
                do_spinner.setSelection(index1);
                si_spinner.setSelection(index2);
=======

                //db.insertRegion(region[0], region[1]);
                GPS_info.setText(addr);
>>>>>>> 391e44ad2a0c2cd97d43cd3eaa221b11cca20921
            }
        });

        do_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.do_list,    android.R.layout.simple_spinner_item);
        do_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        do_spinner.setAdapter(do_adapter);
        do_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //si_adapter를 재설정.
                do_name = parent.getSelectedItem();

                si_adapter = ArrayAdapter.createFromResource(getActivity(), si_list[(int)id],    android.R.layout.simple_spinner_item);
                si_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                si_spinner.setAdapter(si_adapter);
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        si_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                si_name = parent.getSelectedItem();
                GPS_info.setText(do_name+" "+si_name);
                db.insertRegion((String) do_name, (String) si_name);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        return view;
        /* Eunjae Lss Code END*/
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}