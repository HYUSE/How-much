package hyuse.how_much;
import java.util.ArrayList;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HomeList extends BaseAdapter {
    /* Eunjae Lee Code */
    private ArrayList<Home_data>   m_List;

    public HomeList() {
        m_List = new ArrayList<Home_data>();
    }
    public int getCount() {
        return m_List.size();
    }
    public Object getItem(int position) {
        return m_List.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_item, parent, false);
            TextView name = (TextView) convertView.findViewById(R.id.item_name);
            name.setText(m_List.get(position).name);
            TextView value = (TextView) convertView.findViewById(R.id.item_value);
            value.setText(m_List.get(position).value);
            TextView checker = (TextView) convertView.findViewById(R.id.checker);
            if(!m_List.get(position).checker) {
                checker.setText("");
            }

            convertView.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Toast.makeText(context, "바로 가기! "+m_List.get(pos).name, Toast.LENGTH_SHORT).show();
                    ResultFragment newFragment = new ResultFragment();

                    // replace fragment
                    FragmentTransaction transaction = ((MainActivity)context).getFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putString("name", m_List.get(pos).name);
                    bundle.putString("sub_id", m_List.get(pos).KEY);
                    newFragment.setArguments(bundle);

                    transaction.replace(R.id.fragment_layout, newFragment);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();
                    FragmentManager fm = ((MainActivity)context).getFragmentManager();                }
            });
        }

        return convertView;
    }
    public void add(Home_data item) {
        m_List.add(item);
    }
    public boolean key_chk(Home_data item){
        for(int i=0;i<m_List.size();i++){
            if(m_List.get(i).KEY == item.KEY){
                if(Integer.parseInt(m_List.get(i).value) > Integer.parseInt(item.value)){
                    m_List.set(i,new Home_data(item.KEY,m_List.get(i).name,item.value,item.checker));
                    return true;
                }
            }
        }
        return false;
    }
    public void remove(int _position) {
        m_List.remove(_position);
    }
    public void reset(){m_List = new ArrayList<Home_data>();}
    /* Eunjae Code END */
}
