package com.stockcloud;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

public class ThreadReply {
	public String text;
	public int floor;
	public int replyTo;
	public int voting;
	public String user_created;
	public long date;
	public int thread_id;
	public String topic_name;

	AmazonDynamoDBClient dynamoDB;
	final String tableName = "stock-replies";

	public ThreadReply(String t, String u, int thread_id, int f, int rt,
			String topic_name) {
		this.text = t;
		this.floor = f;
		this.replyTo = rt;
		this.voting = 0;
		this.user_created = u;
		this.thread_id = thread_id;
		this.topic_name = topic_name;

		Date d = new Date();
		this.date = d.getTime();
	}

	public ThreadReply() {
		// TODO Auto-generated constructor stub
		this.voting = 0;

		Date d = new Date();
		this.date = d.getTime();
	}

	public void print() {
		System.out.println(this.date);
		System.out.println(this.user_created);
		System.out.println(this.topic_name);
		System.out.println(this.thread_id);
		System.out.println(this.floor);
		System.out.println(this.replyTo);
		System.out.println(this.text);
		System.out.println(this.voting);
	}

	public void saveToDB() {
		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		Map<String, AttributeValue> reply = newReply(this);
		PutItemRequest putItemRequest = new PutItemRequest(tableName, reply);
		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
		// System.out.println("Result: " + putItemResult);

		dynamoDB.shutdown();
	}

	public void upVote() {
		this.voting++;
		this.updateVoting();
	}

	public void downVote() {
		this.voting--;
		this.updateVoting();
	}

	public void updateVoting() {
		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("topic+thread+floor",
				new AttributeValue().withS(this.topic_name + "+"
						+ Integer.toString(this.thread_id) + "+"
						+ Integer.toString(this.floor)));

		Map<String, AttributeValueUpdate> update = new HashMap<String, AttributeValueUpdate>();
		update.put(
				"voting",
				new AttributeValueUpdate().withAction(AttributeAction.PUT)
						.withValue(
								new AttributeValue(Integer
										.toString(this.voting))));

		UpdateItemRequest updateItemRequest = new UpdateItemRequest()
				.withTableName(tableName).withKey(key)
				.withAttributeUpdates(update);
		UpdateItemResult updateItemResult = dynamoDB
				.updateItem(updateItemRequest);
		dynamoDB.shutdown();
	}

	private static Map<String, AttributeValue> newReply(ThreadReply tr) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put(
				"topic+thread+floor",
				new AttributeValue(tr.topic_name + "+"
						+ Integer.toString(tr.thread_id) + "+"
						+ Integer.toString(tr.floor)));
		item.put("date", new AttributeValue(Long.toString(tr.date)));
		item.put("user_created", new AttributeValue(tr.user_created));
		item.put("topic_name", new AttributeValue(tr.topic_name));
		item.put("thread_id",
				new AttributeValue(Integer.toString(tr.thread_id)));
		item.put("floor", new AttributeValue(Integer.toString(tr.floor)));
		item.put("reply_to", new AttributeValue(Integer.toString(tr.replyTo)));
		item.put("text", new AttributeValue(tr.text));
		item.put("voting", new AttributeValue(Integer.toString(tr.voting)));

		return item;
	}
}
