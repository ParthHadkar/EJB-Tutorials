package com.ejb.sessionbean;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;

/**
 * Session Bean implementation class TimerServiceSessionBean
 */
@Stateless
public class TimerServiceSessionBean implements TimerServiceSessionBeanRemote, TimerServiceSessionBeanLocal {

	@Resource
	private SessionContext sessionContext;
    
    public TimerServiceSessionBean() {}

    @Override
	public void createTimer(long timeInMillisSec,String msg) {
    	sessionContext.getTimerService().createTimer(timeInMillisSec,msg);//"Welcome to TimerService"
		
	}

    @Timeout
    @Override
	public void timeOutHandler(Timer timer) {
		System.out.println("timeOutHandler : "+timer.getInfo());
		timer.cancel();
	}

}
