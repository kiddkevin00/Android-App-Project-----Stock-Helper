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
import android.widget.EditText;

public class SearchStockActivity extends ActionBarActivity {

	public Button _searchStockButton;
	public Context _context;
	public EditText _searchStockName;

	public String stockName;
	public String email;
	public String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_stock);

		_context = this;
		_searchStockName = (EditText) findViewById(R.id.searchStockName);
		_searchStockButton = (Button) findViewById(R.id.searchStock);

		_searchStockName.setText("APPLEINC");

		Intent intent2 = getIntent();
		email = intent2.getStringExtra("email");
		password = intent2.getStringExtra("password");

		Log.d("***", "Account in search Stock : " + email + password);

		_searchStockButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stockName = _searchStockName.getText().toString();
				Log.d("***", "Stock Name to search: " + stockName);
				Intent intent = new Intent();
				intent.setClass(_context, StockDetailActivity.class);
				intent.putExtra("stockName", stockName);
				intent.putExtra("email", email);
				intent.putExtra("password", password);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_stock, menu);
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
