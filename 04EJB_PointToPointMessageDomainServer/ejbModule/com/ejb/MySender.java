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
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MySender 
{
	
	private BufferedReader bf = null;
	private Properties props;
	private InitialContext ctx;
	private QueueConnectionFactory queueConnectionFactory;
	private QueueConnection queueConnection;
	private QueueSession queueSession;
	private Queue queue;
	private QueueSender queueSender;
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
	public void createQueue(String queueConnectionFactoryName,String quequeName) 
	{
		try {
			//1. Initialize QueueConnectionFactory object
			queueConnectionFactory = (QueueConnectionFactory) ctx.lookup(queueConnectionFactoryName);//"myQueueConnectionFactory"
			//2. Create QueueConnection using QueueConnectionFactory object and start the connection
			queueConnection = queueConnectionFactory.createQueueConnection();
			queueConnection.start();
			//3. Create QueueSession using QueueConnection object
			queueSession = queueConnection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
			//4. Get Queue Object
			queue = (Queue) ctx.lookup(quequeName);//"MyQueue"
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void sendMessage(String msg) 
	{
		try {
			textMessage.setText(msg);
			queueSender.send(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 	
	private void showGui()
	{
		System.out.println("#######################");
		System.out.println("Welcome To Point To Point Messaging Domain");
		System.out.println("#######################");
		System.out.println("Options \n 1.Send Message \n 2. Exit\n Enter Choice: \n");
	}
	
	public void TestQueueSender() {
		try {
			createQueue("ConnectionFactory","/queue/MyQueue");
			//5. Create QueueSender Object
			queueSender = queueSession.createSender(queue);
			//6. Create TextMessage Object
			textMessage = queueSession.createTextMessage();
			int choice = 0;
			while(choice != 3) 
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
			if(queueConnection != null) 
			{
				try {
					queueConnection.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(queueSession != null) 
			{
				try {
					queueSession.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(queueSender != null) 
			{
				try {
					queueSender.close();
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
		mySender.TestQueueSender();
	}

}
