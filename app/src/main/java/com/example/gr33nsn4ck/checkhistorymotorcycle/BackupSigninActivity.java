package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;

public class BackupSigninActivity extends AppCompatActivity {

    private String str_username;
    private String str_password;
    private EditText edt_username;
    private EditText edt_password;
    private Button btn_signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_signin);


        bindWidgets();

        // ถ้าสมัครสมาชิกแล้วจะมองไม่เห็นปุ่ม Create New Password
        checkUserSignin();
    }


    private void bindWidgets() {
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_signup = (Button) findViewById(R.id.btn_signup);
    }


    // ถ้ากลับมาจากหน้า Register แล้วจะไม่สามารถมองเห็นปุ่ม SignUp
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            btn_signup.setVisibility(View.GONE);
        }
    }


    private void checkUserSignin() {
        SharedPreferences spf = getSharedPreferences("backup_preference", Context.MODE_PRIVATE);
        boolean signup_state = spf.getBoolean("backup_signup_state", false);
        if (signup_state) {
            btn_signup.setVisibility(View.GONE); // ซ่อนปุ่ม Sign-up
        }
    }



    class LoginTask extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog = new ProgressDialog(BackupSigninActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(".. กำลังตรวจสอบ ..");
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            try {
                response = Ion.with(getApplicationContext())
                        .load("http://trymycmh.16mb.com/signin.php")
                        .setBodyParameter("username", params[0])
                        .setBodyParameter("password", params[1])
                        .asString()
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if(response.length() == 33){
                SharedPreferences spf = getSharedPreferences("backup_preference", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = spf.edit();
                editor. putBoolean("bk_signin_state", true);
                editor.putString("foldername", response.replace(" ", ""));
                editor.commit();

                startActivity(new Intent(getApplicationContext(), BackupDatabaseActivity.class));
                BackupSigninActivity.this.finish();
            }else{
                Toast.makeText(getApplicationContext(), "Login Fail !", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void btn_signup_clicked(View view) {
        startActivityForResult(new Intent(BackupSigninActivity.this, BackupSignUpActivity.class), 1);
    }

    public void btn_signin_clicked(View view) {
        str_username = edt_username.getText().toString();
        str_password = edt_password.getText().toString();

        new LoginTask().execute(str_username, str_password);
    }



}
