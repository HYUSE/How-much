package hyuse.how_much;
import java.util.ArrayList;

import android.content.Context;
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

                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "리스트 클릭 : "+m_List.get(pos), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return convertView;
    }
    public boolean add(Home_data item) {
        boolean chk = true;
        for(int i=0;i<getCount();i++){
            Home_data t = m_List.get(i);
            if(t.KEY != item.KEY){
                continue;
            }
            else{
                if(Integer.parseInt(t.value) > Integer.parseInt(item.value)){
                    m_List.remove(i);
                    chk = false;
                }
            }

        }
        m_List.add(item);
        return chk;
    }
    public void remove(int _position) {
        m_List.remove(_position);
    }
    public void reset(){m_List = new ArrayList<Home_data>();}
    /* Eunjae Code END */
}
