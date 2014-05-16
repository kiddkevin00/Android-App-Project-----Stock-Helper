//package com.stockcloud;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.locks.Condition;
//
//import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
//import com.amazonaws.regions.Region;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.cloudwatch.model.ComparisonOperator;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
//import com.amazonaws.services.dynamodbv2.model.AttributeAction;
//import com.amazonaws.services.dynamodbv2.model.AttributeValue;
//import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
//import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
//import com.amazonaws.services.dynamodbv2.model.PutItemResult;
//import com.amazonaws.services.dynamodbv2.model.ScanRequest;
//import com.amazonaws.services.dynamodbv2.model.ScanResult;
//import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
//import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
//
////##note
////"chat topic" = stock name
////"thread" = topic under stock
////"reply" under topic
//
//public class ChatTopic {
//	protected String topicStockCodeName; // stock name
//	protected int thread_count; // number of threads(topic)
//
//	AmazonDynamoDBClient dynamoDB;
//	final String tableName = "stock-chat-topics";
//	final String tableName2 = "stock-threads";
//
//	public ChatTopic() {
//		// TODO Auto-generated constructor stub
//		this.thread_count = 0;
//	}
//
//	public ChatTopic(String tscn) {
//		if (findChatTopic(tscn) == null) {
//			this.topicStockCodeName = tscn;
//			this.thread_count = 0;
//			this.saveToDB();
//		} else
//			System.out.println("Record existed!");
//	}
//
//	public ChatTopic findChatTopic(String tscn) {
//		dynamoDB = new AmazonDynamoDBClient(
//				new ClasspathPropertiesFileCredentialsProvider());
//		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		dynamoDB.setRegion(usWest2);
//
//		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
//		Condition condition = new Condition().withComparisonOperator(
//				ComparisonOperator.EQ.toString()).withAttributeValueList(
//				new AttributeValue().withS(tscn));
//		scanFilter.put("topicStockCodeName", condition);
//		ScanRequest scanRequest = new ScanRequest(tableName)
//				.withScanFilter(scanFilter);
//		ScanResult scanResult = dynamoDB.scan(scanRequest);
//		// System.out.println("Scan Result: " + scanResult);
//		dynamoDB.shutdown();
//		if (scanResult.getCount() == 0)
//			return null;
//		if (scanResult.getCount() > 1)
//			return null;
//
//		Map<String, AttributeValue> targetRecord = scanResult.getItems().get(0);
//		ChatTopic target = new ChatTopic(targetRecord.get("topicStockCodeName")
//				.getS());
//		target.thread_count = Integer.parseInt(targetRecord.get(
//				"umber_of_threads").getS());
//
//		return target;
//	}
//
//	private int numberOfThreads(String tscn) {
//		dynamoDB = new AmazonDynamoDBClient(
//				new ClasspathPropertiesFileCredentialsProvider());
//		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		dynamoDB.setRegion(usWest2);
//
//		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
//		Condition condition = new Condition().withComparisonOperator(
//				ComparisonOperator.EQ.toString()).withAttributeValueList(
//				new AttributeValue().withS(tscn));
//		scanFilter.put("topicStockCodeName", condition);
//		ScanRequest scanRequest = new ScanRequest(tableName)
//				.withScanFilter(scanFilter);
//		ScanResult scanResult = dynamoDB.scan(scanRequest);
//		// System.out.println("Scan Result: " + scanResult);
//		dynamoDB.shutdown();
//		if (scanResult.getCount() == 0)
//			return -1;
//		if (scanResult.getCount() > 1)
//			return -1;
//
//		Map<String, AttributeValue> targetRecord = scanResult.getItems().get(0);
//		int number_of_threads = Integer.parseInt(targetRecord.get(
//				"number_of_threads").getS());
//
//		return number_of_threads;
//	}
//
//	public ThreadBody addThreadToTopic(String text, String user_email) {
//		ThreadBody tb = new ThreadBody();
//		tb.text = text;
//		tb.user_created = user_email;
//		tb.topic_name = this.topicStockCodeName;
//		this.thread_count = numberOfThreads(this.topicStockCodeName);
//		tb.thread_id = ++this.thread_count;
//
//		tb.saveToDB();
//		this.updateThreads();
//		return tb;
//	}
//
//	public void print() {
//		System.out.println(this.topicStockCodeName);
//		System.out.println(this.thread_count);
//	}
//
//	public void saveToDB() {
//		dynamoDB = new AmazonDynamoDBClient(
//				new ClasspathPropertiesFileCredentialsProvider());
//		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		dynamoDB.setRegion(usWest2);
//
//		Map<String, AttributeValue> topic = newTopic(this);
//		PutItemRequest putItemRequest = new PutItemRequest(tableName, topic);
//		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
//		// System.out.println("Result: " + putItemResult);
//
//		dynamoDB.shutdown();
//	}
//
//	public void updateThreads() {
//		dynamoDB = new AmazonDynamoDBClient(
//				new ClasspathPropertiesFileCredentialsProvider());
//		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		dynamoDB.setRegion(usWest2);
//
//		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
//		key.put("topicStockCodeName",
//				new AttributeValue().withS(this.topicStockCodeName));
//
//		Map<String, AttributeValueUpdate> update = new HashMap<String, AttributeValueUpdate>();
//		update.put(
//				"number_of_threads",
//				new AttributeValueUpdate().withAction(AttributeAction.PUT)
//						.withValue(
//								new AttributeValue(Integer
//										.toString(this.thread_count))));
//
//		UpdateItemRequest updateItemRequest = new UpdateItemRequest()
//				.withTableName(tableName).withKey(key)
//				.withAttributeUpdates(update);
//		UpdateItemResult updateItemResult = dynamoDB
//				.updateItem(updateItemRequest);
//		dynamoDB.shutdown();
//	}
//
//	private static Map<String, AttributeValue> newTopic(ChatTopic ct) {
//
//		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
//		item.put("topicStockCodeName",
//				new AttributeValue(ct.topicStockCodeName));
//		item.put("number_of_threads",
//				new AttributeValue(Integer.toString(ct.thread_count)));
//
//		return item;
//	}
//
//	public List<ThreadBody> listThreads() {
//		List<ThreadBody> thread_list = new ArrayList<ThreadBody>();
//		dynamoDB = new AmazonDynamoDBClient(
//				new ClasspathPropertiesFileCredentialsProvider());
//		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		dynamoDB.setRegion(usWest2);
//
//		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
//		Condition condition = new Condition().withComparisonOperator(
//				ComparisonOperator.EQ.toString()).withAttributeValueList(
//				new AttributeValue().withS(this.topicStockCodeName));
//		scanFilter.put("topic_name", condition);
//		ScanRequest scanRequest = new ScanRequest(tableName2)
//				.withScanFilter(scanFilter);
//		ScanResult scanResult = dynamoDB.scan(scanRequest);
//		// System.out.println("Scan Result: " + scanResult);
//
//		List<Map<String, AttributeValue>> result = scanResult.getItems();
//		for (Map<String, AttributeValue> thread : result) {
//			ThreadBody this_thread = new ThreadBody();
//			this_thread.date = Long.parseLong(thread.get("date").getS());
//			this_thread.text = thread.get("text").getS();
//			this_thread.thread_id = Integer.parseInt(thread.get("thread_id")
//					.getS());
//			this_thread.user_created = thread.get("user_created").getS();
//			this_thread.topic_name = thread.get("topic_name").getS();
//			this_thread.reply_count = Integer.parseInt(thread.get(
//					"number_of_replies").getS());
//			thread_list.add(this_thread);
//		}
//
//		dynamoDB.shutdown();
//		if (thread_list.size() == 0)
//			return null;
//		return sortThreads(thread_list);
//	}
//
//	private List<ThreadBody> sortThreads(List<ThreadBody> threads) {
//		Collections.sort(threads, new Comparator<ThreadBody>() {
//
//			@Override
//			public int compare(ThreadBody o1, ThreadBody o2) {
//				// TODO Auto-generated method stub
//				return o2.thread_id - o1.thread_id;
//			}
//
//		});
//		return threads;
//	}
//}
//
//class ThreadBody {
//	// protected String title; //show in thread list of a stock
//	protected String text; // show on top of chat room (detail)
//	protected long date; // date created (no show)
//	protected int thread_id; // primary key of chat room table
//	protected final int floor = 0; // no need
//	protected String user_created; // poster
//	protected int reply_count;
//	protected String topic_name; // under which stock name
//
//	AmazonDynamoDBClient dynamoDB;
//	final String tableName = "stock-threads";
//	final String tableName2 = "stock-replies";
//
//	public ThreadBody(String t, String u, int id, String topic_name) {
//		this.text = t;
//		this.user_created = u;
//		this.thread_id = id;
//		this.topic_name = topic_name;
//		this.reply_count = 0;
//	}
//
//	public ThreadBody() {
//		// TODO Auto-generated constructor stub
//		this.reply_count = 0;
//
//		Date d = new Date();
//		this.date = d.getTime();
//	}
//
//	public int number_of_replies(String tscn, int thread_id) {
//		dynamoDB = new AmazonDynamoDBClient(
//				new ClasspathPropertiesFileCredentialsProvider());
//		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		dynamoDB.setRegion(usWest2);
//
//		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
//		Condition condition1 = new Condition().withComparisonOperator(
//				ComparisonOperator.EQ.toString()).withAttributeValueList(
//				new AttributeValue().withS(tscn));
//		Condition condition2 = new Condition().withComparisonOperator(
//				ComparisonOperator.EQ.toString()).withAttributeValueList(
//				new AttributeValue().withS(Integer.toString(thread_id)));
//		scanFilter.put("topic_name", condition1);
//		scanFilter.put("thread_id", condition2);
//		ScanRequest scanRequest = new ScanRequest(tableName)
//				.withScanFilter(scanFilter);
//		ScanResult scanResult = dynamoDB.scan(scanRequest);
//		// System.out.println("Scan Result: " + scanResult);
//		dynamoDB.shutdown();
//		if (scanResult.getCount() == 0)
//			return -1;
//		if (scanResult.getCount() > 1)
//			return -1;
//
//		Map<String, AttributeValue> targetRecord = scanResult.getItems().get(0);
//		int reply_count = Integer.parseInt(targetRecord
//				.get("number_of_replies").getS());
//
//		return reply_count;
//	}
//
//	public ThreadReply addReplyToThread(String text, String user_email,
//			int target_floor) {
//		ThreadReply tr = new ThreadReply();
//
//		tr.text = text;
//		tr.user_created = user_email;
//		tr.thread_id = this.thread_id;
//		tr.replyTo = target_floor;
//		tr.topic_name = this.topic_name;
//		this.reply_count = number_of_replies(this.topic_name, this.thread_id);
//
//		tr.floor = ++this.reply_count;
//
//		tr.saveToDB();
//		this.updateReplies();
//		return tr;
//	}
//
//	public void print() {
//		System.out.println(this.date);
//		System.out.println(this.user_created);
//		System.out.println(this.topic_name);
//		System.out.println(this.thread_id);
//		System.out.println(this.floor);
//		System.out.println(this.text);
//		System.out.println(this.reply_count);
//	}
//
//	public void saveToDB() {
//		dynamoDB = new AmazonDynamoDBClient(
//				new ClasspathPropertiesFileCredentialsProvider());
//		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		dynamoDB.setRegion(usWest2);
//
//		Map<String, AttributeValue> thread = newThread(this);
//		PutItemRequest putItemRequest = new PutItemRequest(tableName, thread);
//		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
//		// System.out.println("Result: " + putItemResult);
//
//		dynamoDB.shutdown();
//	}
//
//	public void updateReplies() {
//		dynamoDB = new AmazonDynamoDBClient(
//				new ClasspathPropertiesFileCredentialsProvider());
//		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		dynamoDB.setRegion(usWest2);
//
//		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
//		key.put("topic+thread",
//				new AttributeValue().withS(this.topic_name + "+"
//						+ Integer.toString(this.thread_id)));
//
//		Map<String, AttributeValueUpdate> update = new HashMap<String, AttributeValueUpdate>();
//		update.put(
//				"number_of_replies",
//				new AttributeValueUpdate().withAction(AttributeAction.PUT)
//						.withValue(
//								new AttributeValue(Integer
//										.toString(this.reply_count))));
//
//		UpdateItemRequest updateItemRequest = new UpdateItemRequest()
//				.withTableName(tableName).withKey(key)
//				.withAttributeUpdates(update);
//		UpdateItemResult updateItemResult = dynamoDB
//				.updateItem(updateItemRequest);
//		dynamoDB.shutdown();
//	}
//
//	private static Map<String, AttributeValue> newThread(ThreadBody tb) {
//		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
//		item.put("topic+thread", new AttributeValue(tb.topic_name + "+"
//				+ Integer.toString(tb.thread_id)));
//		item.put("date", new AttributeValue(Long.toString(tb.date)));
//		item.put("user_created", new AttributeValue(tb.user_created));
//		item.put("topic_name", new AttributeValue(tb.topic_name));
//		item.put("thread_id",
//				new AttributeValue(Integer.toString(tb.thread_id)));
//		item.put("floor", new AttributeValue(Integer.toString(tb.floor)));
//		item.put("text", new AttributeValue(tb.text));
//		item.put("number_of_replies",
//				new AttributeValue(Integer.toString(tb.reply_count)));
//
//		return item;
//	}
//
//	public List<ThreadReply> listReplies() {
//		List<ThreadReply> replies = new ArrayList<ThreadReply>();
//
//		dynamoDB = new AmazonDynamoDBClient(
//				new ClasspathPropertiesFileCredentialsProvider());
//		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		dynamoDB.setRegion(usWest2);
//
//		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
//		Condition condition1 = new Condition().withComparisonOperator(
//				ComparisonOperator.EQ.toString()).withAttributeValueList(
//				new AttributeValue().withS(this.topic_name));
//		scanFilter.put("topic_name", condition1);
//		Condition condition2 = new Condition().withComparisonOperator(
//				ComparisonOperator.EQ.toString()).withAttributeValueList(
//				new AttributeValue().withS(Integer.toString(this.thread_id)));
//		scanFilter.put("thread_id", condition2);
//
//		ScanRequest scanRequest = new ScanRequest(tableName2)
//				.withScanFilter(scanFilter);
//		ScanResult scanResult = dynamoDB.scan(scanRequest);
//		// System.out.println("Scan Result: " + scanResult);
//
//		List<Map<String, AttributeValue>> result = scanResult.getItems();
//		for (Map<String, AttributeValue> reply : result) {
//			ThreadReply this_reply = new ThreadReply();
//			this_reply.text = reply.get("text").getS();
//			this_reply.floor = Integer.parseInt(reply.get("floor").getS());
//			this_reply.replyTo = Integer.parseInt(reply.get("reply_to").getS());
//			this_reply.voting = Integer.parseInt(reply.get("voting").getS());
//			this_reply.topic_name = reply.get("topic_name").getS();
//			this_reply.thread_id = Integer.parseInt(reply.get("thread_id")
//					.getS());
//			this_reply.user_created = reply.get("user_created").getS();
//			this_reply.date = Long.parseLong(reply.get("date").getS());
//
//			replies.add(this_reply);
//		}
//
//		if (replies.size() == 0)
//			return null;
//		return sortReplies(replies);
//	}
//
//	private List<ThreadReply> sortReplies(List<ThreadReply> replies) {
//		Collections.sort(replies, new Comparator<ThreadReply>() {
//
//			@Override
//			public int compare(ThreadReply o1, ThreadReply o2) {
//				// TODO Auto-generated method stub
//				if (o1.voting != o2.voting)
//					return o2.voting - o1.voting;
//				else
//					return o1.floor - o2.floor;
//			}
//
//		});
//		return replies;
//	}
//}
//
//// per message
//class ThreadReply {
//	protected String text; // message
//	protected int floor; // the #th of the reply
//	protected int replyTo; // the target of each message to which floor
//	protected int voting; // number
//	protected String user_created;
//	protected long date;
//	protected int thread_id; // no comfuse with title
//	protected String topic_name; // stock name
//
//	AmazonDynamoDBClient dynamoDB;
//	final String tableName = "stock-replies";
//
//	public ThreadReply(String t, String u, int thread_id, int f, int rt,
//			String topic_name) {
//		this.text = t;
//		this.floor = f;
//		this.replyTo = rt;
//		this.voting = 0;
//		this.user_created = u;
//		this.thread_id = thread_id;
//		this.topic_name = topic_name;
//
//		Date d = new Date();
//		this.date = d.getTime();
//	}
//
//	public ThreadReply() {
//		// TODO Auto-generated constructor stub
//		this.voting = 0;
//
//		Date d = new Date();
//		this.date = d.getTime();
//	}
//
//	public void print() {
//		System.out.println(this.date);
//		System.out.println(this.user_created);
//		System.out.println(this.topic_name);
//		System.out.println(this.thread_id);
//		System.out.println(this.floor);
//		System.out.println(this.replyTo);
//		System.out.println(this.text);
//		System.out.println(this.voting);
//	}
//
//	public void saveToDB() {
//		dynamoDB = new AmazonDynamoDBClient(
//				new ClasspathPropertiesFileCredentialsProvider());
//		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		dynamoDB.setRegion(usWest2);
//
//		Map<String, AttributeValue> reply = newReply(this);
//		PutItemRequest putItemRequest = new PutItemRequest(tableName, reply);
//		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
//		// System.out.println("Result: " + putItemResult);
//
//		dynamoDB.shutdown();
//	}
//
//	public void upVote() {
//		this.voting++;
//		this.updateVoting();
//	}
//
//	public void downVote() {
//		this.voting--;
//		this.updateVoting();
//	}
//
//	public void updateVoting() {
//		dynamoDB = new AmazonDynamoDBClient(
//				new ClasspathPropertiesFileCredentialsProvider());
//		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
//		dynamoDB.setRegion(usWest2);
//
//		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
//		key.put("topic+thread+floor",
//				new AttributeValue().withS(this.topic_name + "+"
//						+ Integer.toString(this.thread_id) + "+"
//						+ Integer.toString(this.floor)));
//
//		Map<String, AttributeValueUpdate> update = new HashMap<String, AttributeValueUpdate>();
//		update.put(
//				"voting",
//				new AttributeValueUpdate().withAction(AttributeAction.PUT)
//						.withValue(
//								new AttributeValue(Integer
//										.toString(this.voting))));
//
//		UpdateItemRequest updateItemRequest = new UpdateItemRequest()
//				.withTableName(tableName).withKey(key)
//				.withAttributeUpdates(update);
//		UpdateItemResult updateItemResult = dynamoDB
//				.updateItem(updateItemRequest);
//		dynamoDB.shutdown();
//	}
//
//	private static Map<String, AttributeValue> newReply(ThreadReply tr) {
//		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
//		item.put(
//				"topic+thread+floor",
//				new AttributeValue(tr.topic_name + "+"
//						+ Integer.toString(tr.thread_id) + "+"
//						+ Integer.toString(tr.floor)));
//		item.put("date", new AttributeValue(Long.toString(tr.date)));
//		item.put("user_created", new AttributeValue(tr.user_created));
//		item.put("topic_name", new AttributeValue(tr.topic_name));
//		item.put("thread_id",
//				new AttributeValue(Integer.toString(tr.thread_id)));
//		item.put("floor", new AttributeValue(Integer.toString(tr.floor)));
//		item.put("reply_to", new AttributeValue(Integer.toString(tr.replyTo)));
//		item.put("text", new AttributeValue(tr.text));
//		item.put("voting", new AttributeValue(Integer.toString(tr.voting)));
//
//		return item;
//	}
// }
