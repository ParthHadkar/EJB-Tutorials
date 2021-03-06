package com.ejb.sessionbean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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

import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;

import com.ejb.model.Book;
import com.ejb.model.Publisher;

@Stateless//(mappedName = "lbrsb")
//@LocalBinding(jndiBinding="lbrsb/LibraryPersistanceBean")
@RemoteBinding(jndiBinding="lbrsb/LibraryPersistanceBean")
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
	public void addBook(String name,String author,String pName,String pAddress) {
		try
		{
		Book book= new Book(name, author);
		System.out.println(book+ "addBook "+book.getName()+" "+book.getAuthor());
		Set<Publisher> publisherSet = new HashSet<Publisher>();
		Publisher publisher = new Publisher(pName, pAddress);
		publisherSet.add(publisher);
		book.setPublishers(publisherSet);
		entityManager.persist(book);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeBook(String name,String author) {
		entityManager.remove(getBook(name, author));
	}

	@Override
	public List<Book> getBooks() {
		List<Book> myBook = null;
		Query query = entityManager.createQuery("FROM Book");//createNativeQuery("select b.id,b.name,b.author,p.id,p.name,p.address from  Book b inner join  Publisher p inner join  book_publisher bp on bp.publisher_id = p.id and bp.book_id = b.id"
				//,Book.class);
		/*List<Object[]> resultList = (List<Object[]>)query.getResultList();
		List<Book> myBook = new ArrayList<Book>();
		for (Object[] aRow : resultList) 
		{
			Book book = (Book) aRow[0];
			HashSet<Publisher> publisher = (HashSet<Publisher>) aRow[1];
			//HashSet<Publisher> pset = new HashSet<Publisher>();
			//pset.add(publisher);
			book.setPublishers(publisher);
			myBook.add(book);
		}*/
		myBook = query.getResultList();
		return myBook;
	}

	@Override
	public Book getBook(String name,String author) {
		Book book = null;
		Query query = entityManager.createQuery("FROM Book b WHERE b.name=:name AND b.author=:author");
		query.setParameter("name", name);
		query.setParameter("author", author);
		book = (Book) query.getSingleResult();
		book.setPublishers(book.getPublishers());
		System.out.println("book"+book.toString());
		return book;
	}

	
    
    
}
