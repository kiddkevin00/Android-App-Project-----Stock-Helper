package com.stockcloud;

/*
 * Copyright 2012-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;

/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class updatestock {

	/*
	 * Important: Be sure to fill in your AWS access credentials in the
	 * AwsCredentials.properties file before you try to run this sample.
	 * http://aws.amazon.com/security-credentials
	 */

	static AmazonDynamoDBClient dynamoDB;

	/**
	 * The only information needed to create a client are security credentials
	 * consisting of the AWS Access Key ID and Secret Access Key. All other
	 * configuration, such as the service endpoints, are performed
	 * automatically. Client parameters, such as proxies, can be specified in an
	 * optional ClientConfiguration object when constructing a client.
	 * 
	 * @see com.amazonaws.auth.BasicAWSCredentials
	 * @see com.amazonaws.auth.PropertiesCredentials
	 * @see com.amazonaws.ClientConfiguration
	 */
	private static void init() throws Exception {
		/*
		 * This credentials provider implementation loads your AWS credentials
		 * from a properties file at the root of your classpath.
		 */
		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);

	}

	public void update() throws Exception {
		init();
		String csvString;
		URL url = null;
		URLConnection urlConn = null;
		InputStreamReader inStream = null;
		BufferedReader buff = null;
		// static AmazonDynamoDBClient dynamoDB;
		// String jb=symbol.substring(0, 3)+"+"+symbol.substring(3, 7);
		// System.out.println(jb);
		// String[] newsymbol=symbol.split(",");
		int i = 0;
		String symbol[] = { "ABT", "ABBV", "ACE", "ACN", "ACT", "ADBE", "ADT",
				"AET", "AFL", "ARG", "AA", "ALL", "ALTR", "AMZN", "AAPL",
				"BLL", "BAC", "BMS", "BBY", "BLK", "BSX", "CA", "COF", "CCL",
				"CAT", "CERN", "CVX", "CSCO", "COH", "KO", "COST", "DVN",
				"DTV", "DG", "DOV", "DOW", "DUK", "EBAY", "EOG", "EMR", "EFX",
				"EQR", "EXPE", "ESRX", "FB", "FE", "FLIR", "FMC", "F", "FOSL",
				"GME", "GPS", "GD", "GE", "GIS", "GS", "GOOG", "HOG", "HRS",
				"HCP", "HAS", "HES", "HD", "HST", "HUM", "ITW", "INTC", "ICE",
				"IBM", "INTU", "IVZ", "JOY", "JNPR", "KEY", "KMI", "KLAC",
				"KSS", "KR", "LB", "LRCX", "LM", "LEN", "LNC", "LOW", "MAC",
				"MMM", "M", "MAR", "MA", "MCD", "MRK", "MET", "MU", "MSFT",
				"MNST", "MS", "MOS", "MUR", "MYL" };
		// String
		// Num[]={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","3","2","3","4","5","6","7","8","9","10","1","2","3","4","5","6","7","8","9","10","1","2","3","4","5","6","7","8","9","10","1","2","3","4","5","6","7","8","9","10","1","2","3","4","5","6","7","8","9","10","1","2","3","4","5","6","7","8","9","10",}
		// loop to get the current data of every stocks(100)
		while (i < symbol.length) {
			url = new URL("http://quote.yahoo.com/d/quotes.csv?s=" + symbol[i]
					+ "&f=osl1d1t1c1ohgv&e=.csv");
			urlConn = url.openConnection();
			inStream = new InputStreamReader(urlConn.getInputStream());
			buff = new BufferedReader(inStream);
			// get the quote as a csv string
			csvString = buff.readLine();
			// while(csvString!=null)

			// {
			// parse the csv string
			StringTokenizer tokenizer = new StringTokenizer(csvString, ",");
			String open = tokenizer.nextToken();
			String ticker = tokenizer.nextToken();
			String price = tokenizer.nextToken();
			String tradeDate = tokenizer.nextToken();
			String tradeTime = tokenizer.nextToken();

			System.out.println("Symbol: " + ticker + " Price: " + price
					+ " Date: " + tradeDate + " Time: " + tradeTime);

			// ���������������������������table
			try {
				String tableName = symbol[i] + "current";

				// Create table if it does not exist yet
				if (Tables.doesTableExist(dynamoDB, tableName)) {
					System.out.println("Table " + tableName
							+ " is already ACTIVE");
				} else {
					// Create a table with a primary hash key named 'name',
					// which holds a string
					CreateTableRequest createTableRequest = new CreateTableRequest()
							.withTableName(tableName)
							.withKeySchema(
									new KeySchemaElement().withAttributeName(
											"Time").withKeyType(KeyType.HASH))
							.withAttributeDefinitions(
									new AttributeDefinition()
											.withAttributeName("Time")
											.withAttributeType(
													ScalarAttributeType.S))
							.withProvisionedThroughput(
									new ProvisionedThroughput()
											.withReadCapacityUnits(1L)
											.withWriteCapacityUnits(1L));
					TableDescription createdTableDescription = dynamoDB
							.createTable(createTableRequest)
							.getTableDescription();
					System.out.println("Created Table: "
							+ createdTableDescription);

					// Wait for it to become active
					System.out.println("Waiting for " + tableName
							+ " to become ACTIVE...");
					Tables.waitForTableToBecomeActive(dynamoDB, tableName);
				}
				Map<String, AttributeValue> item = newItem(ticker, price,
						tradeDate, tradeTime, open);
				PutItemRequest putItemRequest = new PutItemRequest(tableName,
						item);
				PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
				System.out.println("Result: " + putItemResult);

			}

			catch (AmazonServiceException ase) {
				System.out
						.println("Caught an AmazonServiceException, which means your request made it "
								+ "to AWS, but was rejected with an error response for some reason.");
				System.out.println("Error Message:    " + ase.getMessage());
				System.out.println("HTTP Status Code: " + ase.getStatusCode());
				System.out.println("AWS Error Code:   " + ase.getErrorCode());
				System.out.println("Error Type:       " + ase.getErrorType());
				System.out.println("Request ID:       " + ase.getRequestId());
			} catch (AmazonClientException ace) {
				System.out
						.println("Caught an AmazonClientException, which means the client encountered "
								+ "a serious internal problem while trying to communicate with AWS, "
								+ "such as not being able to access the network.");
				System.out.println("Error Message: " + ace.getMessage());
			}// try over

			i++;
		}// whileover

		inStream.close();
		buff.close();

	}// main over

	private static Map<String, AttributeValue> newItem(String ticker,
			String price, String tradeDate, String tradeTime, String open) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("ticker", new AttributeValue(ticker));
		item.put("price", new AttributeValue(price));
		item.put("tradeDate", new AttributeValue(tradeDate));
		item.put("Time", new AttributeValue(tradeTime));
		item.put("open", new AttributeValue(open));
		// item.put("No", new AttributeValue(Num));

		return item;
	}// Map function over

}// class over
