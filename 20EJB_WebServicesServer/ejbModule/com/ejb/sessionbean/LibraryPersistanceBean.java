package com.ejb.sessionbean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import com.ejb.model.Book;

@Stateless//(mappedName = "lbrsb")
@WebService(serviceName="LibraryService")
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
	@WebMethod(operationName="addBook")
	public void addBook(@WebParam(name = "name")String name,@WebParam(name = "author")String author) {
		Book book= new Book(name, author);
		System.out.println(book+ "addBook "+book.getName()+" "+book.getAuthor());
		entityManager.persist(book);
		
	}

	@Override
	@WebMethod(operationName="removeBook")
	public void removeBook(@WebParam(name = "name")String name,@WebParam(name = "author")String author) {
		entityManager.remove(getBook(name, author));
	}

	@Override
	@WebMethod(operationName="getBooks")
	public List<Book> getBooks()  {//throws BookNotFoundException
		List<Book> resultList = new ArrayList<Book>();
		/*try
		{*/
		Query query = entityManager.createQuery("FROM Book b");
		resultList = query.getResultList();		
		/*}
		 catch (Exception se) {
	         //throw (EJBException) new EJBException(se).initCause(se);    
			 se.printStackTrace();
	      }	*/
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
