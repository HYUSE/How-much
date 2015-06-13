package com.example.hwang_gyojun.hyu_se;
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
    private ArrayList<home_data>   m_List;

    public HomeList() {
        m_List = new ArrayList<home_data>();
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
            TextView cheker = (TextView) convertView.findViewById(R.id.checker);
            if(!m_List.get(position).checker) {
                cheker.setText("");
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
    public void add(home_data item) {
        m_List.add(item);
    }
    public void remove(int _position) {
        m_List.remove(_position);
    }

}



