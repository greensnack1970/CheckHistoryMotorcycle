package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gr33nsn4ck.checkhistorymotorcycle.Adapter.CustomAdapterListViewMain;

public class MainActivity extends AppCompatActivity {

    ListView listView_Main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ถ้ายังไม่ได้ Login ให้เด้งไปหน้า LoginActivity
        if(!checkUserSignin()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            MainActivity.this.finish();
            Toast.makeText(getApplicationContext(), "Sign-in Please !", Toast.LENGTH_SHORT).show();
        }


        listView_Main = (ListView) findViewById(R.id.listView_Main);
        listView_Main.setAdapter(new CustomAdapterListViewMain(getApplicationContext()));
        listView_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:startActivity(new Intent(MainActivity.this, CreateDetailRepairActivity.class));break;
                    case 1:startActivity(new Intent(MainActivity.this, SearchHistoryActivity.class));break;
                    case 2:startActivity(new Intent(MainActivity.this, CategoryMotorcycleActivity.class));break;
                    case 3:startActivity(new Intent(MainActivity.this, ShowCustomersListActivity.class));break;
                    case 4:startActivity(new Intent(MainActivity.this, BackupDatabaseActivity.class));break;
                    case 5:sign_out();break;
                    default:break;
                }
            }
        });





    }

    private boolean checkUserSignin() {
        SharedPreferences spf = getSharedPreferences("com.example.gr33nsn4ck.checkhistorymotorcycle", Context.MODE_PRIVATE);
        if (spf.getBoolean("signin-state", false)) {
            Log.e(".......", "login แล้ว");
            return true;
        }else{
            Log.e("......", "ยังไม่ได้ Login");
            return false;
        }
    }

    private void sign_out(){
        // เปลี่ยนค่า signin-state เป็น false
        SharedPreferences spf = getSharedPreferences("com.example.gr33nsn4ck.checkhistorymotorcycle", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putBoolean("signin-state", false);
        editor.commit();

        Intent intent = getIntent();
        MainActivity.this.finish();
        startActivity(intent);
    }

}
