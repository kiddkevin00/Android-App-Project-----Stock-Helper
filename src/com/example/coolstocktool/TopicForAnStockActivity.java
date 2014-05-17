package com.example.coolstocktool;

import java.util.List;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.stockcloud.ChatTopic;
import com.stockcloud.ThreadBody;

public class TopicForAnStockActivity extends ActionBarActivity {

	public class DataForm2 {
		// String title;
		// String text;
		// long date;
		// int thread_id;
		// String user_created;
		// int reply_count;
		// String topic_name;

	}

	public ListView _threadList;
	public Context _context;
	public TopicForAnStockAdapter _adapter;

	// public ArrayList<DataForm2> data;
	public TopicAsyncTask topicAsync;
	public String stockName;
	public String email;
	public String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_for_an_stock);

		_context = this;

		Intent intent = getIntent();
		stockName = intent.getStringExtra("stockName");
		email = intent.getStringExtra("email");
		password = intent.getStringExtra("password");

		Log.d("***", "Account and Stock Name for the following topics : "
				+ stockName + email + password);

		// hard-code input data
		// data = new ArrayList<DataForm2>();
		// DataForm2 bucket = new DataForm2();
		// for testing back-end
		// bucket.number = tesTopic.thread_count;
		// bucket.number = 123;
		// bucket.ownerName = "Bob";
		// bucket.describe = "Detail Info";
		// bucket.dateCreate = "2014";
		// bucket.stockName = "ASUS";
		// data.add(bucket);
		// Log.d("***", "data : " + data);

		topicAsync = new TopicAsyncTask();
		topicAsync.execute(stockName);

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

	// for testing
	private class TopicAsyncTask extends
			AsyncTask<String, integer, List<ThreadBody>> {

		@Override
		protected List<ThreadBody> doInBackground(String... v) {

			ChatTopic ct = new ChatTopic();
			Log.d("***", "String: " + v[0]);
			ct = ct.findChatTopic(v[0]);
			List<ThreadBody> tb = ct.listThreads();
			return tb;

		}

		// for getView method
		@Override
		protected void onPostExecute(List<ThreadBody> result) {

			if (result != null) {
				Log.d("*****", "onpost result3: " + result.size());
				// set adapter
				_adapter = new TopicForAnStockAdapter(_context,
						R.layout.adapter_topic_for_an_stock, result);
				_threadList = (ListView) findViewById(R.id.threadList);
				_threadList.setAdapter(_adapter);
			} else {
				// system alert dialog
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						_context);
				alertDialogBuilder.setTitle("Sorry!!");
				alertDialogBuilder.setMessage("Nothing Found!!!")
						.setPositiveButton("Try Another Name",
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										onBackPressed();
									}
								});
				AlertDialog alertDialog = alertDialogBuilder.create();

				alertDialog.show();
			}
		}

	}
}
