package com.ejb.sessionbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.ejb.model.Book;
import com.ejb.model.Publisher;

@Stateless//(mappedName = "lbrsb")
@TransactionManagement(TransactionManagementType.BEAN)
public class LibraryPersistanceBean implements LibrarySessionBeanLocal,LibrarySessionBeanRemote 
{
	 //@PersistenceUnit(unitName="unit1") 
	 //private EntityManagerFactory factory;

	@PersistenceContext(unitName = "unit1")
	private EntityManager entityManager;
	
	@Resource
	private UserTransaction userTransaction;
	
         
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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addBook(String name,String author,String pName,String pAddress) {
		try
		{
		userTransaction.begin();
		Book book= new Book(name, author);
		System.out.println(book+ "addBook "+book.getName()+" "+book.getAuthor());
		entityManager.persist(book);
		Publisher publisher = new Publisher(pName, pAddress);
		book.setPublisher(publisher);
		entityManager.merge(book);
		userTransaction.commit();
		}
		catch (Exception e) {
			try {
				userTransaction.rollback();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
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
