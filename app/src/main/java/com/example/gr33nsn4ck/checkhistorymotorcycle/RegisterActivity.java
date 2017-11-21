package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText edt_password;
    private String str_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_password = (EditText) findViewById(R.id.edt_password);
    }


    public void signup_submit(View view) {
        str_password = edt_password.getText().toString();
        insertPassword2Database();
    }


    private void insertPassword2Database() {
        MySqlite sqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = sqlite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", str_password);

        db.insert("admin", null, values);
        db.close();
        if(!db.isOpen()){
            // change signin-state = true
            SharedPreferences spf = getSharedPreferences("com.example.gr33nsn4ck.checkhistorymotorcycle", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = spf.edit();
            edit.putBoolean("signup-state", true);
            edit.commit();

            Toast.makeText(getApplicationContext(), "Register Success :D", Toast.LENGTH_SHORT).show();
            RegisterActivity.this.setResult(RESULT_OK);
            RegisterActivity.this.finish();
        }
    }
}
