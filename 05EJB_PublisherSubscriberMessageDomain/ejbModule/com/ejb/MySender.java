package com.ejb;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MySender 
{
	
	private BufferedReader bf = null;
	private Properties props;
	private InitialContext ctx;
	private TopicConnectionFactory topicConnectionFactory;
	private TopicConnection topicConnection;
	private TopicSession topicSession;
	private Topic topic;
	private TopicPublisher topicPublisher;
	private TextMessage textMessage;
	
	// Instant Initialization Block (IIB)
	{
		try {
			props = new Properties();
			FileInputStream propsFile = new FileInputStream("jndi.properties");
			props.load(propsFile);
			ctx = new InitialContext(props);//props
			bf = new BufferedReader(new InputStreamReader(System.in));
		}
		catch(Exception e) {
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
	
	public void sendMessage(String msg) 
	{
		try {
			textMessage.setText(msg);
			topicPublisher.publish(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showGui()
	{
		System.out.println("#######################");
		System.out.println("Welcome To Publisher Subscriber Messaging Domain");
		System.out.println("#######################");
		System.out.println("Options \n 1.Send Message \n 2. Exit\n Enter Choice: \n");
	}
	
	public void TestTopicSender() {
		try {
			createTopic("ConnectionFactory","/topic/MyTopic");
			//5. CreateTopicSender Object
			topicPublisher = topicSession.createPublisher(topic);
			//6. Create TextMessage Object
			textMessage = topicSession.createTextMessage();
			int choice = 0;
			while(choice != 2) 
			{
				showGui();
				String userInput = bf.readLine();
				if(userInput.matches("[1-2]"))
				{
					 choice = Integer.parseInt(userInput);
					 switch (choice) {
					case 1:
						 System.out.println("Enter Message");
						 String userMsg = bf.readLine();
						if(userMsg.trim() != null && userMsg.trim().length() > 0) {
							sendMessage(userMsg);
						}
						else {
							System.out.println("Can't Enter Empty Message");
						}
						break;
					case 2:
						break;
					}
				}
				else 
				{
					System.out.println("Please enter choice between 1 and 2");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(bf != null) 
			{
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(topicConnection != null) 
			{
				try {
					topicConnection.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(topicSession != null) 
			{
				try {
					topicSession.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(topicPublisher != null) 
			{
				try {
					topicPublisher.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(textMessage != null) 
			{
				try {
					textMessage.clearBody();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public static void main(String[] args) {
		MySender mySender = new MySender();
		mySender.TestTopicSender();
	}

}
