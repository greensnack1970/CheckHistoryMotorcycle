package com.example.gr33nsn4ck.checkhistorymotorcycle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.gr33nsn4ck.checkhistorymotorcycle.R;


public class CustomAdapterGridviewMain extends BaseAdapter {

    int[] images;
    String[] topics;

    Context context;
    LayoutInflater inflater;

    public CustomAdapterGridviewMain(Context context, String[] topics, int[] images) {
        this.context = context;
        this.topics = topics;
        this.images = images;
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

        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.gridview_static, null);
            holder.leftImageView = (ImageView) convertView.findViewById(R.id.leftImageView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.leftImageView.setImageResource(R.mipmap.ic_launcher);

        return null;
    }

    private class ViewHolder {
        ImageView leftImageView;
    }
}
