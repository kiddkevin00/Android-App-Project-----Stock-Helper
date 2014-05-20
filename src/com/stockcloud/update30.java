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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class update30 {

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

	}

	public int update_30(String stockName) throws Exception {
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
		// int i =0;
		String symbol = stockName;
		// ,"ABBV","ACE","ACN","ACT","ADBE","ADT","AET","AFL","ARG","AA","ALL","ALTR","AMZN","AAPL","BLL","BAC","BMS","BBY","BLK","BSX","CA","COF","CCL","CAT","CERN","CVX","CSCO","COH","KO","COST","DVN","DTV","DG","DOV","DOW","DUK","EBAY","EOG","EMR","EFX","EQR","EXPE","ESRX","FB","FE","FLIR","FMC","F","FOSL","GME","GPS","GD","GE","GIS","GS","GOOG","HOG","HRS","HCP","HAS","HES","HD","HST","HUM","ITW","INTC","ICE","IBM","INTU","IVZ","JOY","JNPR","KEY","KMI","KLAC","KSS","KR","LB","LRCX","LM","LEN","LNC","LOW","MAC","MMM","M","MAR","MA","MCD","MRK","MET","MU","MSFT","MNST","MS","MOS","MUR","MYL"};

		// Num[]={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","3","2","3","4","5","6","7","8","9","10","1","2","3","4","5","6","7","8","9","10","1","2","3","4","5","6","7","8","9","10","1","2","3","4","5","6","7","8","9","10","1","2","3","4","5","6","7","8","9","10","1","2","3","4","5","6","7","8","9","10",}
		// loop to get the current data of every stocks(100)
		// while(i<symbol.length)
		// {
		List<String> closelist = new ArrayList<String>();
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);// ������������
		int month = ca.get(Calendar.MONTH);// ������������
		int day = ca.get(Calendar.DATE) - 1;// ���������
		String finalyear = String.valueOf(year);
		String finalmonth = String.valueOf(month);
		String finalday = String.valueOf(day);
		String startday = "1";
		String startmonth = finalmonth;
		String startyear = finalyear;
		System.out.println(year);
		System.out.println(month);
		System.out.println(day);
		url = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + symbol
				+ "&d=" + finalmonth + "&e=" + finalday + "&f=" + finalyear
				+ "&g=d&a=" + startmonth + "&b=" + startday + "&c=" + startyear
				+ "&ignore=.csv");
		urlConn = url.openConnection();
		inStream = new InputStreamReader(urlConn.getInputStream());
		buff = new BufferedReader(inStream);
		// get the quote as a csv string
		csvString = buff.readLine();
		csvString = buff.readLine();
		// String closelist[] = null;
		int i = 0;
		while (csvString != null)

		{
			// parse the csv string
			StringTokenizer tokenizer = new StringTokenizer(csvString, ",");
			String date = tokenizer.nextToken();
			String open = tokenizer.nextToken();
			String high = tokenizer.nextToken();
			String low = tokenizer.nextToken();
			String close = tokenizer.nextToken();
			String volume = tokenizer.nextToken();
			String adj = tokenizer.nextToken();

			// System.out.println("date: " + date +
			// " open: " + open + " high: " + high
			// + " low: " + low);

			closelist.add(close);

			System.out.println(close);
			i++;

			csvString = buff.readLine();

		}// whileover
			// int j=0;
		String s;
		s = closelist.get(0);
		s = s.substring(3);
		i--;
		// String s;

		// while(j<i)
		// {
		// s=closelist.get(j);
		// System.out.println(s);
		// j++;
		// }

		inStream.close();
		buff.close();
		System.out.print(i);
		handler handle = new handler();
		int predict = handle.algorithm(closelist, i);
		System.out.print(predict + "");
		return predict;
	}// main over

}// class over
