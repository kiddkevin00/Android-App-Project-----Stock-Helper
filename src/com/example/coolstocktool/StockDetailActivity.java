package com.example.coolstocktool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StockDetailActivity extends ActionBarActivity {

	private Button _addBookmark;
	private Button _findThread;
	public Context _context;
	public TextView _stockName;

	public String stockName;
	public String email;
	public String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_detail);

		_context = this;
		_stockName = (TextView) findViewById(R.id.stockName);
		_findThread = (Button) findViewById(R.id.findThread);
		_addBookmark = (Button) findViewById(R.id.addBookmark);

		Intent intent = getIntent();
		stockName = intent.getStringExtra("stockName");
		email = intent.getStringExtra("email");
		password = intent.getStringExtra("password");
		Log.d("***", "Account and Stock Name in Detail: " + stockName + email
				+ password);

		_addBookmark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		_stockName.setText(stockName);

		_findThread.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent thrIntent = new Intent();
				thrIntent.putExtra("stockName", stockName);
				thrIntent.putExtra("email", email);
				thrIntent.putExtra("password", password);
				thrIntent.setClass(_context, TopicForAnStockActivity.class);
				startActivity(thrIntent);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_detail, menu);
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
