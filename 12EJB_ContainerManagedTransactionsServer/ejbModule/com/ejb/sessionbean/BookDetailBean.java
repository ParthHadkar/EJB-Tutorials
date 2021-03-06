package com.ejb.sessionbean;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ejb.model.Book;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BookDetailBean implements BookDetailBeanRemote,BookDetailBeanLocal
{

	@PersistenceContext(unitName = "unit1")
	private EntityManager entityManager;
	
	public BookDetailBean() {}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void createUserDetail(Book book) 
	{
		entityManager.merge(book);
	}
	
}
