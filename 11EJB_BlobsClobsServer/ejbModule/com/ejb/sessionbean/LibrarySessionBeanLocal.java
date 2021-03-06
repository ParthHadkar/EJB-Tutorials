package com.ejb.sessionbean;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.ejb.model.Book;

@Local
public interface LibrarySessionBeanLocal 
{
	
	public void addBook(String name,String author);
	
	public void addBook(String name,String author,String publisherName,String publisherAddress);
	
	public void addBook(String name,String author,String publisherName,String publisherAddress,byte[] image, String xml);
	
	public void removeBook(String name,String author);
	
	public List<Book> getBooks();
	
	public Book getBook(String name,String author);

}
