package com.ejb.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.naming.InitialContext;

import com.ejb.model.Book;
import com.ejb.model.Publisher;
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
			Set<Publisher> pSet = books.get(i).getPublishers();
			List<Publisher> publishers = null;
			if(pSet != null && pSet.size() > 0)
			{
			publishers = new ArrayList<Publisher>(pSet);
			}
			String publisher = (publishers != null && publishers.size()>0) ?publishers.get(i).getName() :"";
			String publisherAddress = (publishers != null && publishers.size()>0) ?publishers.get(i).getAddress() :"";
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
			Set<Publisher> pSet = books.get(i).getPublishers();
			List<Publisher> publishers = null;
			if(pSet != null && pSet.size() == 0)
			{
			publishers = new ArrayList<Publisher>(pSet);
			}
			String publisher = (publishers != null&& publishers.size()>0) ?publishers.get(i).getName() :"";
			String publisherAddress = (publishers != null&& publishers.size()>0) ?publishers.get(i).getAddress() :"";
			System.out.println(i+1+". Book Name: "+books.get(i).getName()+" Book1 Author: "+books.get(i).getAuthor()
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
			LibrarySessionBeanRemote librarySessionBeanRemote = (LibrarySessionBeanRemote) ctx.lookup("lbrsb/LibraryPersistanceBean");
			//LibrarySessionBeanLocal librarySessionBeanLocal = (LibrarySessionBeanLocal) ctx.lookup("lbrsb/LibraryPersistanceBean");
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
						 librarySessionBeanRemote.addBook(bookName, bookAuthor,publisherName,publisherAddress);
						 showBooks(librarySessionBeanRemote);
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
			LibrarySessionBeanRemote librarySessionBeanRemote1 = (LibrarySessionBeanRemote) ctx.lookup("lbrsb/LibraryPersistanceBean");
			//LibrarySessionBeanLocal librarySessionBeanLocal1 = (LibrarySessionBeanLocal) ctx.lookup("lbrsb/LibraryPersistanceBean");
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
