package com.ejb.listener;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import com.ejb.model.Book;
import com.ejb.sessionbean.LibraryPersistanceBean;
import com.ejb.sessionbean.LibrarySessionBeanRemote;

@MessageDriven(name="BookMessageHandler",
activationConfig = {
		@ActivationConfigProperty(propertyName="destinationType",propertyValue="javax.jms.Queue"),
		@ActivationConfigProperty(propertyName="destination",propertyValue="/queue/BookQueue")
		})
public class LibraryMessageBean implements MessageListener {
	
	@Resource
	private MessageDrivenContext messageDrivenContext;
	
	@EJB
	private LibrarySessionBeanRemote librarySessionBeanRemote;
	
	private int choice;
	
	 public LibraryMessageBean() { }

	public void onMessage(Message arg0) {
		try {
			if(arg0 instanceof TextMessage) {
				TextMessage tm = (TextMessage) arg0;
				choice = Integer.parseInt(tm.getText());
				System.out.println("Following Message is been recieved: "+choice);			
			}
			else if(arg0 instanceof ObjectMessage) {
				ObjectMessage tm = (ObjectMessage) arg0;
				Book book = (Book) tm.getObject();
				if(choice == 1) {
					librarySessionBeanRemote.addBook(book.getName(), book.getAuthor());
				}
				else if(choice == 2) {
					librarySessionBeanRemote.removeBook(book.getName(), book.getAuthor());
				}
				System.out.println("Following Message is been recieved: "+choice);			
			}
			
		}
		catch (Exception e) {
			System.out.println("JBosss Error: "+e);
			messageDrivenContext.setRollbackOnly();
			e.printStackTrace();
		}

	}

}
