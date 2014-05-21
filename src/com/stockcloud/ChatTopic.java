package com.stockcloud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public class ChatTopic {
	public String topicStockCodeName;
	public int thread_count;

	AmazonDynamoDBClient dynamoDB;
	final String tableName = "stock-chat-topics";
	final String tableName2 = "stock-threads";

	public ChatTopic() {
		// TODO Auto-generated constructor stub
		this.thread_count = 0;
	}

	public ChatTopic(String tscn) {
		this.topicStockCodeName = tscn;
		this.thread_count = 0;
		this.saveToDB();
		System.out.println("Topic Created!");
	}

	public void commit() {
		if (findChatTopic(this.topicStockCodeName) == null) {
			this.saveToDB();
			System.out.println("Topic Saved!");
		} else
			System.out.println("Record existed!");
	}

	public ChatTopic findChatTopic(String tscn) {
		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		Condition condition = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(tscn));
		scanFilter.put("topicStockCodeName", condition);
		ScanRequest scanRequest = new ScanRequest(tableName)
				.withScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);
		// System.out.println("Scan Result: " + scanResult);
		dynamoDB.shutdown();

		if (scanResult.getCount() == 0)
			return null;
		if (scanResult.getCount() > 1)
			return null;

		Map<String, AttributeValue> targetRecord = scanResult.getItems().get(0);
		ChatTopic target = new ChatTopic();
		target.topicStockCodeName = targetRecord.get("topicStockCodeName")
				.getS();
		target.thread_count = Integer.parseInt(targetRecord.get(
				"number_of_threads").getS());

		return target;
	}

	private int numberOfThreads(String tscn) {
		ChatTopic targetTopic = findChatTopic(tscn);
		System.out.println("Current Number of threads: "
				+ targetTopic.thread_count);
		if (targetTopic == null)
			return -1;
		else
			return targetTopic.thread_count;
	}

	public ThreadBody addThreadToTopic(String title, String text,
			String user_email) {
		System.out.println("Adding thread!");
		ThreadBody tb = new ThreadBody();
		tb.title = title;
		tb.text = text;
		tb.user_created = user_email;
		tb.topic_name = this.topicStockCodeName;
		this.thread_count = numberOfThreads(this.topicStockCodeName);
		tb.thread_id = ++this.thread_count;

		tb.saveToDB();
		this.updateThreads();

		System.out.println("Thread added!");
		return tb;

	}

	public void print() {
		System.out.println(this.topicStockCodeName);
		System.out.println(this.thread_count);
	}

	public void saveToDB() {
		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		Map<String, AttributeValue> topic = newTopic(this);
		PutItemRequest putItemRequest = new PutItemRequest(tableName, topic);
		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
		// System.out.println("Result: " + putItemResult);

		dynamoDB.shutdown();
	}

	public void updateThreads() {
		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("topicStockCodeName",
				new AttributeValue().withS(this.topicStockCodeName));

		Map<String, AttributeValueUpdate> update = new HashMap<String, AttributeValueUpdate>();
		update.put(
				"number_of_threads",
				new AttributeValueUpdate().withAction(AttributeAction.PUT)
						.withValue(
								new AttributeValue(Integer
										.toString(this.thread_count))));

		UpdateItemRequest updateItemRequest = new UpdateItemRequest()
				.withTableName(tableName).withKey(key)
				.withAttributeUpdates(update);
		UpdateItemResult updateItemResult = dynamoDB
				.updateItem(updateItemRequest);
		dynamoDB.shutdown();
	}

	private static Map<String, AttributeValue> newTopic(ChatTopic ct) {

		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("topicStockCodeName",
				new AttributeValue(ct.topicStockCodeName));
		item.put("number_of_threads",
				new AttributeValue(Integer.toString(ct.thread_count)));

		return item;
	}

	public List<ThreadBody> listThreads() {
		List<ThreadBody> thread_list = new ArrayList<ThreadBody>();
		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		Condition condition = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(this.topicStockCodeName));
		scanFilter.put("topic_name", condition);
		ScanRequest scanRequest = new ScanRequest(tableName2)
				.withScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);
		// System.out.println("Scan Result: " + scanResult);

		List<Map<String, AttributeValue>> result = scanResult.getItems();
		for (Map<String, AttributeValue> thread : result) {
			ThreadBody this_thread = new ThreadBody();
			this_thread.title = thread.get("title").getS();
			this_thread.date = Long.parseLong(thread.get("date").getS());
			this_thread.text = thread.get("text").getS();
			this_thread.thread_id = Integer.parseInt(thread.get("thread_id")
					.getS());
			this_thread.user_created = thread.get("user_created").getS();
			this_thread.topic_name = thread.get("topic_name").getS();
			this_thread.reply_count = Integer.parseInt(thread.get(
					"number_of_replies").getS());
			thread_list.add(this_thread);
		}

		dynamoDB.shutdown();
		if (thread_list.size() == 0)
			return null;
		return sortThreads(thread_list);
	}

	private List<ThreadBody> sortThreads(List<ThreadBody> threads) {
		Collections.sort(threads, new Comparator<ThreadBody>() {

			@Override
			public int compare(ThreadBody o1, ThreadBody o2) {
				// TODO Auto-generated method stub
				return o2.thread_id - o1.thread_id;
			}

		});
		return threads;
	}
}
