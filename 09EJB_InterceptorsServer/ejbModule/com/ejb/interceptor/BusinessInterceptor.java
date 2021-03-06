package com.ejb.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class BusinessInterceptor {

	@AroundInvoke
	public Object methodInterceptor(InvocationContext ctx)  throws Exception
	{
		System.out.println("**** Intercept call to LibraryPresistanceBean method ****"+ctx.getMethod().getName());
		return ctx.proceed();
	}
	
}
