package com.example.gr33nsn4ck.checkhistorymotorcycle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gr33nsn4ck.checkhistorymotorcycle.CreateDetailRepairActivity;
import com.example.gr33nsn4ck.checkhistorymotorcycle.R;
import com.example.gr33nsn4ck.checkhistorymotorcycle.ShowCustomerInformationActivity;
import com.example.gr33nsn4ck.checkhistorymotorcycle.ShowMotorcycleInformationActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class CustomAdapterListViewShowMotorcyclesList extends BaseAdapter {


    LayoutInflater inflater;
    Context context;

    private ArrayList<Integer> motorcycle_id = new ArrayList<Integer>();
    private ArrayList<String> brand = new ArrayList<String>();
    private ArrayList<String> color = new ArrayList<String>();
    private ArrayList<String> license_plate = new ArrayList<String>();
    private ArrayList<String> owner_id = new ArrayList<String>();
    private ArrayList<String> picture1_name = new ArrayList<String>();

    public CustomAdapterListViewShowMotorcyclesList(Context context, ArrayList<Integer> _id, ArrayList<String> brand, ArrayList<String> color, ArrayList<String> license_plate, ArrayList<String> owner_id, ArrayList<String> picture1_name) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.motorcycle_id = _id;
        this.brand = brand;
        this.color = color;
        this.license_plate = license_plate;
        this.owner_id = owner_id;
        this.picture1_name = picture1_name;
    }

    @Override
    public int getCount() {
        return motorcycle_id.size();
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
            convertView = inflater.inflate(R.layout.item_listview_showmotorcycles, null);
            holder.leftImageView = (ImageView) convertView.findViewById(R.id.leftImageView);
            holder.tv_details = (TextView) convertView.findViewById(R.id.tv_details);
            holder.btn_add = (TextView) convertView.findViewById(R.id.btn_add);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        // Load Image
        File img = new File(Environment.getExternalStorageDirectory()+"/motorcycles_pictures/"+picture1_name.get(position) + ".png");
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(img));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        holder.leftImageView.setImageBitmap(bitmap);
        holder.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowMotorcycleInformationActivity.class);
                intent.putExtra("motorcycle_id", String.valueOf(motorcycle_id.get(position)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.tv_details.setText("รุ่น : " + brand.get(position) + "\n" +
                "สี : " + color.get(position) + "\n" +
                "หมายเลขทะเบียน" + license_plate.get(position) + "\n");

        holder.tv_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowMotorcycleInformationActivity.class);
                intent.putExtra("motorcycle_id", String.valueOf(motorcycle_id.get(position)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateDetailRepairActivity.class);
                intent.putExtra("owner_id", owner_id.get(position));
                intent.putExtra("motorcycle_id", motorcycle_id.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                Log.e("........", "motorcycle_id : " + motorcycle_id.get(position));

            }
        });

        return convertView;
    }

    class ViewHolder{
        ImageView leftImageView;
        TextView tv_details;
        TextView btn_add;
    }

}
