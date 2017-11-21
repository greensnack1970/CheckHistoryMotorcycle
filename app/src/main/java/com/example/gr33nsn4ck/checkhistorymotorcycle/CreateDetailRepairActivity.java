package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CreateDetailRepairActivity extends AppCompatActivity {


    private EditText edt_customer_name;
    private EditText edt_motorcycle_brand;
    private EditText edt_color;
    private EditText edt_license_plate;
    private RadioGroup radiogroup_category;
    private RadioButton radio_classic;
    private RadioButton radio_enuduro;
    private RadioButton radio_bigbike;
    private RadioButton radio_other;

    private EditText edt_phonenumber;
    private EditText edt_detail;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private EditText edt_target_date;

    private Bitmap selectedImage1;
    private Bitmap selectedImage2;
    private Bitmap selectedImage3;
    private Bitmap selectedImage4;

    private String str_customer_name;
    private String str_motorcycle_brand;
    private String str_color;
    private String str_license_plate;
    private String str_phone_number;
    private String str_detail;
    private String str_target_date;
    private String str_img1name = null;
    private String str_img2name = null;
    private String str_img3name = null;
    private String str_img4name = null;
    private String str_category;

    // Auto Infomation
    private String owner_id;
    private Integer motorcycle_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createdetail_repairhistory);

        bindWidgets();

        if (getIntent().getStringExtra("owner_id") != null) {
            owner_id = getIntent().getStringExtra("owner_id");
            motorcycle_id = getIntent().getIntExtra("motorcycle_id", 0);
            getAutoMotorcycleInformaiton(motorcycle_id);
            getAutoCustomerInformaiton(owner_id);
        }
    }

    private void getAutoMotorcycleInformaiton(Integer motorcycle_id) {
        MySqlite mySQLite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySQLite.getReadableDatabase();
        Cursor motorcycles_cursor = db.rawQuery("SELECT * FROM motorcycles WHERE _id = ?", new String[]{String.valueOf(motorcycle_id)});
        if (motorcycles_cursor.moveToFirst()) {
            while (!motorcycles_cursor.isAfterLast()) {
                String str_motorcycle_brand = motorcycles_cursor.getString(motorcycles_cursor.getColumnIndex("brand"));
                edt_motorcycle_brand.setText(str_motorcycle_brand);

                String str_color = motorcycles_cursor.getString(motorcycles_cursor.getColumnIndex("color"));
                edt_color.setText(str_color);

                String str_license_plate = motorcycles_cursor.getString(motorcycles_cursor.getColumnIndex("license_plate"));
                edt_license_plate.setText(str_license_plate);

                String str_category = motorcycles_cursor.getString(motorcycles_cursor.getColumnIndex("category"));
                switch (str_category){
                    case "classic":
                        radio_classic.setChecked(true);
                        break;
                    case "enduro":
                        radio_enuduro.setChecked(true);
                        break;
                    case "bigbike":
                        radio_bigbike.setChecked(true);
                        break;
                    case "other":
                        radio_other.setChecked(true);
                        break;
                }

                motorcycles_cursor.moveToNext();
            }
        }
        motorcycles_cursor.close();
        db.close();
    }

    private void getAutoCustomerInformaiton(String owner_id){
        MySqlite mySQLite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySQLite.getReadableDatabase();
        Cursor customers_cursor = db.rawQuery("SELECT * FROM customers WHERE _id = ?", new String[]{String.valueOf(owner_id)});
        if (customers_cursor.moveToFirst()) {
            while (!customers_cursor.isAfterLast()) {
                String str_name = customers_cursor.getString(customers_cursor.getColumnIndex("name"));
                edt_customer_name.setText(str_name);
                String str_phonenumber = customers_cursor.getString(customers_cursor.getColumnIndex("phonenumber"));
                edt_phonenumber.setText(str_phonenumber);
                customers_cursor.moveToNext();
            }
        }
        customers_cursor.close();
        db.close();
    }


    private void bindWidgets() {
        edt_customer_name = (EditText) findViewById(R.id.edt_customer_name);
        edt_motorcycle_brand = (EditText) findViewById(R.id.edt_motorcycle_brand);
        edt_color = (EditText) findViewById(R.id.edt_color);
        edt_license_plate = (EditText) findViewById(R.id.edt_license_plate);
        radiogroup_category = (RadioGroup) findViewById(R.id.radiogroup_category);
        edt_phonenumber = (EditText) findViewById(R.id.edt_phonenumber);
        edt_detail = (EditText) findViewById(R.id.edt_detail);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        edt_target_date = (EditText) findViewById(R.id.edt_target_date);

        radio_classic = (RadioButton) findViewById(R.id.radio_classic);
        radio_enuduro = (RadioButton) findViewById(R.id.radio_enduro);
        radio_bigbike = (RadioButton) findViewById(R.id.radio_bigbike);
        radio_other = (RadioButton) findViewById(R.id.radio_other);


        radiogroup_category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_classic:
                        str_category = "classic";
                        break;
                    case R.id.radio_enduro:
                        str_category = "enduro";
                        break;
                    case R.id.radio_bigbike:
                        str_category = "bigbike";
                        break;
                    case R.id.radio_other:
                        str_category = "other";
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 || requestCode == 2 || requestCode == 3 || requestCode == 4 && resultCode == RESULT_OK) {
            try {
                set2ImageView(requestCode, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Buttons //

    public void btn_pickimg1(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    public void btn_pickimg2(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    public void btn_pickimg3(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 3);
    }

    public void btn_pickimg4(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 4);
    }


    public void btn_submit(View view) throws FileNotFoundException {
        new CreateDetailRepairTask().execute();
    }


    class CreateDetailRepairTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(CreateDetailRepairActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("... Loading ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            str_customer_name = edt_customer_name.getText().toString();
            str_motorcycle_brand = edt_motorcycle_brand.getText().toString();
            str_color = edt_color.getText().toString();
            str_license_plate = edt_license_plate.getText().toString();
            str_category = CreateDetailRepairActivity.this.str_category;
            str_phone_number = edt_phonenumber.getText().toString();
            str_detail = edt_detail.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {
            submitDetailRepair();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }


    } // End AsyncTask


    private void set2ImageView(int requestCode, Intent data) throws IOException {

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(data.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // random image name
        Random rand = new Random();
        String value = rand.nextInt(999999999) + "" + rand.nextInt(999999999);

        switch (requestCode) {
            case 1:
                str_img1name = String.valueOf(value);
                selectedImage1 = BitmapFactory.decodeStream(imageStream);
                img1.setImageBitmap(selectedImage1);
                break;
            case 2:
                str_img2name = String.valueOf(value);
                selectedImage2 = BitmapFactory.decodeStream(imageStream);
                img2.setImageBitmap(selectedImage2);
                break;
            case 3:
                str_img3name = String.valueOf(value);
                selectedImage3 = BitmapFactory.decodeStream(imageStream);
                img3.setImageBitmap(selectedImage3);
                break;
            case 4:
                str_img4name = String.valueOf(value);
                selectedImage4 = BitmapFactory.decodeStream(imageStream);
                img4.setImageBitmap(selectedImage4);
                break;
            default:
                break;
        }


    }

    private void submitDetailRepair() {
        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("customer_name", str_customer_name);
        values.put("motorcycle_brand", str_motorcycle_brand);
        values.put("color", str_color);
        values.put("license_plate", str_license_plate);
        values.put("category", str_category);
        values.put("phonenumber", str_phone_number);
        values.put("detail", str_detail);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateTime = sdf.format(new Date());
        values.put("date", currentDateTime);

        //values.put("target_date", str_target_date);
        values.put("repair_state", "รออะไหล่");

        if (str_img1name != null) {
            saveToExternalStorageStorage(selectedImage1, str_img1name + ".png");
            values.put("img1", str_img1name);
        }
        if (str_img2name != null) {
            saveToExternalStorageStorage(selectedImage2, str_img2name + ".png");
            values.put("img2", str_img2name);
        }
        if (str_img2name != null) {
            saveToExternalStorageStorage(selectedImage3, str_img3name + ".png");
            values.put("img3", str_img3name);
        }
        if (str_img2name != null) {
            saveToExternalStorageStorage(selectedImage4, str_img4name + ".png");
            values.put("img4", str_img4name);
        }

        db.insert("history", null, values);
        db.close();

        // get lasted row id && Redirect User to ShowDetailOfRepair
        redirectToShowDetailAcitivty();
    }


    private void saveToExternalStorageStorage(Bitmap bitmap, String filename) {
        try {
            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/history_pictures");
            folder.mkdirs();

            File dest = new File(Environment.getExternalStorageDirectory() + "/history_pictures/", filename);
            FileOutputStream out = new FileOutputStream(dest);

            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void redirectToShowDetailAcitivty() {

        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getWritableDatabase();

        String query = "SELECT COUNT(_id) from history ORDER BY _id DESC LIMIT 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            String lastedId = String.valueOf(c.getLong(0));
            Intent intent = new Intent(CreateDetailRepairActivity.this, ShowDetailOfRepair.class);
            intent.putExtra("id", lastedId);
            startActivity(intent);
            CreateDetailRepairActivity.this.finish();
        }

        db.close();
    }


}
