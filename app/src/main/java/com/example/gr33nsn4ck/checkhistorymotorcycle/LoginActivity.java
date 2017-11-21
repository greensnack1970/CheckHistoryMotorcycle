package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 1;
    private EditText edt_password;
    private Button btn_signup;

    private String str_password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btn_signup = (Button) findViewById(R.id.btn_signup);
        edt_password = (EditText) findViewById(R.id.edt_password);

        // ถ้าสมัครสมาชิกแล้วจะมองไม่เห็นปุ่ม Create New Password
        checkUserSignupState();
    }



    // ถ้ากลับมาจากหน้า Register แล้วจะไม่สามารถมองเห็นปุ่ม SignUp
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SIGNUP && resultCode == RESULT_OK){
            btn_signup.setVisibility(View.GONE);
        }
    }


    private void checkUserSignupState() {
        SharedPreferences spf = getSharedPreferences("com.example.gr33nsn4ck.checkhistorymotorcycle", Context.MODE_PRIVATE);
        boolean signup_state = spf.getBoolean("signup-state", false);
        if (signup_state) {
            Log.e(".......", "สมัครสมาชิกแล้ว");
            btn_signup.setVisibility(View.GONE); // ซ่อนปุ่ม Sign-up
        }
    }

    // เมธอทสำหรับเช็ค password ที่ผู้ใช้กรอก กับพาสเวิร์ดในฐานข้อมูล (ตาราง admin)
    private String getPwdOfAdmin(String password) {
        String result = null;

        MySqlite sqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = sqlite.getReadableDatabase();

        String[] columns = new String[]{"password"};
        String selection = "password = ?";
        String[] selectionArgs = new String[]{password};

        Cursor cursor = db.query("admin", columns, selection, selectionArgs, null,null,null);
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                result = cursor.getString(cursor.getColumnIndex("password"));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return result;
    }


    private void checkSigninPassword(String oldPassword) {
        try {
            // ถ้าพาสเวิร์ดตรงกับที่เคยสมัครสมาชิกไว้
            if (oldPassword.equals(str_password)) {
                changeSigninState();
            } else {
                Toast.makeText(getApplicationContext(), "Login Fail !", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Login Fail !", Toast.LENGTH_LONG).show();
        }
    }

    // change signin-state = true
    private void changeSigninState() {
        SharedPreferences spf = getSharedPreferences("com.example.gr33nsn4ck.checkhistorymotorcycle", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean("signin-state", true);
        edit.commit();

        // Redirect 2 MainActivity
        LoginActivity.this.finish();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }


    public void btn_signup(View view) {
        startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), REQUEST_SIGNUP);
    }

    public void btn_signin(View view) {

        if(!edt_password.getText().toString().equals("")) {
            str_password = edt_password.getText().toString();
            String oldPassword = getPwdOfAdmin(str_password);

            checkSigninPassword(oldPassword);
        }else {
            Toast.makeText(getApplicationContext(), "Login Fail !", Toast.LENGTH_LONG).show();
        }
    }


}
