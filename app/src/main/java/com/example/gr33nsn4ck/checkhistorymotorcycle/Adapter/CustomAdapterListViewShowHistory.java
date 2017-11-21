package com.example.gr33nsn4ck.checkhistorymotorcycle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gr33nsn4ck.checkhistorymotorcycle.R;
import com.example.gr33nsn4ck.checkhistorymotorcycle.ShowDetailOfRepair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class CustomAdapterListViewShowHistory extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<String> id;
    ArrayList<String> motorcycle_band;
    ArrayList<String> customer_name;
    ArrayList<String> repair_state;
    ArrayList<String> img1;

    public CustomAdapterListViewShowHistory(Context context, ArrayList<String> id,
                                            ArrayList<String> motorcycle_band,
                                            ArrayList<String> customer_name,
                                            ArrayList<String> repair_state,
                                            ArrayList<String> img1) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.id = id;
        this.motorcycle_band = motorcycle_band;
        this.customer_name = customer_name;
        this.repair_state = repair_state;
        this.img1 = img1;
    }

    @Override
    public int getCount() {
        return id.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_history, null);
            holder = new ViewHolder();

            holder.txt_motorcycle_brand = (TextView) convertView.findViewById(R.id.txt_motorcycle_brand);
            holder.txt_customer_name = (TextView) convertView.findViewById(R.id.txt_customer_name);
            holder.txt_state = (TextView) convertView.findViewById(R.id.txt_state);

            holder.leftImageView = (ImageView) convertView.findViewById(R.id.leftImageView);
            holder.leftImageView = (ImageView) convertView.findViewById(R.id.leftImageView);
            holder.leftImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowDetailOfRepair.class);
                    intent.putExtra("id", id.get(position));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_motorcycle_brand.setText(motorcycle_band.get(position));
        holder.txt_motorcycle_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowDetailOfRepair.class);
                intent.putExtra("id", id.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.txt_customer_name.setText(customer_name.get(position));
        holder.txt_state.setText(repair_state.get(position));

        File img = new File(Environment.getExternalStorageDirectory()+"/history_pictures/"+img1.get(position) + ".png");
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(img));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        holder.leftImageView.setImageBitmap(bitmap);

        return convertView;
    }
    private class ViewHolder {
        ImageView leftImageView;
        TextView txt_motorcycle_brand;
        TextView txt_customer_name;
        TextView txt_state;
    }
}
