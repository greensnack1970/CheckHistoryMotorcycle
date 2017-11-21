package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gr33nsn4ck.checkhistorymotorcycle.Adapter.CustomAdapterListViewShowMotorcyclesList;

import java.util.ArrayList;

public class ShowMotrcyclesListActivity extends AppCompatActivity {

    // widges
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listview_motorcycles;
    private TextView tv_customer_name;
    private CustomAdapterListViewShowMotorcyclesList adapter;

    // motorcycles
    private ArrayList<Integer> _id = new ArrayList<Integer>(); // motorcycle id
    private ArrayList<String> brand = new ArrayList<String>();
    private ArrayList<String> color = new ArrayList<String>();
    private ArrayList<String> license_plate = new ArrayList<String>();
    private ArrayList<String> owner_id = new ArrayList<String>();
    private ArrayList<String> picture1_name = new ArrayList<String>();

    // arguments
    private String str_customer_id;
    private String str_customer_name;

    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_motrcycles_list);

        str_customer_id = getIntent().getStringExtra("id");
        str_customer_name = getIntent().getStringExtra("name");

        Log.e(".......", "customer id : " + str_customer_id);

        bindWisgets();
        tv_customer_name.setText("รถของคุณ : " + str_customer_name);


        listview_motorcycles.setOnScrollListener(new AbsListView.OnScrollListener() {
            int totalItemCount;
            int position;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (position == totalItemCount && scrollState == 0) {
                    new GetMotorcyclesListTask().execute();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.position = firstVisibleItem + visibleItemCount;
                this.totalItemCount = totalItemCount;
            }
        });



        new GetMotorcyclesListTask().execute();
    }



    private void bindWisgets() {
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        tv_customer_name = (TextView) findViewById(R.id.tv_customer_name);
        listview_motorcycles = (ListView) findViewById(R.id.listview_motorcycles);
    }



    class GetMotorcyclesListTask extends AsyncTask<String,Void,Cursor> {
        @Override
        protected Cursor doInBackground(String... params) {
            MySqlite mySqlite = new MySqlite(getApplicationContext());
            SQLiteDatabase db = mySqlite.getReadableDatabase();
            String query = "SELECT * FROM motorcycles "+
                    "WHERE owner_id = "+str_customer_id+
                    " ORDER BY _id DESC " +
                    "LIMIT "+page+"0,10";

            Cursor cursor = db.rawQuery(query, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            if(page >= 1){
                putCursor2ArrayList(cursor);
                adapter.notifyDataSetChanged();
            }else {
                putCursor2ArrayList(cursor);
                adapter = new CustomAdapterListViewShowMotorcyclesList(getApplicationContext(), _id, brand, color, license_plate, owner_id, picture1_name);
                listview_motorcycles.setAdapter(adapter);
            }
            page += 1;
        }
    } // end task


    private void putCursor2ArrayList(Cursor cursor) {
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                _id.add(cursor.getInt(cursor.getColumnIndex("_id")));
                brand.add(cursor.getString(cursor.getColumnIndex("brand")));
                color.add(cursor.getString(cursor.getColumnIndex("color")));
                license_plate.add(cursor.getString(cursor.getColumnIndex("license_plate")));
                owner_id.add(cursor.getString(cursor.getColumnIndex("owner_id")));
                picture1_name.add(cursor.getString(cursor.getColumnIndex("picture1_name")));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_showmotorcycles, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // ปุ่มเพิ่มลูกค้าใหม่
            case R.id.action_new_motorcycle:
                Intent intent = new Intent(ShowMotrcyclesListActivity.this, CreateNewMotorcyclesActivity.class);
                intent.putExtra("owner_id", str_customer_id);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
