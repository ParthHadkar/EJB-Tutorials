package com.ejb.sessionbean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;

import com.ejb.model.Book;


@Stateful//(mappedName = "lbrsb")
public class LibraryStatefulSessionBean implements LibrarySessionBeanRemote, LibrarySessionBeanLocal 
{

	public List<Book> booksList;
   
	 public LibraryStatefulSessionBean() 
	    {
	    	if(booksList == null) {
	    		booksList = new ArrayList<Book>();
	    	}
	    }

		@Override
		public void addBook(String name,String author) {
			Book book= new Book(name, author);
			System.out.println(book+ "addBook "+book.getName()+" "+book.getAuthor());
			booksList.add(book);
			
		}

		@Override
		public void removeBook(String name,String author) {
			System.out.println(booksList);
			booksList.remove(getBook(name, author));
			System.out.println(booksList);
		}

		@Override
		public List<Book> getBooks() {
			return booksList;
		}

		@Override
		public Book getBook(String name,String author) {
			Book book = null;
			for(int i=0; i<booksList.size(); i++) {
				Book book1 = booksList.get(i);
				if(book1.getName().equals(name) && book1.getName().equals(author)) {
					book = book1;
					System.out.println(book+ "getBook "+book.getName()+" "+book.getAuthor());
					break;
				}
			}
			return book;
		}

}
