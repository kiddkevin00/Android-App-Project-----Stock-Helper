package com.example.coolstocktool;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.stockcloud.ThreadBody;
import com.stockcloud.ThreadReply;

public class ChatRoomActivity extends ActionBarActivity {

	public ListView _chatList;
	public Button _sendMessage;
	public Context _context;
	public ChatRoomAdapter _adapter;
	public List<ThreadReply> _messageList;
	// for dialog
	public EditText _message;

	private ChatRoomAsyncTask chatRoomAsync;
	private SendMessageAsyncTask sendMessageAsync;
	public ThreadBody tb;
	// get intent
	public String title;
	public String text;
	public long date;
	public int thread_id;
	public String user_created;
	public int reply_count;
	public String topic_name;
	public int floor;
	public String email;
	public String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);

		_context = this;
		_chatList = (ListView) findViewById(R.id.listView1);
		_sendMessage = (Button) findViewById(R.id.sendMessage);

		// for testing
		// List<String> test = new ArrayList<String>();
		// test.add("haha");

		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		text = intent.getStringExtra("text");
		date = intent.getLongExtra("date", 0);
		thread_id = intent.getIntExtra("thread_id", 0);
		user_created = intent.getStringExtra("user_created");
		reply_count = intent.getIntExtra("reply_count", 0);
		topic_name = intent.getStringExtra("topic_name");
		floor = intent.getIntExtra("floor", 0);
		Log.d("***", "chatRoom: " + title + text + date + thread_id
				+ user_created + reply_count + topic_name + floor);
		tb = new ThreadBody(title, text, user_created, thread_id, topic_name);

		email = intent.getStringExtra("email");
		password = intent.getStringExtra("password");
		Log.d("***", "Account in Chat Room : " + email + password);

		chatRoomAsync = new ChatRoomAsyncTask();
		chatRoomAsync.execute(tb);

		_sendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(_context);

				// 2. Chain together various setter methods to set the dialog
				// characteristics
				builder.setTitle("Send Message..");

				// 3. setView in the middle of dialog
				LayoutInflater inflater = getLayoutInflater();
				final View view = inflater.inflate(R.layout.message, null);
				builder.setView(view);
				// 4. Add the buttons
				builder.setPositiveButton("Send",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User clicked CREATE button
								_message = (EditText) view
										.findViewById(R.id.nameEditText);

								List<String> paras = new ArrayList<String>();
								paras.add(email);
								paras.add(_message.getText().toString());
								Log.d("***",
										"Dialog: " + paras.get(0)
												+ paras.get(1));

								sendMessageAsync = new SendMessageAsyncTask();
								sendMessageAsync.execute(paras);

								// return to background
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
								dialog.dismiss();
							}
						});
				// 5. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				// 6. put this to where ever you want to show this dialog
				dialog.show();

			}
		});

	}

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
			AsyncTask<ThreadBody, integer, List<ThreadReply>> {

		@Override
		protected List<ThreadReply> doInBackground(ThreadBody... tb) {

			Log.d("***", "Input4: " + tb[0]);
			_messageList = tb[0].listReplies();

			return _messageList;
		}

		// for getView method
		@Override
		protected void onPostExecute(List<ThreadReply> result) {

			if (result != null) {
				Log.d("*****", "onpost result4: " + result.size());

				// set adapter
				_adapter = new ChatRoomAdapter(_context,
						R.layout.adapter_chat_room, result);
				_chatList = (ListView) findViewById(R.id.listView1);
				_chatList.setAdapter(_adapter);
			} else {

			}
		}
	}

	// for testing
	private class SendMessageAsyncTask extends
			AsyncTask<List<String>, integer, String> {

		@Override
		protected String doInBackground(List<String>... v) {

			try {
				Log.d("***", "Input7: " + v[0].get(0) + v[0].get(1));
				ThreadBody tBody = new ThreadBody(title, text, user_created,
						thread_id, topic_name);
				tBody.addReplyToThread(v[0].get(1), v[0].get(0), 0);

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
				Log.d("*****", "onpost result7: " + result);

			} else {

			}
		}

	}

}
