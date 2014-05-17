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

import com.stockcloud.ThreadBody;

public class TopicForAnStockAdapter extends ArrayAdapter<ThreadBody> implements
		OnClickListener {

	class LayoutForm2 {
		TextView _title;

		String title;
		String text;
		long date;
		int thread_id;
		String user_created;
		int reply_count;
		String topic_name;
		int floor;
	}

	public ArrayList<ThreadBody> data;

	public View _row;
	public Activity _context;

	public TopicForAnStockAdapter(Context context, int resource,
			List<ThreadBody> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		Log.d("***", "This is Topic Adapter Constructor");
		// allow = true;
		this._context = (Activity) context;
		// get data
		data = (ArrayList<ThreadBody>) objects;
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
			item._title = (TextView) _row.findViewById(R.id.textView1);
			_row.setTag(item);
			_row.setOnClickListener(this);
		}

		item = (LayoutForm2) _row.getTag();

		item.title = data.get(position).title;
		item.text = data.get(position).text;
		item.date = data.get(position).date;
		item.thread_id = data.get(position).thread_id;
		item.user_created = data.get(position).user_created;
		item.reply_count = data.get(position).reply_count;
		item.topic_name = data.get(position).topic_name;
		item.floor = data.get(position).floor;

		Log.d("***", "Title : " + data.get(position).title);
		item._title.setText(item.title);

		return _row;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		String title2 = ((LayoutForm2) v.getTag()).title;
		String text2 = ((LayoutForm2) v.getTag()).text;
		long date2 = ((LayoutForm2) v.getTag()).date;
		int thread_id2 = ((LayoutForm2) v.getTag()).thread_id;
		String user_created2 = ((LayoutForm2) v.getTag()).user_created;
		int reply_count2 = ((LayoutForm2) v.getTag()).reply_count;
		String topic_name2 = ((LayoutForm2) v.getTag()).topic_name;
		int floor2 = ((LayoutForm2) v.getTag()).floor;
		Log.d("***", "Clicked a Topic of specified Stock: " + title2);

		Intent intent = new Intent();
		intent.setClass(_context, ChatRoomActivity.class);
		intent.putExtra("title", title2);
		intent.putExtra("text", text2);
		intent.putExtra("date", date2);
		intent.putExtra("thread_id", thread_id2);
		intent.putExtra("user_created", user_created2);
		intent.putExtra("reply_count", reply_count2);
		intent.putExtra("topic_name", topic_name2);
		intent.putExtra("floor", floor2);
		_context.startActivity(intent);

	}
}
