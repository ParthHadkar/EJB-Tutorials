package com.ejb.listener;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import com.ejb.model.Book;

public class BookCallBackListener {

	@PrePersist
	public void prePersist(Object book) {
		System.out.println(getClass().getSimpleName()+" PrePersist : Book to be created with book id: "+((Book)book).getId());
	}
	
	@PostPersist
	public void postPersist(Book book) {
		System.out.println(getClass().getSimpleName()+" PostPersist : Book created with book id: "+book.getId());
	}
	
	@PreRemove
	public void preRemove(Book book) {
		System.out.println(getClass().getSimpleName()+" PreRemove : About to delete Book with book id: "+book.getId());
	}
	
	@PostRemove
	public void postRemove(Book book) {
		System.out.println(getClass().getSimpleName()+" PostRemove : Deleted Book with book id: "+book.getId());
	}
	
	@PreUpdate
	public void preUpdate(Book book) {
		System.out.println(getClass().getSimpleName()+" PreUpdate : About to update Book with book id: "+book.getId());
	}
	
	@PostUpdate
	public void postUpdate(Book book) {
		System.out.println(getClass().getSimpleName()+" PostUpdate : Updated Book with book id: "+book.getId());
	}
	
	@PostLoad
	public void postLoad(Book book) {
		System.out.println(getClass().getSimpleName()+" PostLoad : Loaded Book with book id: "+book.getId());
	}
	
}
