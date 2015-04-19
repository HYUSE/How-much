package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class GpsFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;

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
        View view = inflater.inflate(R.layout.fragment_gps, container, false);
        // Inflate the layout for this fragment
        Spinner do_spinner = (Spinner) view.findViewById(R.id.region_do);//send.xml의 스피너 아이디
        ArrayAdapter<CharSequence> do_adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.region_do_array,
                android.R.layout.simple_spinner_item);
        do_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        do_spinner.setAdapter(do_adapter);

        Spinner si_spinner = (Spinner) view.findViewById(R.id.region_si);//send.xml의 스피너 아이디
        ArrayAdapter<CharSequence> si_adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.region_si_array,
                android.R.layout.simple_spinner_item);
        si_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        si_spinner.setAdapter(si_adapter);

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

    @Override
    public void onClick(View v) {

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