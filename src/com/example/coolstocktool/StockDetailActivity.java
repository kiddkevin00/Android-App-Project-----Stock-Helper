package com.example.coolstocktool;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.stockcloud.User;
import com.stockcloud.searchstock;
import com.stockcloud.update30;
import com.stockcloud.updatestock;

public class StockDetailActivity extends ActionBarActivity {

	private Button _addBookmark;
	private Button _findThread;
	private Button _deleteBookmark;
	public Context _context;
	public TextView _stockName;
	public TextView _price;
	public TextView _updateTime;
	public TextView _openPrice;
	public TextView _predictPrice;

	public String stockName;
	public String email;
	public String password;
	public User usr;
	public AddBookMarkAsyncTask addBookMarkAsync;
	public StockDetailAsyncTask stockDetailAsync;
	public StockPredictAsyncTask stockPredictAsync;
	public DeleteBookMarkAsyncTask deleteBookMarkAsync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_detail);

		_context = this;
		_stockName = (TextView) findViewById(R.id.stockNameResult);
		_findThread = (Button) findViewById(R.id.findThread);
		_addBookmark = (Button) findViewById(R.id.addBookmark);
		_price = (TextView) findViewById(R.id.priceResult);
		_updateTime = (TextView) findViewById(R.id.updateTimeResult);
		_openPrice = (TextView) findViewById(R.id.openPriceResult);
		_predictPrice = (TextView) findViewById(R.id.predictPriceResult);
		_deleteBookmark = (Button) findViewById(R.id.delete);

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
				List<String> paras = new ArrayList<String>();
				paras.add(email);
				paras.add(password);
				paras.add(stockName);
				addBookMarkAsync = new AddBookMarkAsyncTask();
				addBookMarkAsync.execute(paras);
			}
		});

		_deleteBookmark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<String> paras2 = new ArrayList<String>();
				paras2.add(email);
				paras2.add(password);
				paras2.add(stockName);
				deleteBookMarkAsync = new DeleteBookMarkAsyncTask();
				deleteBookMarkAsync.execute(paras2);
			}
		});

		_stockName.setText(stockName);

		// if (stockName.equals("APPLEINC")) {
		// String stockNameString = "ABT";
		stockDetailAsync = new StockDetailAsyncTask();
		stockDetailAsync.execute(stockName);
		// } else {
		stockPredictAsync = new StockPredictAsyncTask();
		stockPredictAsync.execute(stockName);
		// }

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

	private class AddBookMarkAsyncTask extends
			AsyncTask<List<String>, integer, String> {

		@Override
		protected String doInBackground(List<String>... v) {

			Log.d("***", "Input5: " + v[0].get(0) + v[0].get(1) + v[0].get(2));
			usr = new User();
			try {
				Log.d("***",
						"User retrieve : "
								+ usr.retrieveUser(v[0].get(0), v[0].get(1)));

				if (usr.retrieveUser(v[0].get(0), v[0].get(1)) != null) {
					usr = usr.retrieveUser(v[0].get(0), v[0].get(1));
				} else {
					usr = usr.retrieveUser("123", "123");
				}
				usr.addFavorite(v[0].get(2));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "okay";
		}

		// for getView method
		@Override
		protected void onPostExecute(String result) {

			if (result != null) {
				Log.d("*****", "onpost result2: " + result);

			} else {

			}
		}
	}

	private class DeleteBookMarkAsyncTask extends
			AsyncTask<List<String>, integer, String> {

		@Override
		protected String doInBackground(List<String>... v) {

			Log.d("***", "Input9: " + v[0].get(0) + v[0].get(1) + v[0].get(2));
			usr = new User();
			try {
				Log.d("***",
						"User retrieve : "
								+ usr.retrieveUser(v[0].get(0), v[0].get(1)));

				if (usr.retrieveUser(v[0].get(0), v[0].get(1)) != null) {
					usr = usr.retrieveUser(v[0].get(0), v[0].get(1));
				} else {
					usr = usr.retrieveUser("123", "123");
				}
				usr.deleteFavorite(v[0].get(2));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "okay";
		}

		// for getView method
		@Override
		protected void onPostExecute(String result) {

			if (result != null) {
				Log.d("*****", "onpost result9: " + result);

			} else {

			}
		}
	}

	private class StockDetailAsyncTask extends
			AsyncTask<String, integer, List<String>> {

		@Override
		protected List<String> doInBackground(String... v) {

			Log.d("***", "String: " + v[0]);
			updatestock updatest = new updatestock();
			searchstock searchst = new searchstock();
			Log.d("***", "testingg");
			List<String> success = new ArrayList<String>();
			try {
				updatest.update(v[0]);
				Log.d("***", "test result : " + searchst.search(v[0]).get(0)
						+ searchst.search(v[0]).get(1)
						+ searchst.search(v[0]).get(2));
				success = new ArrayList<String>();
				success = searchst.search(v[0]);
			} catch (Exception e) {
				success.add("error!!");
				Log.d("****", "wow2");
				e.printStackTrace();
			}

			return success;

		}

		@Override
		protected void onPostExecute(List<String> result) {

			if (result != null) {
				Log.d("*****", "onpost result8: " + result.size());
				_price.setText(result.get(2));
				_updateTime.setText(result.get(0));
				_openPrice.setText(result.get(1));

			} else {
				Log.d("**", "fail");
			}

		}
	}

	private class StockPredictAsyncTask extends
			AsyncTask<String, integer, String> {

		@Override
		protected String doInBackground(String... v) {

			Log.d("***", "String: " + v[0]);
			Log.d("***", "testingg");
			update30 update_30 = new update30();
			int success;
			String successString;
			try {
				Log.d("***", "test result : ");
				success = update_30.update_30(v[0]);
				successString = Integer.toString(success);
			} catch (Exception e1) {
				successString = "fail!";
				Log.d("****", "wow2");
				e1.printStackTrace();

			}

			return successString;

		}

		@Override
		protected void onPostExecute(String result) {

			if (result != null) {
				Log.d("*****", "onpost result2: " + result);
				_predictPrice.setText(result);
			} else {
				Log.d("**", "fail");
			}

		}
	}
}
