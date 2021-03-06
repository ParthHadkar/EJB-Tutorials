package com.ejb.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;

import com.ejb.model.Book;
import com.ejb.sessionbean.LibrarySessionBeanLocal;
import com.ejb.sessionbean.LibrarySessionBeanRemote;

public class EJBLibraryClient 
{
	
	private BufferedReader bf = null;
	private Properties props;
	private InitialContext ctx;
	
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
			String publisher = (books.get(i).getPublisher() != null) ?books.get(i).getPublisher().getName().toString() :"";
			String publisherAddress = (books.get(i).getPublisher() != null) ?books.get(i).getPublisher().getAddress().toString() :"";
			System.out.println(i+1+". Book Name: "+books.get(i).getName()+" Book Author: "+books.get(i).getAuthor()
					+" Publisher Name: "+publisher
					+" Publisher Address: "+publisherAddress);
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
			String publisher = (books.get(i).getPublisher() != null) ?books.get(i).getPublisher().getName().toString() :"";
			String publisherAddress = (books.get(i).getPublisher() != null) ?books.get(i).getPublisher().getAddress().toString() :"";
			System.out.println(i+1+". Book Name: "+books.get(i).getName()+" Book Author: "+books.get(i).getAuthor()
					+" Publisher Name: "+publisher
					+" Publisher Address: "+publisherAddress);
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void testStatelessEjb()
	{
		try {
			int choice = 0;
			LibrarySessionBeanRemote librarySessionBeanRemote = (LibrarySessionBeanRemote) ctx.lookup("11EJB_BlobsClobs/LibraryPersistanceBean/remote");
			//LibrarySessionBeanLocal librarySessionBeanLocal = (LibrarySessionBeanLocal) ctx.lookup("11EJB_BlobsClobs/LibraryPersistanceBean/local");
			while(choice != 3) 
			{
				showGui();
				String userInput = bf.readLine();
				if(userInput.matches("[1-3]"))
				{
					 choice = Integer.parseInt(userInput);
					 String bookName;
					 String bookAuthor;
					 String publisherName;
					 String publisherAddress;
					 String dfImageXml;
					 switch(choice) {
					 case 1:
						 System.out.println("Enter Book Name");
						 bookName = bf.readLine();
						 System.out.println("Enter Book Author");
						 bookAuthor = bf.readLine();
						 System.out.println("Enter Publisher Name");
						 publisherName = bf.readLine();
						 System.out.println("Enter Publisher Address");
						 publisherAddress = bf.readLine();
						 System.out.println("Enter Y OR N To add Default Image and Default Xml");
						 dfImageXml = bf.readLine();
						 if(dfImageXml.equalsIgnoreCase("Y") || dfImageXml.equalsIgnoreCase("Yes"))
						 {
							 byte[] imageBytes = {0x32, 0x32,0x32, 0x32,0x32,
						               0x32,0x32, 0x32,
						               0x32, 0x32,0x32, 0x32,0x32, 0x32,0x32, 0x32,
						               0x32, 0x32,0x32, 0x32,0x32, 0x32,0x32, 0x32
						               };
							 String xml = "<book><name>"+bookName+"</name><author>"+bookAuthor+"</author>"
							 		+ "<publisherName>"+publisherName+"</publisherName><publisherAddress>"+publisherAddress+"</publisherAddress></book>";
							 librarySessionBeanRemote.addBook(bookName, bookAuthor,publisherName,publisherAddress,imageBytes,xml);
							 showBooks(librarySessionBeanRemote);
						 }
						 else if(dfImageXml.equalsIgnoreCase("N") || dfImageXml.equalsIgnoreCase("No"))
						 {
							 librarySessionBeanRemote.addBook(bookName, bookAuthor,publisherName,publisherAddress);
							 showBooks(librarySessionBeanRemote);
						 }
						 else 
						 {
							 System.out.println("Please Enter Y OR N");
						 }
						 //librarySessionBeanLocal.addBook(bookName, bookAuthor);
						 //showBooks(librarySessionBeanLocal);
						 break;
					 case 2:
						 System.out.println("Enter Book Name");
						 bookName = bf.readLine();
						 System.out.println("Enter Book Author");
						 bookAuthor = bf.readLine();
						 //Book book = librarySessionBeanRemote.getBook(bookName, bookAuthor);
						 //System.out.println(book+ "Book "+book.getName()+" "+book.getAuthor());
						 librarySessionBeanRemote.removeBook(bookName,bookAuthor);
						 showBooks(librarySessionBeanRemote);
						 //librarySessionBeanLocal.removeBook(bookName,bookAuthor);
						 //showBooks(librarySessionBeanLocal);
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
			LibrarySessionBeanRemote librarySessionBeanRemote1 = (LibrarySessionBeanRemote) ctx.lookup("11EJB_BlobsClobs/LibraryPersistanceBean/remote");
			//LibrarySessionBeanLocal librarySessionBeanLocal1 = (LibrarySessionBeanLocal) ctx.lookup("11EJB_BlobsClobs/LibraryPersistanceBean/local");
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
