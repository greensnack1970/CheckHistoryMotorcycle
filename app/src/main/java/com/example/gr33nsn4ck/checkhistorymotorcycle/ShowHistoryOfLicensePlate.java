package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;
import com.example.gr33nsn4ck.checkhistorymotorcycle.Adapter.CustomAdapterListViewShowHistory;

import java.util.ArrayList;

public class ShowHistoryOfLicensePlate extends AppCompatActivity {


    ListView listView;
    CustomAdapterListViewShowHistory adapter;
    ArrayList<String> id = new ArrayList<String>();
    ArrayList<String> motorcycle_brand = new ArrayList<String>();
    ArrayList<String> customer_name = new ArrayList<String>();
    ArrayList<String> repair_state = new ArrayList<String>();
    ArrayList<String> img1 = new ArrayList<String>();

    String license_plate = null;
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history_of_motorcycle);

        license_plate = getIntent().getStringExtra("license_plate");
        listView = (ListView) findViewById(R.id.listView_history);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int position;
            int totalitemcount;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(position == totalitemcount && scrollState == 0){
                    new SelectDataTask().execute();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.position = firstVisibleItem+visibleItemCount;
                this.totalitemcount = totalItemCount;
            }
        });

        new SelectDataTask().execute();
    }



    class SelectDataTask extends AsyncTask<String, Void, Cursor>{
        @Override
        protected Cursor doInBackground(String... params) {
            MySqlite mySqlite = new MySqlite(getApplicationContext());
            SQLiteDatabase db = mySqlite.getReadableDatabase();
            String query = "SELECT * FROM history " +
                           "WHERE license_plate =\""+license_plate+"\" " +
                           "ORDER BY _id DESC " +
                           "LIMIT "+page+"0,10";

            Cursor cursor = db.rawQuery(query, null);

            return cursor;
        }
        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            if(page >= 1){
                cursor2ArrayList(cursor);
                adapter.notifyDataSetChanged();
            }else {
                cursor2ArrayList(cursor);
                adapter = new CustomAdapterListViewShowHistory(getApplicationContext(), id, motorcycle_brand, customer_name, repair_state, img1);
                listView.setAdapter(adapter);
            }
            page += 1;
        }
    }

    private void cursor2ArrayList(Cursor cursor) {
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                id.add(cursor.getString(cursor.getColumnIndex("_id")));
                motorcycle_brand.add(cursor.getString(cursor.getColumnIndex("motorcycle_brand")));
                customer_name.add(cursor.getString(cursor.getColumnIndex("customer_name")));
                repair_state.add(cursor.getString(cursor.getColumnIndex("repair_state")));
                img1.add(cursor.getString(cursor.getColumnIndex("img1")));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }


}
