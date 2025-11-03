package com.hubspot.dropwizard.guicier.aop;

import java.util.concurrent.atomic.AtomicInteger;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

class MyInterceptor implements MethodInterceptor {

  public final AtomicInteger counter = new AtomicInteger();

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    counter.incrementAndGet();
    return invocation.proceed();
  }
}
