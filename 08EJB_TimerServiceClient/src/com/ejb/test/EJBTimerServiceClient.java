package com.ejb.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ejb.sessionbean.TimerServiceSessionBean;
import com.ejb.sessionbean.TimerServiceSessionBeanLocal;
import com.ejb.sessionbean.TimerServiceSessionBeanRemote;

public class EJBTimerServiceClient {
	
	private BufferedReader bf = null;
	private Properties props;
	private InitialContext ctx;
	
	// Instant Initialization block (IIB)
	{
		try {
			props = new Properties();
			FileReader propsFile = new FileReader("jndi.properties"); 
			props.load(propsFile);
			ctx = new InitialContext(props);//props
			bf = new BufferedReader(new InputStreamReader(System.in));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showGui()
	{
		System.out.println("#######################");
		System.out.println("Welcome To Timer Service");
		System.out.println("#######################");
	}
	
	private void testTimerServiceEjb() 
	{
		try {
			boolean isRunning = true;
			TimerServiceSessionBeanRemote timerServiceSessionBeanRemote = (TimerServiceSessionBeanRemote) ctx.lookup("08EJB_TimerService/TimerServiceSessionBean/remote");
			//TimerServiceSessionBeanLocal timerServiceSessionLocal = (TimerServiceSessionBeanLocal) ctx.lookup("08EJB_TimerService/TimerServiceSessionBean/local");
			while(isRunning) {
				System.out.println("Enter Time In MillisSeconds");
				String userInput = bf.readLine();
				if(userInput.matches("[0-9]*"))
				{
					long timeInMillisSec = Long.parseLong(userInput);
					System.out.println("Enter Message");
					String msg = bf.readLine();
					timerServiceSessionBeanRemote.createTimer(timeInMillisSec, msg);
					//timerServiceSessionLocal.createTimer(timeInMillisSec, msg);
					isRunning = false;
				}
				else
				{
					System.out.println("Please enter Valid Time In MillisSeconds");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(bf != null) 
			{
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		EJBTimerServiceClient ejbTimerServiceClient = new EJBTimerServiceClient();
		ejbTimerServiceClient.testTimerServiceEjb();
	}
	
}
