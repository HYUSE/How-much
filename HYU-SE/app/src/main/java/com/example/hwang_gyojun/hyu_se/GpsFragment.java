package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class GpsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    public GetGPS GPS;
    public TextView GPS_info;
    Spinner si_spinner;
    Object do_name;
    Object si_name;
    ArrayAdapter<CharSequence> do_adapter;
    ArrayAdapter<CharSequence> si_adapter;

    public GpsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_gps, container, false);

        Spinner do_spinner = (Spinner) view.findViewById(R.id.do_spinner);
        si_spinner = (Spinner) view.findViewById(R.id.si_spinner);
        Button auto_find = (Button) view.findViewById(R.id.button_confirm);
        GPS_info = (TextView) view.findViewById(R.id.GPS_info);

        GPS = new GetGPS(this.getActivity());

        auto_find.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String addr = GPS.get_location();
                GPS_info.setText(addr);
            }
        });

        do_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.do_list,    android.R.layout.simple_spinner_item);
        do_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        do_spinner.setAdapter(do_adapter);
        do_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                do_name = parent.getSelectedItem();
                switch ((int)id){
                    case 0:
                        si_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.si_02,
                                android.R.layout.simple_spinner_item);
                        break;
                    case 1:
                        si_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.si_031,
                                android.R.layout.simple_spinner_item);
                        break;
                    case 2:
                        si_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.si_033,
                                android.R.layout.simple_spinner_item);
                        break;
                    case 3:
                        si_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.si_041,
                                android.R.layout.simple_spinner_item);
                        break;
                    case 4:
                        si_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.si_063,
                                android.R.layout.simple_spinner_item);
                        break;
                    case 5:
                        si_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.si_055,
                                android.R.layout.simple_spinner_item);
                        break;

                    default:
                }
                si_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                si_spinner.setAdapter(si_adapter);

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        si_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                si_name = parent.getSelectedItem();
                GPS_info.setText(do_name+" "+si_name);
            }
            @Override
             public void onNothingSelected(AdapterView<?> parent) {

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
