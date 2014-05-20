package com.stockcloud;

public class Test {
	public void testing() throws Exception {

		// Test User.java
		/*
		 * String pw = "qwerty"; User newUser = new User("jethro",
		 * "jethro@live.cn", pw);
		 * System.out.println("User created: ID = "+newUser.userID +
		 * ", E-Mail = " + newUser.email + ", Password = " + pw);
		 * 
		 * newUser.changeID("jethro894", pw);
		 * System.out.println("User info updated: ID = "+newUser.userID +
		 * ", E-Mail = " + newUser.email + ", Password = " + pw);
		 * 
		 * String newpw = "asdasd"; if(newUser.changePassword(newpw, pw))
		 * System.out.println("User Password updated: Password = " + newpw);
		 * else System.out.println("User Password update failed");
		 * 
		 * System.out.println("=============================================");
		 * newUser.addFavorite("stockA"); newUser.addFavorite("stockE");
		 * newUser.addFavorite("stockB"); newUser.addFavorite("stockE");
		 * newUser.addFavorite("stockD"); newUser.addFavorite("stockG");
		 * 
		 * for(String i : newUser.favorates){ System.out.println(i); }
		 * 
		 * System.out.println("=============================================");
		 * newUser.deleteFavorite("stockE"); newUser.deleteFavorite("stockZ");
		 * 
		 * for(String i : newUser.favorates){ System.out.println(i); }
		 * 
		 * System.out.println("=============================================");
		 * User scanresult = User.searchUser("jethro@live.cn");
		 * System.out.println("User retrieved: ID = "+scanresult.userID +
		 * ", E-Mail = " + scanresult.email + ", PasswordHash = " +
		 * scanresult.passwdHash); for(String i : scanresult.favorates){
		 * System.out.println(i); }
		 */

		// Test ChatTopic.java

		String DEFAULT_STOCK = "ABT";
		ChatTopic ct = new ChatTopic(DEFAULT_STOCK);

		// ct.topicStockCodeName = DEFAULT_STOCK;

		String POSTER1 = "123";
		ThreadBody tb1 = ct.addThreadToTopic("buy ASAP",
				"The FIrst Text Thread", POSTER1);
		String POSTER2 = "jethro@hotmail.com";
		ThreadBody tb2 = ct.addThreadToTopic("Check it out",
				"The second Text Thread", POSTER2);

		ThreadReply tb1tr1 = tb1.addReplyToThread(
				"The 1st text reply in thread 1", POSTER2, 0);
		ThreadReply tb1tr2 = tb1.addReplyToThread(
				"The 2 text reply in thread 1", POSTER1, 0);
		ThreadReply tb1tr3 = tb1.addReplyToThread(
				"The 3rd text reply in thread 1", POSTER2, 2);

		tb1tr3.upVote();
		tb1tr3.upVote();
		tb1tr3.downVote();
		tb1tr3.downVote();
		tb1tr3.downVote();
		tb1tr3.downVote();
		ct.print();
		System.out.println("=============================================");
		tb1.print();
		System.out.println("=============================================");
		tb1tr1.print();
		System.out.println("=================");
		tb1tr2.print();
		System.out.println("=================");
		tb1tr3.print();
		System.out.println("=================");
		try {
			if (ct.listThreads() != null) {
				for (ThreadBody tb : ct.listThreads()) {
					System.out.println(tb.text);
					if (tb.listReplies() != null) {
						for (ThreadReply tr : tb.listReplies()) {
							System.out.println(tr.text);
						}
					}

				}
			}

		} catch (Exception e) {

		}

		System.out.println("=============================================");

	}
}
