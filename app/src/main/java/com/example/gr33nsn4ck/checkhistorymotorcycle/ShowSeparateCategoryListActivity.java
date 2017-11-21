package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.gr33nsn4ck.checkhistorymotorcycle.Adapter.CustomAdapterListViewShowHistory;

import java.util.ArrayList;

public class ShowSeparateCategoryListActivity extends AppCompatActivity {

    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> motorcycle_brand = new ArrayList<String>();
    private ArrayList<String> customer_name = new ArrayList<String>();
    private ArrayList<String> repair_state = new ArrayList<String>();
    private ArrayList<String> img1 = new ArrayList<String>();
    private String category;
    private int page = 0;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private CustomAdapterListViewShowHistory adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_separate_category);

        category = getIntent().getStringExtra("category");

        listView = (ListView) findViewById(R.id.listview_separate_category);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int position;
            int totalitemcount;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(position == totalitemcount && scrollState == 0){
                    new GetInformationsTask().execute();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.position = firstVisibleItem+visibleItemCount;
                this.totalitemcount = totalItemCount;
            }
        });


        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                startActivity(getIntent());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        new GetInformationsTask().execute();
    }




    class GetInformationsTask extends AsyncTask<String, Void, Cursor> {
        @Override
        protected Cursor doInBackground(String... params) {
            return selectInformations();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            if(page >= 1){
                putCursor2ArrayList(cursor);
                adapter.notifyDataSetChanged();
            }else {
                putCursor2ArrayList(cursor);
                adapter = new CustomAdapterListViewShowHistory(getApplicationContext(), id, motorcycle_brand, customer_name, repair_state, img1);
                listView.setAdapter(adapter);
            }
            page += 1;
        }
    }


    private Cursor selectInformations() {
        MySqlite mySqlite = new MySqlite(getApplicationContext());
        SQLiteDatabase db = mySqlite.getReadableDatabase();

        String query = "SELECT * FROM history " +
                "WHERE category = \"" + category + "\" " +
                "ORDER BY _id DESC " +
                "LIMIT " + page + "0,10";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


    private void putCursor2ArrayList(Cursor cursor) {
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
