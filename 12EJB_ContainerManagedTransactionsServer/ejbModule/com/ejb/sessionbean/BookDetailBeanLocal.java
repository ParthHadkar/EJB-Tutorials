package com.ejb.sessionbean;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.ejb.model.Book;

@Local
public interface BookDetailBeanLocal {
 
	public void createUserDetail(Book book);
	
}
