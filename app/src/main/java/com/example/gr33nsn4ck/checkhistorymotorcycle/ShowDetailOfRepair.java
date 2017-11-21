package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

public class ShowDetailOfRepair extends AppCompatActivity {


    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;

    private TextView tv_customer_name;
    private TextView tv_motorcycle_brand;
    private TextView tv_color;
    private TextView tv_license_plate;
    private TextView tv_category;
    private TextView tv_phonenumber;
    private TextView tv_detail;
    private TextView tv_date;
    private TextView tv_target_date;
    private TextView tv_repair_state;
    private TextView tv_repair_detail;
    private SwipeRefreshLayout swipeRefreshLayout;


    private String _id;
    private String str_customer_name = null;
    private String str_motorcycle_brand = null;
    private String str_color = null;
    private String str_license_plate = null;
    private String str_category = null;
    private String str_phonenumber = null;
    private String str_detail = null;
    private String str_date = null;
    private String str_target_date = null;
    private String str_repair_state = null;
    private String str_repair_detail = null;
    private String str_img1 = null;
    private String str_img2 = null;
    private String str_img3 = null;
    private String str_img4 = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_of_repair);

        _id = getIntent().getStringExtra("id");

        // ผูก Object กับ Widget
        bindWidgets();

        // get Data into cursor
        Cursor cursor = selectData();
        if (cursor.moveToFirst()) {
            try {
                getInformations(cursor);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void bindWidgets() {
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);

        tv_customer_name = (TextView) findViewById(R.id.tv_customer_name);
        tv_motorcycle_brand = (TextView) findViewById(R.id.tv_motorcycle_brand);
        tv_color = (TextView) findViewById(R.id.tv_color);
        tv_license_plate = (TextView) findViewById(R.id.tv_license_plate);
        tv_category = (TextView) findViewById(R.id.tv_category);
        tv_phonenumber = (TextView) findViewById(R.id.tv_phonenumber);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_target_date = (TextView) findViewById(R.id.tv_target_date);
        tv_repair_state = (TextView) findViewById(R.id.tv_repair_state);
        tv_repair_detail = (TextView)findViewById(R.id.tv_repair_detail);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                finish();
                startActivity(getIntent());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private Cursor selectData() {
        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getReadableDatabase();
        String query = "SELECT * FROM history WHERE _id = \"" + _id + "\"";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    private void getInformations(Cursor cursor) throws FileNotFoundException {
        // get Data from cursor
        str_img1 = cursor.getString(cursor.getColumnIndex("img1"));
        str_img2 = cursor.getString(cursor.getColumnIndex("img2"));
        str_img3 = cursor.getString(cursor.getColumnIndex("img3"));
        str_img4 = cursor.getString(cursor.getColumnIndex("img4"));
        str_customer_name = cursor.getString(cursor.getColumnIndex("customer_name"));
        str_motorcycle_brand = cursor.getString(cursor.getColumnIndex("motorcycle_brand"));
        str_color = cursor.getString(cursor.getColumnIndex("color"));
        str_license_plate = cursor.getString(cursor.getColumnIndex("license_plate"));
        str_category = cursor.getString(cursor.getColumnIndex("category"));
        str_phonenumber = cursor.getString(cursor.getColumnIndex("phonenumber"));
        str_detail = cursor.getString(cursor.getColumnIndex("detail"));
        str_date = cursor.getString(cursor.getColumnIndex("date"));
        str_target_date = cursor.getString(cursor.getColumnIndex("target_date"));
        str_repair_state = cursor.getString(cursor.getColumnIndex("repair_state"));
        str_repair_detail = cursor.getString(cursor.getColumnIndex("repair_detail"));

        set2Widgets();
    }

    private void set2Widgets() throws FileNotFoundException {
        // set Data
        tv_customer_name.setText("เจ้าของรถ : " + str_customer_name);
        tv_motorcycle_brand.setText("รุ่น : " + str_motorcycle_brand);
        tv_color.setText("สี : " + str_color);
        tv_license_plate.setText("หมายเลขทะเบียน : "+str_license_plate);
        tv_category.setText("ประเภท : "+str_category);
        tv_phonenumber.setText("เบอร์โทร : "+str_phonenumber);
        tv_detail.setText("อาการเสีย : "+str_detail);
        tv_date.setText("วันที่ : "+str_date);
        tv_target_date.setText("วันครบกำหนดการซ่อม :"+str_target_date);
        tv_repair_state.setText("สถานะการซ่อม : "+str_repair_state);
        tv_repair_detail.setText("รายการที่ซ่อม : \n"+str_repair_detail);


        File img1 = new File(Environment.getExternalStorageDirectory()+"/history_pictures/", str_img1 + ".png");
        Bitmap bitmap1 = BitmapFactory.decodeStream(new FileInputStream(img1));
        imageView1.setImageBitmap(bitmap1);

        File img2 = new File(Environment.getExternalStorageDirectory()+"/history_pictures/", str_img2 + ".png");
        if(img2.exists()) {
            Bitmap bitmap2 = BitmapFactory.decodeStream(new FileInputStream(img2));
            imageView2.setImageBitmap(bitmap2);
        }
        File img3 = new File(Environment.getExternalStorageDirectory() + "/history_pictures/", str_img3 + ".png");
        if(img3.exists()) {
            Bitmap bitmap3 = BitmapFactory.decodeStream(new FileInputStream(img3));
            imageView3.setImageBitmap(bitmap3);
        }

        File img4 = new File(Environment.getExternalStorageDirectory()+"/history_pictures/", str_img4 + ".png");
        if(img4.exists()) {
            Bitmap bitmap4 = BitmapFactory.decodeStream(new FileInputStream(img4));
            imageView4.setImageBitmap(bitmap4);
        }
    }

    private void getInformationForSendSms(){
        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getWritableDatabase();

        String[] columns = new String[]{"detail", "license_plate", "repair_detail", "target_date", "phonenumber"};
        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{_id};

        // ดึงเบอร์โทร , ข้อมูลการซ่อม จากตาราง history เฉพาะ id = ? เพื่อส่ง SMS
        Cursor cursor = db.query("history", columns, whereClause, whereArgs, null, null, null);
        if(cursor.moveToFirst()){
            String detail = cursor.getString(cursor.getColumnIndex("detail"));
            String license_plate = cursor.getString(cursor.getColumnIndex("license_plate"));
            String target_date = cursor.getString(cursor.getColumnIndex("target_date"));
            String repaired_detail = cursor.getString(cursor.getColumnIndex("repair_detail"));
            String phonenumber = cursor.getString(cursor.getColumnIndex("phonenumber"));
            sendSMS(detail, license_plate, target_date, phonenumber, repaired_detail);
        }
        cursor.close();
    }

    private void sendSMS(String detail, String license_plate, String target_date, String phonenumber, String repair_detail) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phonenumber));

        switch (str_repair_state){
            case "รออะไหล่":
                intent.putExtra("sms_body", "[ระบบแจ้งเตือนสถานะการซ่อม]\n"+
                                            "การซ่อมของรถหมายเลขทะเบียน : "+license_plate+"\nอยู่ในสถานะ : รออะไหล่\n"+
                                            "อาการเสียของรถ : "+detail+"\n");
                startActivity(intent);
                break;
            case "กำลังซ่อม":
                intent.putExtra("sms_body", "[ระบบแจ้งเตือนสถานะการซ่อม]\n"+
                                            "การซ่อมของรถหมายเลขทะเบียน : "+license_plate+"\nอยู่ในสถานะ : กำลังซ่อม\n"+
                                            "อาการเสียของรถ : "+detail+"\n");
                startActivity(intent);
                break;
            case "ซ่อมเสร็จแล้ว":
                intent.putExtra("sms_body", "[ระบบแจ้งเตือนสถานะการซ่อม]\n"+
                                            "การซ่อมของรถหมายเลขทะเบียน : "+license_plate+"\n"+
                                            "อยู่ในสถานะ : ซ่อมเสร็จแล้ว\n"+
                                            "สามารถมารับรถและชำระเงินได้ตั้งแต่วันที่ : "+target_date+"\n"+
                                            "[รายชื่ออะไหล่+ราคาที่ใช้ในการซ่อม] : \n"+
                                            repair_detail+"\n"+
                                            "รวมทั้งสิ้น .... บาท\n");
                startActivity(intent);
                break;
            case "ชำระเงินแล้ว":
                intent.putExtra("sms_body", "[ระบบแจ้งเตือนสถานะการซ่อม]\n"+
                                            "การซ่อมของรถหมายเลขทะเบียน : "+license_plate+"\n"+
                                            "อยู่ในสถานะ : ชำระเงินแล้ว \n"+
                                            "ขอบคุณที่ใช้บริการครับ");
                startActivity(intent);
                break;
        }
    }



    public void btn_edit(View view) {
        Intent intent = new Intent(ShowDetailOfRepair.this, EditInformationAfterRepairedActivity.class);
        intent.putExtra("id", _id);
        startActivity(intent);
    }

    public void btn_send_sms(View view) {
        getInformationForSendSms();
    }



}

