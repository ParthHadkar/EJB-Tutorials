package com.ejb.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;


import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import com.ejb.model.Book;
import com.ejb.sessionbean.LibrarySessionBeanLocal;
import com.ejb.sessionbean.LibrarySessionBeanRemote;

public class EJBLibraryClient 
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
	private ObjectMessage objectMessage;
	
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
	
	private void showGui()
	{
		System.out.println("#######################");
		System.out.println("Welcome To Book Store");
		System.out.println("#######################");
		System.out.println("Options \n 1. Add Book\n 2. Remove Book\n 3. Exit\n Enter Choice: \n");
	}
	
	private void showBooks(LibrarySessionBeanRemote lsr) 
	{
		try {
		List<Book> books = lsr.getBooks();
		System.out.println("Book entered so far: "+books.size());
		for(int i=0; i<books.size(); i++) 
		{
			System.out.println(i+1+". Book Name: "+books.get(i).getName()+" Book Author: "+books.get(i).getAuthor());
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showBooks(LibrarySessionBeanLocal lsr) 
	{
		try {
		List<Book> books = lsr.getBooks();
		System.out.println("Book entered so far: "+books.size());
		for(int i=0; i<books.size(); i++) 
		{
			System.out.println(i+1+". Book Name: "+books.get(i).getName()+" Book Author: "+books.get(i).getAuthor());
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createQueue(String queueConnectionFactoryName,String queueName)
	{
		try {
			//1. Initialize QueueConnectionFactory Object
			queueConnectionFactory = (QueueConnectionFactory) ctx.lookup(queueConnectionFactoryName);//"myTopicConnectionFactory"
			//2. Create QueueConnection using QueueConnectionFactory Object and start the connection
			queueConnection = queueConnectionFactory.createQueueConnection();
			queueConnection.start();
			//3. Create QueueSession Object
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			//4. Get Queue object
			queue = (Queue) ctx.lookup(queueName);//"MyTopic"
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendTextMessage(String msg) 
	{
		try {
			textMessage.setText(msg);
			queueSender.send(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendObjectMessage(Book book) 
	{
		try {
			objectMessage.setObject(book);
			queueSender.send(objectMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void testStatelessEjb()
	{
		try {
			int choice = 0;
			createQueue("ConnectionFactory","/queue/BookQueue");
			//5. Create QueueSender Object
			queueSender = queueSession.createSender(queue);
			//6. Create TextMessage Object
			textMessage = queueSession.createTextMessage();
			//7. Create ObjectMessage Object
			objectMessage = queueSession.createObjectMessage();
			while(choice != 3) 
			{
				showGui();
				String userInput = bf.readLine();
				if(userInput.matches("[1-3]"))
				{
					 choice = Integer.parseInt(userInput);
					 sendTextMessage(userInput);
					 String bookName;
					 String bookAuthor;
					 switch(choice) {
					 case 1:
						 System.out.println("Enter Book Name");
						 bookName = bf.readLine();
						 System.out.println("Enter Book Author");
						 bookAuthor = bf.readLine();
						 Book addBook = new Book(bookName, bookAuthor);
						 sendObjectMessage(addBook);
						 break;
					 case 2:
						 System.out.println("Enter Book Name");
						 bookName = bf.readLine();
						 System.out.println("Enter Book Author");
						 bookAuthor = bf.readLine();
						 Book removeBook = new Book(bookName, bookAuthor);
						 sendObjectMessage(removeBook);
						 break;
					 case 3:
						 break;
					 }
				} 
				else 
				{
					System.out.println("Please enter choice between 1 and 3");
				}
			}
			
			System.out.println("Using second lookup to get library stateless object");
			LibrarySessionBeanRemote librarySessionBeanRemote1 = (LibrarySessionBeanRemote) ctx.lookup("06EJB_MessageDrivenBean/LibraryPersistanceBean/remote");
			//LibrarySessionBeanLocal librarySessionBeanLocal1 = (LibrarySessionBeanLocal) ctx.lookup("06EJB_MessageDrivenBean/LibraryPersistanceBean/local");
			showBooks(librarySessionBeanRemote1);
			//showBooks(librarySessionBeanLocal1);
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
		}
	}
	
	public static void main(String[] args) {
		EJBLibraryClient ejbLibraryClient = new EJBLibraryClient();
		ejbLibraryClient.testStatelessEjb();
	}
	
}
