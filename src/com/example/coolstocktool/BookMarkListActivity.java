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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.stockcloud.User;

public class BookMarkListActivity extends ActionBarActivity {

	public Button _searchThread;
	public ListView _listView;
	private Context _context;
	public BookMarkAdapter _adapter;

	public ArrayList<String> data;
	public User usr;
	public BookmarkAsyncTask bookmarkAsync;
	public String email;
	public String password;

	public class DataFrom {
		// public String stockName;
		// public String id;
		// public String describe;
		// public String dateCreate;
		// public String ownerName;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_list);

		_context = this;

		// hard-code input data
		// data = new ArrayList<DataFrom>();
		// DataFrom bucket = new DataFrom();
		// bucket.id = "0001";
		// bucket.ownerName = "Bob";
		// bucket.describe = "Detail Info";
		// bucket.dateCreate = "2014";
		// bucket.stockName = "ASUS";
		// data.add(bucket);
		//
		// _adapter = new BookMarkAdapter(_context, R.layout.adapter_bm, data);
		// _listView = (ListView) findViewById(R.id.listView1);
		// _listView.setAdapter((ListAdapter) _adapter);

		Intent intent = getIntent();
		email = intent.getStringExtra("email");
		password = intent.getStringExtra("password");
		Log.d("***",
				"Account info: " + intent.getStringExtra("email")
						+ intent.getStringExtra("password"));
		bookmarkAsync = new BookmarkAsyncTask();
		List<String> params = new ArrayList<String>();
		params.add(email);
		params.add(password);
		bookmarkAsync.execute(params);

		_searchThread = (Button) findViewById(R.id.searchThread);
		_searchThread.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(_context, SearchStockActivity.class);
				intent.putExtra("email", email);
				intent.putExtra("password", password);
				startActivity(intent);
			}
		});
	}

	// for testing
	private class BookmarkAsyncTask extends
			AsyncTask<List<String>, integer, List<String>> {

		@Override
		protected List<String> doInBackground(List<String>... v) {

			Log.d("***", "Input: " + v[0].get(0) + v[0].get(1));
			usr = new User();
			List<String> usrBmList = new ArrayList<String>();
			try {
				Log.d("***",
						"User retrieve : "
								+ usr.retrieveUser(v[0].get(0), v[0].get(1)));

				if (usr.retrieveUser(v[0].get(0), v[0].get(1)) != null) {
					usr = usr.retrieveUser(v[0].get(0), v[0].get(1));

					if (usr.listFavorites() != null) {
						Log.d("***",
								"Size : "
										+ Integer.toString(usr.listFavorites()
												.size()));
						usrBmList = usr.listFavorites();

					} else {
						Log.d("**", "");
						usrBmList.add("Asus");
						usrBmList.add("Acer");
					}
					Log.d("***", "user's BM list: " + usrBmList);

				} else {
					usr = usr.retrieveUser("123", "123");
					if (usr.listFavorites() != null) {
						Log.d("***",
								"Size2 : "
										+ Integer.toString(usr.listFavorites()
												.size()));
						usrBmList = usr.listFavorites();

					} else {
						Log.d("**", "");
						usrBmList.add("Asus");
						usrBmList.add("Acer");
					}
					Log.d("***", "user's BM list: " + usrBmList);

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return usrBmList;

		}

		// for getView method
		@Override
		protected void onPostExecute(List<String> result) {

			if (result != null) {
				Log.d("*****", "onpost result2: " + result.size());

				// set adapter
				_adapter = new BookMarkAdapter(_context, R.layout.adapter_bm,
						result);
				_listView = (ListView) findViewById(R.id.listView1);
				_listView.setAdapter(_adapter);
			} else {
				// system alert dialog
				// AlertDialog.Builder alertDialogBuilder = new
				// AlertDialog.Builder(
				// _context);
				// alertDialogBuilder.setTitle("Sorry!!");
				// alertDialogBuilder.setMessage("Nothing Found!!!")
				// .setPositiveButton("Try Another Name",
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// onBackPressed();
				// }
				// });
				// AlertDialog alertDialog = alertDialogBuilder.create();
				//
				// alertDialog.show();
			}
		}

	}
}
