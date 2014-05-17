package com.example.coolstocktool;

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

import com.stockcloud.ChatTopic;

public class MainActivity extends ActionBarActivity {

	private Button _toBookmark;
	private Context _context;
	public TextView _email;
	public TextView _password;

	// for testing
	public test tt;
	public TextView _test;

	public String email;
	public String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_context = this;
		_email = (TextView) findViewById(R.id.ueserID);
		_password = (TextView) findViewById(R.id.password);
		_test = (TextView) findViewById(R.id.test);
		_toBookmark = (Button) findViewById(R.id.toBM);

		_toBookmark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("***", "email: " + _email.getText().toString().equals(""));
				if (!(_email.getText().toString().equals(""))
						&& !(_password.getText().toString().equals(""))) {
					email = _email.getText().toString();
					password = _password.getText().toString();
				} else {
					email = "123";
					password = "123";
				}
				Log.d("***", "account Log In: " + email + password);
				Intent intent = new Intent();
				intent.putExtra("email", email);
				intent.putExtra("password", password);
				intent.setClass(_context, BookMarkListActivity.class);
				startActivity(intent);

			}
		});

		tt = new test();
		tt.execute("APPLEINC");
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
	private class test extends AsyncTask<String, integer, ChatTopic> {

		@Override
		protected ChatTopic doInBackground(String... v) {

			ChatTopic testTopic = new ChatTopic();
			Log.d("***", "String: " + v[0]);
			testTopic = testTopic.findChatTopic(v[0]);
			return testTopic;

		}

		// for getView method
		@Override
		protected void onPostExecute(ChatTopic result) {

			if (result != null) {
				Log.d("*****", "onpost result2: " + result.thread_count);
				_test.setText(Integer.toString(result.thread_count));
			} else {
				Log.d("**", "fail");
			}

		}

	}

}
