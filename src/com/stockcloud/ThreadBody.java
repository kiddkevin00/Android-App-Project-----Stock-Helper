package com.stockcloud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

public class ThreadBody {
	// Title!!!!
	public String title;
	public String text;
	public long date;
	public int thread_id;
	public final int floor = 0;
	public String user_created;
	public int reply_count;
	public String topic_name;

	AmazonDynamoDBClient dynamoDB;
	final String tableName = "stock-threads";
	final String tableName2 = "stock-replies";

	public ThreadBody(String title, String t, String u, int id,
			String topic_name) {
		this.title = title;
		this.text = t;
		this.user_created = u;
		this.thread_id = id;
		this.topic_name = topic_name;
		this.reply_count = 0;
	}

	public ThreadBody() {
		// TODO Auto-generated constructor stub
		this.reply_count = 0;

		Date d = new Date();
		this.date = d.getTime();
	}

	public int number_of_replies(String tscn, int thread_id) {
		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		Condition condition1 = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(tscn));
		Condition condition2 = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(Integer.toString(thread_id)));
		scanFilter.put("topic_name", condition1);
		scanFilter.put("thread_id", condition2);
		ScanRequest scanRequest = new ScanRequest(tableName)
				.withScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);
		// System.out.println("Scan Result: " + scanResult);
		dynamoDB.shutdown();
		if (scanResult.getCount() == 0)
			return -1;
		if (scanResult.getCount() > 1)
			return -1;

		Map<String, AttributeValue> targetRecord = scanResult.getItems().get(0);
		int reply_count = Integer.parseInt(targetRecord
				.get("number_of_replies").getS());

		return reply_count;
	}

	public ThreadReply addReplyToThread(String text, String user_email,
			int target_floor) {
		ThreadReply tr = new ThreadReply();

		tr.text = text;
		tr.user_created = user_email;
		tr.thread_id = this.thread_id;
		tr.replyTo = target_floor;
		tr.topic_name = this.topic_name;
		this.reply_count = number_of_replies(this.topic_name, this.thread_id);

		tr.floor = ++this.reply_count;

		tr.saveToDB();
		this.updateReplies();
		return tr;
	}

	public void print() {
		System.out.println(this.date);
		System.out.println(this.user_created);
		System.out.println(this.topic_name);
		System.out.println(this.thread_id);
		System.out.println(this.floor);
		System.out.println(this.text);
		System.out.println(this.reply_count);
	}

	public void saveToDB() {
		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		Map<String, AttributeValue> thread = newThread(this);
		PutItemRequest putItemRequest = new PutItemRequest(tableName, thread);
		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
		// System.out.println("Result: " + putItemResult);

		dynamoDB.shutdown();
	}

	public void updateReplies() {
		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("topic+thread",
				new AttributeValue().withS(this.topic_name + "+"
						+ Integer.toString(this.thread_id)));

		Map<String, AttributeValueUpdate> update = new HashMap<String, AttributeValueUpdate>();
		update.put(
				"number_of_replies",
				new AttributeValueUpdate().withAction(AttributeAction.PUT)
						.withValue(
								new AttributeValue(Integer
										.toString(this.reply_count))));

		UpdateItemRequest updateItemRequest = new UpdateItemRequest()
				.withTableName(tableName).withKey(key)
				.withAttributeUpdates(update);
		UpdateItemResult updateItemResult = dynamoDB
				.updateItem(updateItemRequest);
		dynamoDB.shutdown();
	}

	private static Map<String, AttributeValue> newThread(ThreadBody tb) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("topic+thread", new AttributeValue(tb.topic_name + "+"
				+ Integer.toString(tb.thread_id)));
		item.put("date", new AttributeValue(Long.toString(tb.date)));
		item.put("user_created", new AttributeValue(tb.user_created));
		item.put("topic_name", new AttributeValue(tb.topic_name));
		item.put("thread_id",
				new AttributeValue(Integer.toString(tb.thread_id)));
		item.put("floor", new AttributeValue(Integer.toString(tb.floor)));
		item.put("title", new AttributeValue(tb.title));
		item.put("text", new AttributeValue(tb.text));
		item.put("number_of_replies",
				new AttributeValue(Integer.toString(tb.reply_count)));

		return item;
	}

	public List<ThreadReply> listReplies() {
		List<ThreadReply> replies = new ArrayList<ThreadReply>();

		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		Condition condition1 = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(this.topic_name));
		scanFilter.put("topic_name", condition1);
		Condition condition2 = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(Integer.toString(this.thread_id)));
		scanFilter.put("thread_id", condition2);

		ScanRequest scanRequest = new ScanRequest(tableName2)
				.withScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);
		// System.out.println("Scan Result: " + scanResult);

		List<Map<String, AttributeValue>> result = scanResult.getItems();
		for (Map<String, AttributeValue> reply : result) {
			ThreadReply this_reply = new ThreadReply();
			this_reply.text = reply.get("text").getS();
			this_reply.floor = Integer.parseInt(reply.get("floor").getS());
			this_reply.replyTo = Integer.parseInt(reply.get("reply_to").getS());
			this_reply.voting = Integer.parseInt(reply.get("voting").getS());
			this_reply.topic_name = reply.get("topic_name").getS();
			this_reply.thread_id = Integer.parseInt(reply.get("thread_id")
					.getS());
			this_reply.user_created = reply.get("user_created").getS();
			this_reply.date = Long.parseLong(reply.get("date").getS());

			replies.add(this_reply);
		}

		if (replies.size() == 0)
			return null;
		return sortReplies(replies);
	}

	private List<ThreadReply> sortReplies(List<ThreadReply> replies) {
		Collections.sort(replies, new Comparator<ThreadReply>() {

			@Override
			public int compare(ThreadReply o1, ThreadReply o2) {
				// TODO Auto-generated method stub
				if (o1.voting != o2.voting)
					return o2.voting - o1.voting;
				else
					return o1.floor - o2.floor;
			}

		});
		return replies;
	}
}
