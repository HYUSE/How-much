package hyuse.how_much;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import static hyuse.how_much.R.id.fragment_layout;


public class RetrieveFragment extends Fragment {//implements AdapterView.OnItemClickListener {
    private OnFragmentInteractionListener mListener;
    private ListView list;
    private SearchView searchView;
    private PostJSON post_json;

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

        post_json = new PostJSON();

        /* haryeong code */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(final String newText) {
                if(newText.length() == 0) {
                    LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
                    linearLayout.removeAllViews();
                    return false;
                }
                String q;//= newText.substring(0, newText.length()-1);
                q = newText;
                length[0] = newText.length();
                post_json.setType("auto_complete");
                if(post_json.send(q)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("연결 실패");
                    alert.setMessage("인터넷 연결이 실패하였습니다.\n 다시 시도해주십시오.");
                    alert.setNeutralButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                            getActivity().moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
                    alert.show();
                }


                try {
                    String responseString = post_json.returnResult();
                    while (responseString == null) { responseString = post_json.returnResult(); }

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
                                    FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();

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

                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.findViewById(R.id.search_view).getWindowToken(), 0);

                                    transaction.commit();

                                }
                            });
                            //TextView bar = new TextView(getActivity());
                            linearLayout.addView(textView);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

        /* haryeong code end */

        /* Kyojun Hwang code */

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (doubleBackToExitPressedOnce) {
                            getActivity().finish();
                            getActivity().moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
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
        /* Kyojun Hwang code end */
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
