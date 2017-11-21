package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.gr33nsn4ck.checkhistorymotorcycle.Adapter.CustomAdapterListViewShowCustomerList;

import java.util.ArrayList;

public class ShowCustomersListActivity extends AppCompatActivity {

    ListView listview;
    SwipeRefreshLayout swipeRefreshLayout;

    private int page = 0;
    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> nickname = new ArrayList<String>();
    private ArrayList<String> sex = new ArrayList<String>();
    private ArrayList<String> age = new ArrayList<String>();
    private ArrayList<String> picture_name = new ArrayList<String>();
    private CustomAdapterListViewShowCustomerList adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_customers_list);


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

        listview = (ListView) findViewById(R.id.listView_show_customers);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            int totalItemCount;
            int position;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(position == totalItemCount && scrollState == 0){
                    new GetCustomersListTask().execute();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.position = firstVisibleItem+visibleItemCount;
                this.totalItemCount = totalItemCount;
            }
        });

        new GetCustomersListTask().execute();
    }



    class GetCustomersListTask extends AsyncTask<String,Void,Cursor> {
        @Override
        protected Cursor doInBackground(String... params) {
            MySqlite mySqlite = new MySqlite(getApplicationContext());
            SQLiteDatabase db = mySqlite.getReadableDatabase();
            String query = "SELECT * FROM customers " +
                    "ORDER BY _id DESC " +
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
                adapter = new CustomAdapterListViewShowCustomerList(getApplicationContext(), id, nickname, sex, age, picture_name);
                listview.setAdapter(adapter);
            }
            page += 1;

        }
    } // end task



    private void putCursor2ArrayList(Cursor cursor) {
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                id.add(cursor.getString(cursor.getColumnIndex("_id")));
                nickname.add(cursor.getString(cursor.getColumnIndex("nickname")));
                sex.add(cursor.getString(cursor.getColumnIndex("sex")));
                age.add(cursor.getString(cursor.getColumnIndex("age")));
                picture_name.add(cursor.getString(cursor.getColumnIndex("picture_name")));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_customer, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // ปุ่มเพิ่มลูกค้าใหม่
            case R.id.action_new_customer:
                startActivity(new Intent(ShowCustomersListActivity.this, CreateNewCustomerActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
