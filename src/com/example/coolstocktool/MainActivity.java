package com.example.coolstocktool;

<<<<<<< HEAD
import android.R.integer;
import android.content.Context;
import android.content.Intent;
=======
import java.util.concurrent.ExecutionException;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
>>>>>>> master
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
<<<<<<< HEAD
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.stockcloud.Test;
=======
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;
>>>>>>> master

import com.example.cloudtools.*;

public class MainActivity extends ActionBarActivity {

<<<<<<< HEAD
	private Button _toBookmark;
	private Context _context;
	public TextView _email;
	public TextView _password;

	// for testing
	public test tt;
	// public TextView _test;

	public String email;
	public String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_context = this;
		_email = (TextView) findViewById(R.id.ueserID);
		_password = (TextView) findViewById(R.id.password);
		// _test = (TextView) findViewById(R.id.test);
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

		// tt = new test();
		// tt.execute("nothing");
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
	private class test extends AsyncTask<String, integer, String> {

		@Override
		protected String doInBackground(String... v) {

			Log.d("***", "String: " + v[0]);
			Log.d("***", "testingg");
			Test t = new Test();
			try {
				t.testing();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String successString = "cool";
			try {
				Log.d("***", "test result : ");

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
				Log.d("*****", "onpost result101: " + result);
				// _test.setText("Update: " + result);
			} else {
				Log.d("**", "fail");
			}

		}
	}
=======
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        TextView tv = (TextView) findViewById(R.id.textView1);
		try {
			tv.setText(new S3Task().execute().get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
    class S3Task extends AsyncTask<Void,Void,String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			s3Init si = new s3Init();
    		return si.init();
		}
    }
>>>>>>> master

}
