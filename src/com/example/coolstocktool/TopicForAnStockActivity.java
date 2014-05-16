package com.example.coolstocktool;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class TopicForAnStockActivity extends ActionBarActivity {

	public class DataForm2 {
		String name;

	}

	public ListView _threadList;
	public Context context;
	public TopicForAnStockAdapter adapter;
	public ArrayList<DataForm2> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_for_an_stock);

		context = this;

		// hard-code input data
		data = new ArrayList<DataForm2>();

		DataForm2 bucket = new DataForm2();
		bucket.name = "Dave";
		// bucket.ownerName = "Bob";
		// bucket.describe = "Detail Info";
		// bucket.dateCreate = "2014";
		// bucket.stockName = "ASUS";

		data.add(bucket);

		context = this;
		adapter = new TopicForAnStockAdapter(context,
				R.layout.adapter_topic_for_an_stock, data);

		_threadList = (ListView) findViewById(R.id.threadList);
		_threadList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.topic_for_an_stock, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
