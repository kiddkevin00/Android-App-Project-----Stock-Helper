package com.example.coolstocktool;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class BookMarkListActivity extends ActionBarActivity {

	public Button _searchThread;
	public ListView _listView;
	private Context context;
	public BookMarkAdapter adapter;

	public ArrayList<DataFrom> data;

	public class DataFrom {
		public String stockName;
		public String id;
		public String describe;
		public String dateCreate;
		public String ownerName;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_list);

		context = this;

		// hard-code input data
		data = new ArrayList<DataFrom>();

		DataFrom bucket = new DataFrom();
		bucket.id = "0001";
		bucket.ownerName = "Bob";
		bucket.describe = "Detail Info";
		bucket.dateCreate = "2014";
		bucket.stockName = "ASUS";

		data.add(bucket);
		Log.d("***", "Heyyyy");

		adapter = new BookMarkAdapter(context, R.layout.adapter_bookmark, data);
		_listView = (ListView) findViewById(R.id.listView1);
		_listView.setAdapter((ListAdapter) adapter);

		_searchThread = (Button) findViewById(R.id.searchThread);
		_searchThread.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, SearchStockActivity.class);
				intent.putExtra("meg2", "from bookmark acitivity");
				startActivity(intent);
			}
		});
	}
}
