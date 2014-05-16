package com.example.coolstocktool;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.coolstocktool.TopicForAnStockActivity.DataForm2;

public class TopicForAnStockAdapter extends ArrayAdapter<DataForm2> implements
		OnClickListener {

	class LayoutForm2 {
		TextView name;
	}

	public ArrayList<DataForm2> data;

	public View _row;
	public Activity _context;

	public TopicForAnStockAdapter(Context context, int resource,
			List<DataForm2> objects) {
		// TODO Auto-generated constructor stub
		super(context, resource, objects);
		Log.d("***", "This is Topic Adapter Constructor");
		// allow = true;
		this._context = (Activity) context;
		// get data
		data = (ArrayList<DataForm2>) objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// super.getView(position, convertView, parent);
		Log.d("***", "hello1");
		_row = convertView;
		LayoutForm2 item;
		Log.d("***", "hello2");
		if (_row == null) {
			Log.d("***", "hello30");
			LayoutInflater inflater = _context.getLayoutInflater();
			_row = inflater.inflate(R.layout.adapter_topic_for_an_stock, null);
			item = new LayoutForm2();
			// item.imageView = (ImageView) _row.findViewById(R.id.imageView1);
			item.name = (TextView) _row.findViewById(R.id.textView1);
			_row.setTag(item);
			_row.setOnClickListener(this);
		}
		Log.d("***", "hello40");
		item = (LayoutForm2) _row.getTag();

		Log.d("***", "hello50");
		String text = data.get(position).name;
		item.name.setText(text);

		Log.d("***", "hello60");

		return _row;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
