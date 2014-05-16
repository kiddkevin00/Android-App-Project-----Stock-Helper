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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coolstocktool.BookMarkListActivity.DataFrom;

public class BookMarkAdapter extends ArrayAdapter<DataFrom> implements
		OnClickListener {

	// protected boolean allow;
	public View row;
	public Activity context;
	public ArrayList<DataFrom> data;

	class LayoutForm {
		public ImageView imageView;
		public TextView text;
		public String imageUri;

	}

	// constructor
	public BookMarkAdapter(Context context, int resource, List<DataFrom> objects) {
		super(context, resource, objects);
		Log.d("***", "hello0");
		// allow = true;
		this.context = (Activity) context;
		// get data
		data = (ArrayList<DataFrom>) objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("***", "hello1");
		row = convertView;
		LayoutForm item;
		Log.d("***", "hello2");
		if (row == null) {
			Log.d("***", "hello3");
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.adapter_bookmark, null);
			item = new LayoutForm();
			item.imageView = (ImageView) row.findViewById(R.id.imageView1);
			item.text = (TextView) row.findViewById(R.id.textView1);
			row.setTag(item);
			row.setOnClickListener(this);
		}
		Log.d("***", "hello4");
		item = (LayoutForm) row.getTag();

		Log.d("***", "hello5");
		String text = data.get(position).stockName;
		item.text.setText(text);

		Log.d("***", "hello6");

		return row;

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.putExtra("sms", "from bookmark activity");
		intent.setClass(context, TopicForAnStockActivity.class);
		context.startActivity(intent);

	}

}
