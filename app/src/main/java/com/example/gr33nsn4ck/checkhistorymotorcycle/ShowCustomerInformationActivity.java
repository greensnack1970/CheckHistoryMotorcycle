package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ShowCustomerInformationActivity extends AppCompatActivity {


    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_imageView;
    private TextView tv_id;
    private TextView tv_name;
    private TextView tv_nickname;
    private TextView tv_address;
    private TextView tv_age;
    private TextView tv_sex;
    private TextView tv_phonenumber;


    private String str_picturename;
    private String str_id;
    private String str_name;
    private String str_nickname;
    private String str_address;
    private String str_age;
    private String str_sex;
    private String str_phonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_customer_information);

        str_id = getIntent().getStringExtra("id");

        bindWidgets();
        getInformation();
        try {
            set2Widgets();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void bindWidgets() {
        img_imageView = (ImageView) findViewById(R.id.imageView);
        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_phonenumber = (TextView) findViewById(R.id.tv_phonenumber);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    private void getInformation() {
        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getReadableDatabase();

        String[] columns = new String[]{"_id", "name", "nickname", "address",
                                        "age", "sex", "phonenumber", "picture_name"};
        String selectwhere = "_id = ?";
        String[] selectarg = new String[]{str_id};

        Cursor cursor = db.query("customers", columns, selectwhere, selectarg, null, null, null);
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                str_id = cursor.getString(cursor.getColumnIndex("_id"));
                str_picturename = cursor.getString(cursor.getColumnIndex("picture_name"));
                str_name = cursor.getString(cursor.getColumnIndex("name"));
                str_nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                str_address = cursor.getString(cursor.getColumnIndex("address"));
                str_age = cursor.getString(cursor.getColumnIndex("age"));
                str_sex = cursor.getString(cursor.getColumnIndex("sex"));
                str_phonenumber = cursor.getString(cursor.getColumnIndex("phonenumber"));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    private void set2Widgets() throws FileNotFoundException {
        tv_id.setText(str_id);
        tv_name.setText(str_name);
        tv_nickname.setText(str_nickname);
        tv_address.setText(str_address);
        tv_age.setText(str_age);
        switch (str_sex){
            case "m":tv_sex.setText("Male");break;
            case "f":tv_sex.setText("Female");break;
        }
        tv_phonenumber.setText(str_phonenumber);

        File img = new File(Environment.getExternalStorageDirectory()+"/customers_pictures/"+str_picturename+".png");
        Log.e("picture name : ", str_picturename);
        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(img));
        img_imageView.setImageBitmap(bitmap);
    }



    public void btn_edit(View view) {
        Intent intent = new Intent(getApplicationContext(), EditCustomerInformationActivity.class);
        intent.putExtra("id", String.valueOf(str_id));
        startActivity(intent);
    }


    public void btn_motorcycles(View view) {
        Intent intent = new Intent(getApplicationContext(), ShowMotrcyclesListActivity.class);
        intent.putExtra("id", String.valueOf(str_id));
        intent.putExtra("name", String.valueOf(str_name));
        startActivity(intent);
    }


}
