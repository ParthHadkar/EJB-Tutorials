package com.ejb.sessionbean;

import javax.ejb.Remote;
import javax.ejb.Timer;

@Remote
public interface TimerServiceSessionBeanRemote {
	
	public void createTimer(long timeInMillisSec,String msg);
	
	public void timeOutHandler(Timer timer);

}
