package com.example.gr33nsn4ck.checkhistorymotorcycle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gr33nsn4ck.checkhistorymotorcycle.R;

public class CustomAdapterListViewMain extends BaseAdapter {

    String[] topics = new String[]{"เพิ่มรายการซ่อม", "ค้นหาจากหมายเลขทะเบียน", "เรียกดูแบบแบ่งหมวดหมู่", "จัดการสมาชิก", "Backup ข้อมูล", "Logout"};
    Context context;
    LayoutInflater inflater;

    public CustomAdapterListViewMain(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return topics.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_listview_main, null);
            holder.tv_topics = (TextView) convertView.findViewById(R.id.tv_topic);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.tv_topics.setText(topics[position]);

        return convertView;
    }

    private class ViewHolder {
        TextView tv_topics;
    }

}
