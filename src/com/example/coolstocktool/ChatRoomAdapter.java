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

import com.stockcloud.ThreadReply;

public class ChatRoomAdapter extends ArrayAdapter<ThreadReply> implements
		OnClickListener {

	class LayoutForm3 {
		TextView text;
	}

	public View row;
	public Activity _context;
	public ArrayList<ThreadReply> data;

	// constructor
	public ChatRoomAdapter(Context context, int resource,
			List<ThreadReply> objects) {
		super(context, resource, objects);
		Log.d("***", "yo");
		// allow = true;
		this._context = (Activity) context;
		// get data
		data = (ArrayList<ThreadReply>) objects;
		Log.d("***", "data from topic for an stock: " + data.get(0));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("***", "yo2");
		row = convertView;
		LayoutForm3 item;
		if (row == null) {
			LayoutInflater inflater = _context.getLayoutInflater();
			row = inflater.inflate(R.layout.adapter_chat_room, null);
			item = new LayoutForm3();
			item.text = (TextView) row.findViewById(R.id.textView1);
			row.setTag(item);
			row.setOnClickListener(this);
		}
		item = (LayoutForm3) row.getTag();

		String text = data.get(position).text;
		Log.d("***", "text: " + text);
		item.text.setText(text);

		return row;

	}

	@Override
	public void onClick(View v) {

		// String stockName = ((LayoutForm3)
		// v.getTag()).text.getText().toString();
		// Log.d("***", "Clicked Stock Name: " + stockName);

		// Intent intent = new Intent();
		// intent.putExtra("", stockName);
		// intent.setClass(_context, StockDetailActivity.class);
		// _context.startActivity(intent);

	}
}
