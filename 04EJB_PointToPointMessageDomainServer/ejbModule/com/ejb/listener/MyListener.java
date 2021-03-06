package com.ejb.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyListener implements MessageListener {

	public void onMessage(Message arg0) {
		try {
			TextMessage tm = (TextMessage) arg0;
			System.out.println("Following Message is been recieved: "+tm.getText());
		}
		catch (Exception e) {
			System.out.println("Error: "+e);
			e.printStackTrace();
		}

	}

}
