package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class BackupSignUpActivity extends AppCompatActivity {

    private String str_username;
    private String str_password;

    private EditText edt_username;
    private EditText edt_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_signup);


        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
    }

    public void btn_submit(View view) {
        str_username = edt_username.getText().toString();
        str_password = edt_password.getText().toString();
        signupAccount();
    }


    private void signupAccount() {
        Ion.with(BackupSignUpActivity.this)
                .load("http://trymycmh.16mb.com/signup.php")
                .setBodyParameter("username", str_username)
                .setBodyParameter("password", str_password)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String response) {

                        if (response.length() == 10 && str_username.length() > 6 && str_password.length() > 6) {
                            // change backup_signup_state = true
                            SharedPreferences spf = getSharedPreferences("backup_preference", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = spf.edit();
                            edit.putBoolean("backup_signup_state", true);
                            edit.commit();

                            BackupSignUpActivity.this.setResult(RESULT_OK);
                            BackupSignUpActivity.this.finish();

                            Toast.makeText(BackupSignUpActivity.this, "[!] สมัครสมาชิกสำเร็จ :D", Toast.LENGTH_LONG).show();
                            Log.e("Signup response : ", "true");
                        }else{
                            Toast.makeText(BackupSignUpActivity.this, "การลงทะเบียนไม่สำเร็จ\n กรุณาใช้ Username และ Password ความยาวไม่ต่ำกว่า 6 หลัก", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }


}
