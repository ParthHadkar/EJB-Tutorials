package com.ejb.sessionbean;

import java.util.List;

import javax.ejb.Remote;

import com.ejb.model.Book;

@Remote
public interface LibrarySessionBeanRemote 
{
	
	public void addBook(String name,String author);
	
	public void removeBook(String name,String author);
	
	public void addBook(String name,String author,String publisherName,String publisherAddress);
	
	public void addBook(String name,String author,String publisherName,String publisherAddress,byte[] image, String xml);
	
	public List<Book> getBooks();
	
	public Book getBook(String name,String author);

}
