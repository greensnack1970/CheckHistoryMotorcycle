package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

public class CreateNewMotorcyclesActivity extends AppCompatActivity {

    private EditText edt_brand;
    private EditText edt_color;
    private EditText edt_license_plate;

    private ImageView img_1;
    private ImageView img_2;
    private ImageView img_3;
    private ImageView img_4;

    private RadioGroup rg_category;

    private Bitmap bitmapImage1;
    private Bitmap bitmapImage2;
    private Bitmap bitmapImage3;
    private Bitmap bitmapImage4;

    private String str_brand;
    private String str_color;
    private String str_license_plate;
    private String str_category;
    private String str_owner_id;
    private String str_imagename1;
    private String str_imagename2;
    private String str_imagename3;
    private String str_imagename4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_motorcycle);

        str_owner_id = getIntent().getStringExtra("owner_id");

        bindWidgets();
    }

    private void bindWidgets() {
        edt_brand = (EditText) findViewById(R.id.edt_brand);
        edt_color = (EditText) findViewById(R.id.edt_color);
        edt_license_plate = (EditText) findViewById(R.id.edt_license_plate);
        rg_category = (RadioGroup) findViewById(R.id.rg_category);

        img_1 = (ImageView) findViewById(R.id.img_1);
        img_2 = (ImageView) findViewById(R.id.img_2);
        img_3 = (ImageView) findViewById(R.id.img_3);
        img_4 = (ImageView) findViewById(R.id.img_4);
    }


    public void btn_select_img1(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    public void btn_select_img2(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    public void btn_select_img3(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 3);
    }

    public void btn_select_img4(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 4);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            set2ImageView(data, requestCode);
        }
    }


    public void set2ImageView(Intent data, int requestCode) {
        InputStream imageStream = null;

        try {
            imageStream = getContentResolver().openInputStream(data.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String value = new Random().nextInt(999999999) + "" + new Random().nextInt(999999999);
        switch (requestCode) {
            case 1:
                str_imagename1 = String.valueOf(value);
                bitmapImage1 = BitmapFactory.decodeStream(imageStream);
                img_1.setImageBitmap(bitmapImage1);

                break;
            case 2:
                str_imagename2 = String.valueOf(value);
                bitmapImage2 = BitmapFactory.decodeStream(imageStream);
                img_2.setImageBitmap(bitmapImage2);
                break;
            case 3:
                str_imagename3 = String.valueOf(value);
                bitmapImage3 = BitmapFactory.decodeStream(imageStream);
                img_3.setImageBitmap(bitmapImage3);
                break;
            case 4:
                str_imagename4 = String.valueOf(value);
                bitmapImage4 = BitmapFactory.decodeStream(imageStream);
                img_4.setImageBitmap(bitmapImage4);
                break;
            default:
                break;
        }
    }

    class CreateNewMotorcycle extends AsyncTask<String, Void, String> {

        public ProgressDialog progressDialog = new ProgressDialog(CreateNewMotorcyclesActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("... Loading ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            str_brand = edt_brand.getText().toString();
            str_color = edt_color.getText().toString();
            str_license_plate = edt_license_plate.getText().toString();
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

        private void insertToDatabase() {
            MySqlite mySqlite = new MySqlite(getApplicationContext());
            SQLiteDatabase db = mySqlite.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("brand", str_brand);
            values.put("color", str_color);
            values.put("license_plate", str_license_plate);

            switch (rg_category.getCheckedRadioButtonId()){
                case R.id.rb_classic:
                    values.put("category", "classic");
                break;
                case R.id.rb_enduro:
                    values.put("category", "enduro");
                    break;
                case R.id.rb_bigbike:
                    values.put("category", "bigbike");
                    break;
                case R.id.rb_other:
                    values.put("category", "other");
                    break;
            }

            values.put("owner_id", str_owner_id);
            values.put("picture1_name", str_imagename1);
            values.put("picture2_name", str_imagename2);
            values.put("picture3_name", str_imagename3);
            values.put("picture4_name", str_imagename4);
            db.insert("motorcycles", null, values);
            db.close();


            save_motorcycleImgToExternalStorage(bitmapImage1, str_imagename1 + ".png");
            save_motorcycleImgToExternalStorage(bitmapImage2, str_imagename2 + ".png");
            save_motorcycleImgToExternalStorage(bitmapImage3, str_imagename3 + ".png");
            save_motorcycleImgToExternalStorage(bitmapImage4, str_imagename4 + ".png");

            finish();
        }

        private void save_motorcycleImgToExternalStorage(Bitmap bitmap, String filename) {
            try {

                File folder = new File(Environment.getExternalStorageDirectory().toString() + "/motorcycles_pictures");
                folder.mkdirs();
                File dest = new File(Environment.getExternalStorageDirectory() + "/motorcycles_pictures", filename);
                FileOutputStream out = new FileOutputStream(dest);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void btn_submit_click(View view) {
        new CreateNewMotorcycle().execute();
    }


}
