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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class CreateNewCustomerActivity extends AppCompatActivity {


    private EditText edt_name;
    private EditText edt_nickname;
    private EditText edt_address;
    private EditText edt_age;
    private EditText edt_phone;
    private RadioGroup radiogroup_sex;
    private Button btn_select_photo;
    private ImageView imageView1;

    private Bitmap bitmapImage;

    private String str_name;
    private String str_nickname;
    private String str_sex;
    private String str_address;
    private String str_phone;
    private String str_age;
    private String str_picture_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_customer);

        bindWidgets();
    }


    private void bindWidgets() {
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_nickname = (EditText) findViewById(R.id.edt_nickname);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_age = (EditText) findViewById(R.id.edt_age);
        imageView1 = (ImageView) findViewById(R.id.photo);
        btn_select_photo = (Button) findViewById(R.id.btn_select_photo);

        radiogroup_sex = (RadioGroup) findViewById(R.id.radiogroup_sex);
        radiogroup_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male:
                        str_sex = "m";
                        break;
                    case R.id.rb_female:
                        str_sex = "f";
                        break;
                }
            }
        });
    }


    public void btn_select_photo_click(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                set2ImageView(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void set2ImageView(Intent data) throws IOException {
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(data.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String value = new Random().nextInt(999999999) + "" + new Random().nextInt(999999999);
        str_picture_name = String.valueOf(value);
        bitmapImage = BitmapFactory.decodeStream(imageStream);
        imageView1.setImageBitmap(bitmapImage);
    }





    class CreateNewCustomer extends AsyncTask<String,Void,String> {

        public ProgressDialog progressDialog = new ProgressDialog(CreateNewCustomerActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("... Loading ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            str_name = edt_name.getText().toString();
            str_nickname = edt_nickname.getText().toString();
            str_address = edt_address.getText().toString();
            str_phone = edt_phone.getText().toString();
            str_age = edt_age.getText().toString();
        }
        @Override
        protected String doInBackground(String... params) {
            insertToDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }

    } // End Task


    private void insertToDatabase() {
        if (str_name != null &&
                str_nickname != null &&
                str_address != null &&
                str_phone != null &&
                str_age != null &&
                str_sex != null &&
                str_picture_name != null) {

            MySqlite mySqlite = new MySqlite(getApplicationContext());
            SQLiteDatabase db = mySqlite.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("name", str_name);
            values.put("nickname", str_nickname);
            values.put("address", str_address);
            values.put("phonenumber", str_phone);
            values.put("age", str_age);
            values.put("sex", str_sex);
            values.put("picture_name", str_picture_name);
            db.insert("customers", null, values);
            db.close();

            redirectToShowCustomerInformationAcitivty();
            saveCustomerImgToExternalStorage(bitmapImage, str_picture_name + ".png");

        } else {
            Toast.makeText(CreateNewCustomerActivity.this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveCustomerImgToExternalStorage(Bitmap bitmap,String filename) {
        try {

            File folder = new File(Environment.getExternalStorageDirectory().toString()+"/customers_pictures");
            folder.mkdirs();

            File dest = new File(Environment.getExternalStorageDirectory()+"/customers_pictures", filename);
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redirectToShowCustomerInformationAcitivty() {
        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getWritableDatabase();

        String query = "SELECT COUNT(_id) from customers ORDER BY _id DESC LIMIT 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            long lastedId = c.getLong(0);
            Intent intent = new Intent(CreateNewCustomerActivity.this, ShowCustomerInformationActivity.class);
            intent.putExtra("id", String.valueOf(lastedId));
            startActivity(intent);
        }

        CreateNewCustomerActivity.this.finish();
    }



    public void btn_submit(View view) {
        new CreateNewCustomer().execute();
    }



}
