package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ShowMotorcycleInformationActivity extends AppCompatActivity {

    TextView tv_brand;
    TextView tv_color;
    TextView tv_license_plate;
    TextView tv_category;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;

    String str_motorcycle_id;
    String str_brand;
    String str_color;
    String str_license_plate;
    String str_category;
    String str_picture1_name;
    String str_picture2_name;
    String str_picture3_name;
    String str_picture4_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_motorcycle_information);
        bindWidgets();

        str_motorcycle_id = getIntent().getStringExtra("motorcycle_id");

        getInformationFromMotorcycleID(str_motorcycle_id);
        try {
            setInformationToWidgets();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void bindWidgets() {
        tv_brand = (TextView) findViewById(R.id.tv_motorcycle_brand);
        tv_color = (TextView) findViewById(R.id.tv_color);
        tv_license_plate = (TextView) findViewById(R.id.tv_license_plate);
        tv_category = (TextView) findViewById(R.id.tv_category);
        imageView1 = (ImageView) findViewById(R.id.image1);
        imageView2 = (ImageView) findViewById(R.id.image2);
        imageView3 = (ImageView) findViewById(R.id.image3);
        imageView4 = (ImageView) findViewById(R.id.image4);
    }

    private void getInformationFromMotorcycleID(String str_motorcycle_id) {
        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getReadableDatabase();

        String[] columns = new String[]{"_id", "brand", "color", "license_plate",
                "category", "picture1_name", "picture2_name", "picture3_name", "picture4_name"};
        String selectwhere = "_id = ?";
        String[] selectarg = new String[]{str_motorcycle_id};

        Cursor cursor = db.query("motorcycles", columns, selectwhere, selectarg, null, null, null);
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                str_brand = cursor.getString(cursor.getColumnIndex("brand"));
                str_color = cursor.getString(cursor.getColumnIndex("color"));
                str_license_plate = cursor.getString(cursor.getColumnIndex("license_plate"));
                str_category = cursor.getString(cursor.getColumnIndex("category"));
                str_picture1_name = cursor.getString(cursor.getColumnIndex("picture1_name"));
                str_picture2_name = cursor.getString(cursor.getColumnIndex("picture2_name"));
                str_picture3_name = cursor.getString(cursor.getColumnIndex("picture3_name"));
                str_picture4_name = cursor.getString(cursor.getColumnIndex("picture4_name"));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }


    private void setInformationToWidgets() throws FileNotFoundException {
        tv_brand.setText(str_brand);
        tv_color.setText(str_color);
        tv_license_plate.setText(str_license_plate);
        tv_category.setText(str_category);

        if(str_picture1_name != null){
            File img = new File(Environment.getExternalStorageDirectory()+"/motorcycles_pictures/"+str_picture1_name+".png");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(img));
            imageView1.setImageBitmap(bitmap);
        }

        if(str_picture2_name != null){
            File img = new File(Environment.getExternalStorageDirectory()+"/motorcycles_pictures/"+str_picture2_name+".png");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(img));
            imageView2.setImageBitmap(bitmap);
        }

        if(str_picture3_name != null){
            File img = new File(Environment.getExternalStorageDirectory()+"/motorcycles_pictures/"+str_picture3_name+".png");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(img));
            imageView3.setImageBitmap(bitmap);
        }

        if(str_picture4_name != null){
            File img = new File(Environment.getExternalStorageDirectory()+"/motorcycles_pictures/"+str_picture4_name+".png");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(img));
            imageView4.setImageBitmap(bitmap);
        }

    }

}
