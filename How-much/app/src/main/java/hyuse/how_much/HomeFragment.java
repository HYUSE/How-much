package hyuse.how_much;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import static hyuse.how_much.R.id.fragment_layout;

public class HomeFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ListView preference_list;
    private ArrayAdapter<String> list_adapter;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        preference_list = (ListView) view.findViewById(R.id.preference_list);

        // Android에서 제공하는 string 문자열 하나를 출력 가능한 layout으로 어댑터 생성
        list_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);

        // ListView에 어댑터 연결
        preference_list.setAdapter(list_adapter);

        // ListView 아이템 터치 시 이벤트 추가
        preference_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResultFragment newFragment = null;
                newFragment = new ResultFragment();

                // replace fragment
                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("name", list_adapter.getItem(position));
                newFragment.setArguments(bundle);

                transaction.replace(fragment_layout, newFragment);

                // Commit the transaction
                transaction.commit();
            }
        });

        // ListView에 아이템 추가
        list_adapter.add("사");
        list_adapter.add("배");

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
