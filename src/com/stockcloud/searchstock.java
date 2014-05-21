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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.util.Tables;

/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class searchstock {

	/*
	 * Before running the code: Fill in your AWS access credentials in the
	 * provided credentials file template, and be sure to move the file to the
	 * default location (~/.aws/credentials) where the sample code will load the
	 * credentials from.
	 * https://console.aws.amazon.com/iam/home?#security_credential
	 * 
	 * WANRNING: To avoid accidental leakage of your credentials, DO NOT keep
	 * the credentials file in your source directory.
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
	 * @see com.amazonaws.auth.ProfilesConfigFile
	 * @see com.amazonaws.ClientConfiguration
	 */
	private static void init() throws Exception {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential
		 * profile by reading from the credentials file located at
		 * (~/.aws/credentials).
		 */
		AWSCredentials credentials = null;
		dynamoDB = new AmazonDynamoDBClient(
				new ClasspathPropertiesFileCredentialsProvider());
		// Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		// dynamoDB.setRegion(usWest2);
	}

	public List<String> search(String args) throws Exception {
		init();

		try {

			String tableName = args + "current";

			// check table if it does not exist yet
			if (Tables.doesTableExist(dynamoDB, tableName)) {
				System.out.println("Table " + tableName + " is already ACTIVE");
			}

			// Scan items
			ScanRequest scanRequest = new ScanRequest(tableName);
			ScanResult scanResult = dynamoDB.scan(scanRequest);
			String PK = null;
			int max = 0;
			String maxapm = "am";
			String time;
			String apm;
			// scan all the item in the table and then delete them by PK
			for (Map<String, AttributeValue> item : scanResult.getItems()) {

				PK = getPK(item);
				// System.out.println(PK);
				time = PK.substring(1, 2) + PK.substring(3, 5);
				apm = PK.substring(5, 7);
				System.out.println(time);
				System.out.println(apm);
				int i = Integer.parseInt(time);
				System.out.println(i);

				if (maxapm.equals("am")) {
					if (apm.equals("am")) {
						if (i > max) {
							maxapm = apm;
							max = i;
						}
					} else {
						maxapm = apm;
						max = i;
					}
				} else {
					if (apm.equals("pm")) {
						if (i > max) {
							maxapm = apm;
							max = i;
						}
					}
				}

			}// loop end
				// find the max time
			System.out.println(max);
			System.out.println(maxapm);
			String s = String.valueOf(max);
			String aim = '"' + s.substring(0, 1) + ":" + s.substring(1)
					+ maxapm + '"';
			System.out.println(aim);
			// search for the max time
			String currenttime = null;
			String currentprice = null;
			String currentopen = null;
			HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
			key.put("Time", new AttributeValue().withS(aim));
			GetItemRequest getItemRequest = new GetItemRequest().withTableName(
					tableName).withKey(key);
			GetItemResult result = dynamoDB.getItem(getItemRequest);
			Map<String, AttributeValue> map = result.getItem();
			for (Map.Entry<String, AttributeValue> item : map.entrySet()) {
				String attributeName = item.getKey();
				AttributeValue value = item.getValue();
				if (attributeName.equals("Time"))
					currenttime = value.getS();
				if (attributeName.equals("price"))
					currentprice = value.getS();
				if (attributeName.equals("open"))
					currentopen = value.getS();
			}

			System.out.println(currenttime);
			System.out.println(currentprice);
			System.out.println(currentopen);
			List<String> currentStockinfoList = new ArrayList<String>();
			currentStockinfoList.add(currenttime);
			currentStockinfoList.add(currentopen);
			currentStockinfoList.add(currentprice);
			return currentStockinfoList;

		} catch (AmazonServiceException ase) {
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
		}
		List<String> errorList = new ArrayList<String>();
		errorList.add("wrong!!");
		return errorList;
	}

	private static String getPK(Map<String, AttributeValue> attributeList) {
		int i = 0;
		String keyvalue = null;
		for (Map.Entry<String, AttributeValue> item : attributeList.entrySet()) {
			String attributeName = item.getKey();
			AttributeValue value = item.getValue();

			keyvalue = value.getS();
			System.out.println(keyvalue);

		}
		return keyvalue;
	}
}