package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditInformationAfterRepairedActivity extends AppCompatActivity {


    private EditText edt_date;
    private EditText edt_target_date;
    private RadioGroup radiogroup_repair_state;
    private EditText edt_detail_of_repair;

    private RadioButton radioButton0;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;


    private String _id;
    private String str_date;
    private String str_target_date;
    private String str_repair_state;
    private String str_repair_detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editrepair);


        _id = getIntent().getStringExtra("id");

        // ผูก Object กับ Widget
        bindWidgets();

        // get Data into cursor
        Cursor cursor = getOldInformation();
        if (cursor.moveToFirst()) {
            setDataToWidgets(cursor);  // set data from cursor into widget
        }
        cursor.close();
    }


    private void bindWidgets() {
        edt_date = (EditText) findViewById(R.id.edt_comein);
        edt_target_date = (EditText) findViewById(R.id.edt_target_date);
        radiogroup_repair_state = (RadioGroup) findViewById(R.id.radiogroup_repair_state);
        edt_detail_of_repair = (EditText) findViewById(R.id.edt_detail_of_repair);


        radioButton0 = (RadioButton) findViewById(R.id.radioButton0);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);

        radiogroup_repair_state = (RadioGroup) findViewById(R.id.radiogroup_repair_state);
        radiogroup_repair_state.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton0:
                        str_repair_state = "รออะไหล่";
                        break;
                    case R.id.radioButton1:
                        str_repair_state = "กำลังซ่อม";
                        break;
                    case R.id.radioButton2:
                        str_repair_state = "ซ่อมเสร็จแล้ว";
                        break;
                    case R.id.radioButton3:
                        str_repair_state = "ชำระเงินแล้ว";
                        break;
                }
            }
        });
    }


    private Cursor getOldInformation() {
        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getReadableDatabase();
        String query = "SELECT date, target_date, repair_state, repair_detail " +
                       "FROM history WHERE _id = \"" + _id + "\"";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    private void setDataToWidgets(Cursor cursor) {
        str_date = cursor.getString(cursor.getColumnIndex("date"));
        str_target_date = cursor.getString(cursor.getColumnIndex("target_date"));
        str_repair_state = cursor.getString(cursor.getColumnIndex("repair_state"));
        str_repair_detail = cursor.getString(cursor.getColumnIndex("repair_detail"));

        edt_date.setText(str_date);
        edt_target_date.setText(str_target_date);
        switch (str_repair_state){
            case "รออะไหล่":
                radioButton0.setChecked(true);
                break;
            case "กำลังซ่อม":
                radioButton1.setChecked(true);
                radioButton0.setVisibility(View.GONE);
                break;

            case "ซ่อมเสร็จแล้ว":radioButton2.setChecked(true);
                radioButton0.setVisibility(View.GONE);
                radioButton1.setVisibility(View.GONE);
                break;
            case "ชำระเงินแล้ว":radioButton3.setChecked(true);
                radioButton0.setVisibility(View.GONE);
                radioButton1.setVisibility(View.GONE);
                radioButton2.setVisibility(View.GONE);
                break;
        }
        edt_detail_of_repair.setText(str_repair_detail);
    }

    private void updateData2Database() {
        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("date", str_date);
        values.put("target_date", str_target_date);
        values.put("repair_state", str_repair_state);
        values.put("repair_detail", str_repair_detail);

        db.update("history", values, "_id = ?", new String[]{_id});
        db.close();

        redirectToShowDetailOfRepair();
    }

    private void redirectToShowDetailOfRepair() {
        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getWritableDatabase();

        String query = "SELECT COUNT(_id) from history ORDER BY _id DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            String lastedId = String.valueOf(cursor.getLong(0));
            Intent intent = new Intent(EditInformationAfterRepairedActivity.this, ShowDetailOfRepair.class);
            intent.putExtra("id", lastedId);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "~ แก้ไขกระทู้สำเร็จ ~", Toast.LENGTH_SHORT).show();
            EditInformationAfterRepairedActivity.this.finish();
        }

        cursor.close();
        db.close();
    }


    public void btn_submit(View view) {

        str_date = edt_date.getText().toString();
        str_repair_detail = edt_detail_of_repair.getText().toString();
        str_target_date = edt_target_date.getText().toString();

        updateData2Database();
    }


}
