package hyuse.how_much;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;
import static hyuse.how_much.R.id.fragment_layout;


public class RetrieveFragment extends Fragment {//implements AdapterView.OnItemClickListener {
    private OnFragmentInteractionListener mListener;
    private ListView list;
    private AutoCompleteAdapter adapter;
    private AutoCompleteTextView auto_search;
    private SearchView searchView;

    private boolean doubleBackToExitPressedOnce;


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
                            final JSONObject c = jsonArray.getJSONObject(i);
                            c.getString("name");
                            TextView textView = new TextView(getActivity());
                            textView.setText(c.getString("name") + "("+c.getString("sub_id")+")");
                            textView.setHeight(70);
                            textView.setWidth(500);
                            textView.setTextSize(17);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ResultFragment newFragment = new ResultFragment();

                                    // replace fragment
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                                    Bundle bundle = new Bundle();

                                    try {
                                        bundle.putString("name", c.getString("name"));
                                        bundle.putString("sub_id", c.getString("sub_id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    newFragment.setArguments(bundle);

                                    transaction.replace(fragment_layout, newFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();

                                }
                            });
                            //TextView bar = new TextView(getActivity());
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
/*
        View view = inflater.inflate(R.layout.fragment_retrieve, container, false);
        list = (ListView) view.findViewById(R.id.retrieve_list);

        auto_search = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        adapter =  new AutoCompleteAdapter(getActivity(),android.R.layout.simple_list_item_1);
        auto_search.setAdapter(adapter);
        auto_search.setOnItemClickListener(this);
*/

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (doubleBackToExitPressedOnce) {
                            getActivity().finish();
                            return true;
                        }

                        doubleBackToExitPressedOnce = true;
                        Toast.makeText(getActivity(), "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                doubleBackToExitPressedOnce = false;
                            }
                        }, 2000);

                        return true;
                    }
                }
                return false;
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
/*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RetrieveItem item = (RetrieveItem) parent.getItemAtPosition(position);

        ResultFragment newFragment = new ResultFragment();

        // replace fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("name", item.getName());
        bundle.putString("sub_id", item.getSubID());
        newFragment.setArguments(bundle);

        transaction.replace(fragment_layout, newFragment);

        InputMethodManager imm= (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(auto_search.getWindowToken(), 0);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
*/
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
