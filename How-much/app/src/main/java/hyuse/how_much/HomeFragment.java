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
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ListView preference_list;
    private HomeList list_adapter;
    private DBOpenHelper db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         db = new DBOpenHelper(getActivity());

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        preference_list = (ListView) view.findViewById(R.id.preference_list);

        list_adapter = new HomeList();
        preference_list.setAdapter(list_adapter);

        if(db.selectRegion().split(" ").length > 1 && (db.selectPreference().length > 0 || db.selectSearch().length > 0)){
            Thread x = new Thread(new Runnable() {
                public void run() {
                    list_adapter.reset();
                    MakeList();
                }
            });
            x.run();
        }

        return view;
    }
    private void MakeList() {
        String[] first = db.selectPreference();
        String[] first_id = first[0].split(",");
        System.out.println("TEST : " + first_id[0]);
        String[] first_name = first[1].split(",");

        String[] second = db.selectSearch();
        String[] second_id = second[0].split(",");
        String[] second_name = second[1].split(",");

        String region = db.selectRegion().split(" ")[1];
        region = region.substring(0,region.length()-1);

        PostJSON sender = new PostJSON();
        sender.setType("home");
        if(first_id.length > 0){
            if(sender.send(makejson(first_id,region))) {
                disconnectInternet();
                return;
            }

            try {
                String result = null;
                while (result == null) {
                    result = sender.returnResult();
                }
                JSONObject json = new JSONObject(result);
                System.out.println(">>>> " + result);
                JSONArray array = json.getJSONArray("data");

                for(int i=0; i<array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    String KEY = obj.getString("sub_id");
                    String name = first_name[i];
                    String price = (obj.getString("price_r") != null) ? obj.getString("price_r") : obj.getString("price_w");
                    price = (price != null) ? price : "0";
                    Home_data data = new Home_data(KEY, name, price, true);
                    list_adapter.add(data);
                }
            } catch (Exception e){

            }
        }
        sender = new PostJSON();
        sender.setType("home");

        if(second_id.length > 0) {
            if (sender.send(makejson(second_id, region))) {
                disconnectInternet();
                return;
            }

            try {
                String result = null;
                while (result == null) {
                    result = sender.returnResult();
                }
                JSONObject json = new JSONObject(result);
                System.out.println(">>>> " + result);
                JSONArray array = json.getJSONArray("data");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String KEY = obj.getString("sub_id");
                    String name = second_name[i];
                    String price = (obj.getString("price_r") != null) ? obj.getString("price_r") : obj.getString("price_w");
                    price = (price != null) ? price : "0";
                    Home_data data = new Home_data(KEY, name, price, false);
                    list_adapter.add(data);
                }
            } catch (Exception e) {

            }
        }
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

    private String makejson(String[] id,String resion_si){
        JSONObject x = new JSONObject();
        JSONObject y = new JSONObject();
        try {
            x.put("type","home");
            y.put("region_si",resion_si);
            JSONArray id_tags = new JSONArray();
            for(int i=0;i<id.length;i++){
                id_tags.put(id[i]);
            }
            y.put("sub_id",id_tags);
            x.put("data",y);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(x.toString());
        return x.toString();
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