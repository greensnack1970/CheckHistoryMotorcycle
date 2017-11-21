package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchHistoryActivity extends AppCompatActivity {


    private EditText edt_license_plate;
    private Button btn_signup;
    private String license_plate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchhistory);

        edt_license_plate = (EditText) findViewById(R.id.edt_license_plate);
        btn_signup = (Button) findViewById(R.id.btn_signup);

    } // onCreate


    private void goToShowHistoryOfLicensePlate() {
        Intent intent = new Intent(getApplicationContext(), ShowHistoryOfLicensePlate.class);
        intent.putExtra("license_plate", license_plate);
        startActivity(intent);
    }


    // เซิสหาประวัติรถจากหมายเลขทะเบียน ส่งไปยัง ShowHistoryOfLicensePlate
    public void btn_search(View view) {
        license_plate = edt_license_plate.getText().toString();
        goToShowHistoryOfLicensePlate();
    }


}
