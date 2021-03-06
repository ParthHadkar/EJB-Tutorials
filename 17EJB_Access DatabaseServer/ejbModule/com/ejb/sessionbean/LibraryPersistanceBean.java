package com.ejb.sessionbean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import com.ejb.model.Book;

@Stateless//(mappedName = "lbrsb")
public class LibraryPersistanceBean implements LibrarySessionBeanLocal,LibrarySessionBeanRemote 
{
    private static String driverClass ="com.mysql.jdbc.Driver";
	
	private static String connUrl ="jdbc:mysql://localhost:3306/bookdb";
	
	private static String username ="root";

	private static String password ="CoderNinj@786";
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driverClass);
			conn = DriverManager.getConnection(connUrl,username,password);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
    public LibraryPersistanceBean() 
    {
    	//getEntityManager();
    }

	@Override
	public void addBook(String name,String author) {
		try {
			System.out.println("Establishing Connection");
			Connection conn = getConnection();
			System.out.println("Connection Established");
			String selectQuery = "INSERT INTO book(name,author) VALUES(?,?)";
			System.out.println("Executing query");
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, name);
			ps.setString(2, author);
			System.out.println("Inserting result");
			int row = ps.executeUpdate();
			
			System.out.println("Result Inserted "+row);			
			ps.close();
			System.out.println("PreparedStatement Closed");
			if(conn != null) {
				conn.close();
				System.out.println("Connection Closed");
			}
			}
		catch(Exception e) {
			System.out.println("Connection Errror "+e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void removeBook(String name,String author) {
		try {
			System.out.println("Establishing Connection");
			Connection conn = getConnection();
			System.out.println("Connection Established");
			String selectQuery = "DELETE FROM book  WHERE name=? and author =?";
			System.out.println("Executing query");
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, name);
			ps.setString(2, author);
			System.out.println("Deleting result");
			int row = ps.executeUpdate();
			
			System.out.println("Result Deleted "+row);			
			ps.close();
			System.out.println("PreparedStatement Closed");
			if(conn != null) {
				conn.close();
				System.out.println("Connection Closed");
			}
			}
		catch(Exception e) {
			System.out.println("Connection Errror "+e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public List<Book> getBooks() {
		List<Book> resultList = new ArrayList<Book>();
		try {
			System.out.println("Establishing Connection");
			Connection conn = getConnection();
			System.out.println("Connection Established");
			String selectQuery = "select * from  book";
			System.out.println("Executing query");
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			System.out.println("Fetching result");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Book book = new Book(rs.getString("name"),rs.getString("author"));
				resultList.add(book);
			}
			System.out.println("Result Fetched");
			rs.close();
			System.out.println("ResultSet Closed");
			ps.close();
			System.out.println("PreparedStatement Closed");
			if(conn != null) {
				conn.close();
				System.out.println("Connection Closed");
			}
			}
		catch(Exception e) {
			System.out.println("Connection Errror "+e.getMessage());
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public Book getBook(String name,String author) {
		Book book = null;
		try {
			System.out.println("Establishing Connection");
			Connection conn = getConnection();
			System.out.println("Connection Established");
			String selectQuery = "select * from  book WHERE name=? and author =?";
			System.out.println("Executing query");
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, name);
			ps.setString(2, author);
			System.out.println("Fetching result");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				book = new Book(rs.getString("name"),rs.getString("author"));			
			}
			System.out.println("Result Fetched");
			rs.close();
			System.out.println("ResultSet Closed");
			ps.close();
			System.out.println("PreparedStatement Closed");
			if(conn != null) {
				conn.close();
				System.out.println("Connection Closed");
			}
			}
		catch(Exception e) {
			System.out.println("Connection Errror "+e.getMessage());
			e.printStackTrace();
		}
		return book;
	}

    
    
}
