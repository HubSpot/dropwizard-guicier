package com.hubspot.dropwizard.guicier.objects;

import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ComponentInvocationCounter {

  private final Multiset<String> invocations = LinkedHashMultiset.create();

  @Inject
  public ComponentInvocationCounter() {}

  public void inc(String name) {
    invocations.add(name);
  }

  public int count(String name) {
    return invocations.count(name);
  }
}
