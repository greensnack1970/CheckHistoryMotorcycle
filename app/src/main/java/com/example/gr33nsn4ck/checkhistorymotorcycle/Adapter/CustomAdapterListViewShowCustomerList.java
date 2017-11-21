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
import com.example.gr33nsn4ck.checkhistorymotorcycle.ShowCustomerInformationActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class CustomAdapterListViewShowCustomerList extends BaseAdapter {

    LayoutInflater inflater;
    Context context;

    ArrayList<String> id;
    ArrayList<String> nickname;
    ArrayList<String> sex;
    ArrayList<String> age;
    ArrayList<String> picture_name;

    public CustomAdapterListViewShowCustomerList(Context context, ArrayList<String> id, ArrayList<String> nickname, ArrayList<String> sex, ArrayList<String> age, ArrayList<String> picture_name) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.id = id;
        this.nickname = nickname;
        this.sex = sex;
        this.age = age;
        this.picture_name = picture_name;
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
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_listview_showcustomer, null);
            holder.left_imageview = (ImageView) convertView.findViewById(R.id.left_imageview);
            holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.tv_sex = (TextView) convertView.findViewById(R.id.tv_sex);
            holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        // Load Image
        File img = new File(Environment.getExternalStorageDirectory()+"/customers_pictures/"+picture_name.get(position) + ".png");
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(img));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        holder.left_imageview.setImageBitmap(bitmap);
        holder.left_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowCustomerInformationActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.tv_nickname.setText(nickname.get(position));
        holder.tv_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowCustomerInformationActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        switch (sex.get(position)){
            case "m":holder.tv_sex.setText("Male");break;
            case "f":holder.tv_sex.setText("Female");break;
        }
        holder.tv_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowCustomerInformationActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.tv_age.setText(age.get(position));
        holder.tv_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowCustomerInformationActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView tv_nickname;
        TextView tv_sex;
        TextView tv_age;
        ImageView left_imageview;
    }

}
