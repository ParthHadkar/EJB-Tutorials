package com.ejb.sessionbean;

import javax.ejb.Remote;

import com.ejb.model.Book;

@Remote
public interface BookDetailBeanRemote {
 
	public void createUserDetail(Book book);
	
}
