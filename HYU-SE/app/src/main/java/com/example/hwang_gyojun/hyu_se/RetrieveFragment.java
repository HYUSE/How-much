package com.example.hwang_gyojun.hyu_se;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class RetrieveFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private SearchView searchView;

    public RetrieveFragment() {
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
        final int[] length = {0};
        final View view = inflater.inflate(R.layout.fragment_retrieve, container, false);
        searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setIconified(false);

        /* haryeong code */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(final String newText) {
                //InputMethodManager imm= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                if(newText.length() == 0) {
                    LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
                    linearLayout.removeAllViews();
                    return false;
                }
                String q;//= newText.substring(0, newText.length()-1);
                q = newText;
                Log.i("newText", newText);
                Log.i("q", q);
                length[0] = newText.length();
                String responseString;
                responseString = request(q);

                try {
                    LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length() != 0) {
                        linearLayout.removeAllViews();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            c.getString("name");
                            TextView textView = new TextView(getActivity());
                            textView.setText(c.getString("name") + "("+c.getString("sub_id")+")");
                            textView.setHeight(70);
                            textView.setWidth(500);
                            TextView bar = new TextView(getActivity());
                            linearLayout.addView(textView);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("responseString : ", responseString);

                return false;
            }
        });

        /* haryeong code end */

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

    /* haryeong code */
    public String request(final String q){
        final int[] end = {1};
        final String[] responseString = new String[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                String urlString = "http://bear.3dwise.com:8000/price/auto_complete?data="+q.replace(" ", "");
                HttpResponse httpResponse = null;
                try
                {
                    URI url = new URI(urlString);

                    HttpGet httpGet = new HttpGet(url);

                    httpResponse = httpClient.execute(httpGet);
                    responseString[0] = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                    end[0] = 0;
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while(end[0] == 1){}
        return responseString[0];
    }
/*haryeong code end*/
}
