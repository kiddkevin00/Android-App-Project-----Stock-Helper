package com.example.coolstocktool;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BookMarkAdapter extends ArrayAdapter<String> implements
		OnClickListener {

	// protected boolean allow;
	public View row;
	public Activity _context;
	public ArrayList<String> data;
	public String email;
	public String password;

	class LayoutForm {
		public TextView _text;
		public String email2;
		public String password2;

	}

	// constructor
	public BookMarkAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		// allow = true;
		this._context = (Activity) context;
		// get data
		data = (ArrayList<String>) objects;
		Log.d("***", "data from bookmark: " + data.get(0));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		row = convertView;
		LayoutForm item;
		if (row == null) {
			LayoutInflater inflater = _context.getLayoutInflater();
			row = inflater.inflate(R.layout.adapter_bm, null);
			item = new LayoutForm();
			item._text = (TextView) row.findViewById(R.id.textView1);
			row.setTag(item);
			row.setOnClickListener(this);
		}
		item = (LayoutForm) row.getTag();

		String stockName = data.get(position);
		Log.d("***", "text: " + stockName);
		item._text.setText(stockName);

		return row;

	}

	@Override
	public void onClick(View v) {

		String stockName = ((LayoutForm) v.getTag())._text.getText().toString();
		Log.d("***", "Clicked Stock Name: " + stockName);

		// BookMarkListActivity.help(stockName, _context);

		Intent intent = new Intent();
		intent.putExtra("stockName", stockName);
		intent.putExtra("email", "123");
		intent.putExtra("password", "123");
		intent.setClass(_context, StockDetailActivity.class);
		_context.startActivity(intent);

	}
}
