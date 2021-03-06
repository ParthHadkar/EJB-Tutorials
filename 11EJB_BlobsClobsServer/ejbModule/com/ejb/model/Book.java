package com.ejb.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.jboss.logging.Logger;
@Entity
@Table(name="book")
public class Book implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "AUTHOR")
	private String author;
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="name",column = @Column(name = "PUBLISHER")),
	@AttributeOverride(name="address",column = @Column(name = "PUBLISHER_ADDRESS"))})
    private Publisher publisher;
	
	@Lob @Basic(fetch = FetchType.EAGER)
	private byte[] image;
	
	@Lob @Basic(fetch = FetchType.EAGER)
	private String xml;
	
	public Book() {}
	
	
	public Book(String name, String author) {
		this.name = name;
		this.author = author;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Publisher getPublisher() {
		return publisher;
	}
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}


	public byte[] getImage() {
		return image;
	}


	public void setImage(byte[] image) {
		this.image = image;
	}


	public String getXml() {
		return xml;
	}


	public void setXml(String xml) {
		this.xml = xml;
	}
	
	
}
