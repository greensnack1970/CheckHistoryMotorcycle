package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySqlite extends SQLiteOpenHelper {

    // GUIDE - http://developer.android.com/guide/topics/data/data-storage.html#db

    // DBNAME , TBAME , DB ver.
    private static final String DATABASE_NAME = "mysqlite";
    private static final int DATABASE_VERSION = 1;

    // Create a helper object to create, open, and/or manage a database.
    public MySqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE history"+
                "(_id INTEGER PRIMARY KEY," +
                "customer_name TEXT(10) NOT NULL," +
                "motorcycle_brand TEXT(40) NOT NULL," +
                "color TEXT(10) NOT NULL,"+
                "license_plate TEXT(10) NOT NULL,"+ // ป้ายทะเบียน
                "category TEXT(10) NOT NULL," +
                "phonenumber TEXT(10) NOT NULL," + // เบอร์โทรลูกค้า
                "detail TEXT(400) NOT NULL," +     // อาการเสีย
                "date TEXT(20) NOT NULL," +        // วันที่รับเข้ามาซ่อม
                "target_date TEXT(10),"+ // วันที่ครบกำหนดการซ่อม
                "repair_state TEXT(10),"+         // สถานะการซ่อม
                "repair_detail TEXT(400),"+       // รายการซ่อม , อะไหล่ , ราคา
                "img1 TEXT(50)," +
                "img2 TEXT(50)," +
                "img3 TEXT(50)," +
                "img4 TEXT(50));");

        db.execSQL("CREATE TABLE admin (id INTEGER PRIMARY KEY," +
                     "password TEXT(6) NOT NULL);");

        db.execSQL("CREATE TABLE customers (_id INTEGER PRIMARY KEY,"+
                "name TEXT(30)," +
                "nickname TEXT(20)," +
                "address TEXT(100)," +
                "age TEXT(3)," +
                "sex TEXT(1)," +
                "phonenumber TEXT(10)," +
                "picture_name TEXT(30));");

        db.execSQL("CREATE TABLE motorcycles (_id INTEGER PRIMARY KEY,"+
                "brand TEXT(30)," + // รุ่น
                "color TEXT(10)," + // สี
                "license_plate TEXT(10)," + // หมายเลขทะเบียน
                "category TEXT(10),"+   //  ประเภท
                "owner_id TEXT(5),"+  //  id ของเจ้าของรถ
                "picture1_name TEXT(30),"+  // ชื่อรูปภาพ
                "picture2_name TEXT(30),"+
                "picture3_name TEXT(30),"+
                "picture4_name TEXT(30));");

        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
