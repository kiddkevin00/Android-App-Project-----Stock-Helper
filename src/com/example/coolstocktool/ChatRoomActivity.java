package com.example.coolstocktool;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

public class ChatRoomActivity extends ActionBarActivity {

	public ListView _chatList;
	public Button _sendMessage;
	public Context _context;
	public ChatRoomAdapter _adapter;
	public List<String> messageList;
	private ChatRoomAsyncTask chatRoomAsync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);

		_context = this;
		_chatList = (ListView) findViewById(R.id.listView1);
		_sendMessage = (Button) findViewById(R.id.sendMessage);

		// for testing
		List<String> test = new ArrayList<String>();
		test.add("haha");

		// set adapter
		_adapter = new ChatRoomAdapter(_context, R.layout.adapter_chat_room,
				test);
		_chatList = (ListView) findViewById(R.id.listView1);
		_chatList.setAdapter(_adapter);

	}

	// chatRoomAsync = new ChatRoomAsyncTask();
	// chatRoomAsync.execute("");

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	private class ChatRoomAsyncTask extends
			AsyncTask<List<String>, integer, List<String>> {

		@Override
		protected List<String> doInBackground(List<String>... v) {

			Log.d("***", "Input: " + v[0].get(0) + v[0].get(1));
			messageList = new ArrayList<String>();

			return messageList;
		}

		// for getView method
		@Override
		protected void onPostExecute(List<String> result) {

			if (result != null) {
				Log.d("*****", "onpost result3: " + result.size());

				// set adapter
				_adapter = new ChatRoomAdapter(_context,
						R.layout.adapter_chat_room, result);
				_chatList = (ListView) findViewById(R.id.listView1);
				_chatList.setAdapter(_adapter);
			} else {

			}
		}
	}

}
