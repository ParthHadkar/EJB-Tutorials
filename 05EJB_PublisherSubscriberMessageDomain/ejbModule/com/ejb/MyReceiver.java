package com.ejb;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

import com.ejb.listener.MyListener;

public class MyReceiver {

	private Properties props;
	private InitialContext ctx;
	private TopicConnectionFactory topicConnectionFactory;
	private TopicConnection topicConnection;
	private TopicSession topicSession;
	private Topic topic;
	private TopicSubscriber topicSubscriber;
	private MyListener myListener;
	
	// Instant Initialization Block (IIB)
	{
		try {
			props = new Properties();
			FileInputStream propsFile = new FileInputStream("jndi.properties");
			props.load(propsFile);
			ctx = new InitialContext(props);//props
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createTopic(String topicConnectionFactoryName,String topicName) 
	{
		try {
			//1. Initialize TopicConnectionFactory object
			topicConnectionFactory = (TopicConnectionFactory) ctx.lookup(topicConnectionFactoryName);//"myTopicConnectionFactory"
			//2. Create QueueConnection using TopicConnectionFactory object and start the connection
			topicConnection = topicConnectionFactory.createTopicConnection();
			topicConnection.start();
			//3. Create QueueSession using TopicConnection object
			topicSession = topicConnection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
			//4. Get Queue Object
			topic = (Topic) ctx.lookup(topicName);//"MyTopic"
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void TestTopicReceiver() {
		try {
			createTopic("ConnectionFactory","/topic/MyTopic");
			//5. Create QueueReceiver Object
			topicSubscriber = topicSession.createSubscriber(topic);
			//6. Create Listener Object
			myListener = new MyListener();
			topicSubscriber.setMessageListener(myListener);
			System.out.println("Receiver1 is ready, waiting for messages...");  
            System.out.println("press Ctrl+c to shutdown...");  
			while(true) {
				Thread.sleep(2000);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MyReceiver myReceiver = new MyReceiver();
		myReceiver.TestTopicReceiver();
	}
	
}
