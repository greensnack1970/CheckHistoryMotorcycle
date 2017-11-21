package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditCustomerInformationActivity extends AppCompatActivity {


    private EditText edt_name;
    private EditText edt_nickname;
    private EditText edt_address;
    private EditText edt_age;
    private EditText edt_sex;
    private EditText edt_phonenumber;

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
        setContentView(R.layout.activity_edit_customer_information);

        str_id = getIntent().getStringExtra("id");
        bindWidgets();

        getUserInformaitonFromId();
        set2Widgets();
    }

    private void bindWidgets() {
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_nickname = (EditText) findViewById(R.id.edt_nickname);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_age = (EditText) findViewById(R.id.edt_age);
        edt_sex = (EditText) findViewById(R.id.edt_sex);
        edt_phonenumber = (EditText) findViewById(R.id.edt_phonenumber);
    }


    private void getUserInformaitonFromId() {
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


    private void set2Widgets() {
        edt_name.setText(str_name);
        edt_nickname.setText(str_nickname);
        edt_address.setText(str_address);
        edt_age.setText(str_age);
        edt_sex.setText(str_sex);
        edt_phonenumber.setText(str_phonenumber);
    }


    private void updateData2Databse() {
        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getWritableDatabase();
        String selectwhere = "_id = ?";
        String[] selectarg = new String[]{str_id};

        ContentValues values = new ContentValues();
        values.put("name", str_name);
        values.put("nickname", str_nickname);
        values.put("address", str_address);
        values.put("age", str_age);
        values.put("sex", str_sex);

        db.update("customers", values, selectwhere, selectarg);
        db.close();

        redirect2ShowCustomerInformationActivity();
    }

    private void redirect2ShowCustomerInformationActivity() {
        Intent intent = new Intent(EditCustomerInformationActivity.this, ShowCustomerInformationActivity.class);
        intent.putExtra("id", str_id);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "แก้ไขข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show();
        EditCustomerInformationActivity.this.finish();
    }



    public void btn_edit(View view) {
        str_name = edt_name.getText().toString();
        str_nickname = edt_nickname.getText().toString();
        str_address = edt_address.getText().toString();
        str_age = edt_age.getText().toString();
        str_sex = edt_sex.getText().toString();

        updateData2Databse();
    }


}
