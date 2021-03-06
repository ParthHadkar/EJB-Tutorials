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
import javax.naming.InitialContext;

import com.ejb.listener.MyListener;

public class MyReceiver {

	private BufferedReader br = null;
	private Properties props;
	private InitialContext ctx;
	private QueueConnectionFactory queueConnectionFactory;
	private QueueConnection queueConnection;
	private QueueSession queueSession;
	private Queue queue;
	private QueueReceiver queueReceiver;
	private MyListener myListener;
	
	// Instant Initialization Block (IIB)
	{
		try {
			props = new Properties();
			FileInputStream propsFile = new FileInputStream("jndi.properties");
			props.load(propsFile);
			ctx = new InitialContext(props);//props
			br = new BufferedReader(new InputStreamReader(System.in));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createQueue(String queueConnectionFactoryName,String queueName)
	{
		try {
			//1. Initialize QueueConnectionFactory Object
			queueConnectionFactory = (QueueConnectionFactory) ctx.lookup(queueConnectionFactoryName);//"myQueueConnectionFactory"
			//2. Create QueueConnection using QueueConnectionFactory Object and start the connection
			queueConnection = queueConnectionFactory.createQueueConnection();
			queueConnection.start();
			//3. Create QueueSession Object
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			//4. Get Queue object
			queue = (Queue) ctx.lookup(queueName);//"MyQueue"
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void TestQueueReceiver() {
		try {
			createQueue("ConnectionFactory","/queue/MyQueue");
			//5. Create QueueReceiver Object
			queueReceiver = queueSession.createReceiver(queue);
			//6. Create Listener Object
			myListener = new MyListener();
			queueReceiver.setMessageListener(myListener);
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
		myReceiver.TestQueueReceiver();
	}
	
}
