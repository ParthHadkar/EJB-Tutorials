package com.ejb.sessionbean;

import javax.ejb.Local;
import javax.ejb.Timer;

@Local
public interface TimerServiceSessionBeanLocal {
	
	public void createTimer(long timeInMillisSec,String msg);
	
	public void timeOutHandler(Timer timer);
	
}
