package com.ejb.sessionbean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import com.ejb.model.Book;
import com.ejb.model.Publisher;

@Stateless//(mappedName = "lbrsb")
public class LibraryPersistanceBean implements LibrarySessionBeanLocal,LibrarySessionBeanRemote 
{
	 //@PersistenceUnit(unitName="unit1") 
	 //private EntityManagerFactory factory;

	@PersistenceContext(unitName = "unit1")
	private EntityManager entityManager;
         
	/*protected final EntityManager getEntityManager() {
        if (entityManager == null) {
        	entityManager = factory.createEntityManager();
        }
        return entityManager;
    }*/
	
    public LibraryPersistanceBean() 
    {
    	//getEntityManager();
    }

	@Override
	public void addBook(String name,String author) {
		Book book= new Book(name, author);
		System.out.println(book+ "addBook "+book.getName()+" "+book.getAuthor());
		entityManager.persist(book);
		
	}
	
	@Override
	public void addBook(String name, String author, String publisherName, String publisherAddress) {
		Book book= new Book(name, author);
		Publisher publisher = new Publisher(publisherName,publisherAddress);
		book.setPublisher(publisher);
		System.out.println(book+ "addBook "+book.getName()+" "+book.getAuthor()+" "+book.getPublisher());
		entityManager.persist(book);
	}


	@Override
	public void removeBook(String name,String author) {
		entityManager.remove(getBook(name, author));
	}

	@Override
	public List<Book> getBooks() {
		Query query = entityManager.createQuery("FROM Book b");
		List<Book> resultList = query.getResultList();
		return resultList;
	}

	@Override
	public Book getBook(String name,String author) {
		Book book = null;
		Query query = entityManager.createQuery("FROM Book b WHERE b.name=:name AND b.author=:author");
		query.setParameter("name", name);
		query.setParameter("author", author);
		book = (Book) query.getSingleResult();
		return book;
	}

	
    
    
}
