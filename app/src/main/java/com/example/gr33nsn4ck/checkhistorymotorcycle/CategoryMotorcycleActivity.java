package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.gr33nsn4ck.checkhistorymotorcycle.Adapter.StaticGridViewAdapter;

public class CategoryMotorcycleActivity extends AppCompatActivity {

    GridView gridView;
    private String[] topics = new String[]{"Classic", "Bigbike", "Enduro", "Other"};
    private int[] images = new int[]{R.drawable.btn_classic,
            R.drawable.btn_enduro,
            R.drawable.btn_bigbike,
            R.drawable.btn_other};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_motorcycle_);



        gridView = (GridView) findViewById(R.id.gridView_Category);
        gridView.setAdapter(new StaticGridViewAdapter(getApplicationContext(), topics, images));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryMotorcycleActivity.this, ShowSeparateCategoryListActivity.class);
                switch (position){
                    case 0:
                        intent.putExtra("category", "classic");
                        break;
                    case 1:
                        intent.putExtra("category", "enduro");
                        break;
                    case 2:
                        intent.putExtra("category", "bigbike");
                        break;
                    case 3:
                        intent.putExtra("category", "other");
                        break;
                }
                startActivity(intent);
            }
        });

    }



}
