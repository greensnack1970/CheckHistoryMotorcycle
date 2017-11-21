package com.example.gr33nsn4ck.checkhistorymotorcycle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.gr33nsn4ck.checkhistorymotorcycle.R;

public class StaticGridViewAdapter extends BaseAdapter {

    int[] images;
    String[] topics;
    Context context;
    LayoutInflater inflater;

    public StaticGridViewAdapter(Context context, String[] topics, int[] images) {
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
            holder.topImageView = (ImageView) convertView.findViewById(R.id.leftImageView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        switch (position){
            case 0:
                holder.topImageView.setImageResource(R.drawable.btn_classic);
                break;
            case 1:
                holder.topImageView.setImageResource(R.drawable.btn_enduro);
                break;
            case 2:
                holder.topImageView.setImageResource(R.drawable.btn_bigbike);
                break;
            case 3:
                holder.topImageView.setImageResource(R.drawable.btn_other);
                break;
        }


        return convertView;
    }

    private class ViewHolder {
        ImageView topImageView;
    }

}
