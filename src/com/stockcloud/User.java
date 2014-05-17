package com.stockcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

public class User {
	public String email;
	public String userID;
	public int passwdHash;
	public List<String> favorates;

	AmazonDynamoDBClient dynamoDB;
	final String tableName = "stock-users";

	public User(String userid, String em, String pw) throws Exception {
		this.userID = userid;
		this.email = em;
		this.passwdHash = pw.hashCode();
		this.favorates = new ArrayList<String>();
		this.addFavorite("padding");
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public void commit() throws Exception {
		if (this.isUserExist(this.email) == null)
			this.saveToDB();
	}

	public boolean changeID(String newid, String pw) throws Exception {
		if (pw.hashCode() == this.passwdHash) {
			this.userID = newid;
			this.updateUser();
			return true;
		} else {
			return false;
		}
	}

	public boolean changePassword(String newpw, String pw) throws Exception {
		if (pw.hashCode() == this.passwdHash) {
			this.passwdHash = newpw.hashCode();
			this.updateUser();
			return true;
		} else {
			return false;
		}
	}

	public List<String> addFavorite(String newStock) throws Exception {
		if (!this.favorates.contains(newStock)) {
			this.favorates.add(newStock);
			this.updateUser();
		}
		return this.favorates;
	}

	public List<String> deleteFavorite(String oldStock) throws Exception {
		if (this.favorates.contains(oldStock)) {
			this.favorates.remove(oldStock);
			this.updateUser();
		}
		return this.favorates;
	}

	public void saveToDB() throws Exception {

		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		Map<String, AttributeValue> user = newUser(this);
		PutItemRequest putItemRequest = new PutItemRequest(tableName, user);
		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
		System.out.println("Result: " + putItemResult);

		dynamoDB.shutdown();

	}

	public Map<String, AttributeValue> isUserExist(String email) {

		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		Condition condition = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(email));
		scanFilter.put("email", condition);
		ScanRequest scanRequest = new ScanRequest(tableName)
				.withScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		dynamoDB.shutdown();
		if (scanResult.getCount() != 1)
			return null;
		else
			return scanResult.getItems().get(0);
	}

	public User retrieveUser(String email, String pw) {
		Map<String, AttributeValue> intended_user = isUserExist(email);
		if (intended_user == null)
			return null;
		else {
			int target_Hash = Integer.parseInt(intended_user
					.get("passwordHash").getS());
			if (pw.hashCode() != target_Hash)
				return null;

			User retrievedUser = new User();
			retrievedUser.email = intended_user.get("email").getS();
			retrievedUser.passwdHash = target_Hash;
			retrievedUser.userID = intended_user.get("userID").getS();
			retrievedUser.favorates = intended_user.get("favorates").getSS();
			return retrievedUser;
		}
	}

	public List<String> listFavorites() {
		List<String> stocks = this.favorates;
		stocks.remove("padding");
		return stocks;
	}

	private static Map<String, AttributeValue> newUser(User nu) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("userID", new AttributeValue(nu.userID));
		item.put("email", new AttributeValue(nu.email));
		item.put("passwordHash",
				new AttributeValue(Integer.toString(nu.passwdHash)));
		item.put("favorates", new AttributeValue(nu.favorates));

		/*
		 * Date date = new Date(); item.put("timeCreated", new
		 * AttributeValue(Long.toString(date.getTime())));
		 */
		return item;
	}

	public void deleteUserFromDB(String em) throws AmazonClientException,
			AmazonServiceException {
		AmazonDynamoDBClient dynamoDB;
		final String tableName = "stock-users";

		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("email", new AttributeValue().withS(em));
		DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
				.withTableName(tableName).withKey(key);
		DeleteItemResult deleteItemResult = dynamoDB
				.deleteItem(deleteItemRequest);
		System.out.println("DeleteResult: " + deleteItemResult);

		dynamoDB.shutdown();
	}

	public void updateUser() throws Exception {
		AmazonDynamoDBClient dynamoDB;
		final String tableName = "stock-users";

		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("email", new AttributeValue().withS(this.email));

		Map<String, AttributeValueUpdate> update = new HashMap<String, AttributeValueUpdate>();
		update.put("userID",
				new AttributeValueUpdate().withAction(AttributeAction.PUT)
						.withValue(new AttributeValue(this.userID)));
		update.put(
				"passwordHash",
				new AttributeValueUpdate().withAction(AttributeAction.PUT)
						.withValue(
								new AttributeValue(Integer
										.toString(this.passwdHash))));
		update.put("favorates",
				new AttributeValueUpdate().withAction(AttributeAction.PUT)
						.withValue(new AttributeValue(this.favorates)));

		UpdateItemRequest updateItemRequest = new UpdateItemRequest()
				.withTableName(tableName).withKey(key)
				.withAttributeUpdates(update);
		UpdateItemResult updateItemResult = dynamoDB
				.updateItem(updateItemRequest);
		dynamoDB.shutdown();
	}

	static public User searchUser(String email) throws Exception {
		AmazonDynamoDBClient dynamoDB;
		final String tableName = "stock-users";

		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		Condition condition = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(email));
		scanFilter.put("email", condition);
		ScanRequest scanRequest = new ScanRequest(tableName)
				.withScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		if (scanResult.getCount() == 1) {
			Map<String, AttributeValue> userFiltered = scanResult.getItems()
					.get(0);
			User user = new User();
			user.userID = userFiltered.get("userID").getS();
			user.email = userFiltered.get("email").getS();
			user.passwdHash = Integer.parseInt(userFiltered.get("passwordHash")
					.getS());
			user.favorates = userFiltered.get("favorates").getSS();

			return user;
		} else
			return null;

	}

}
