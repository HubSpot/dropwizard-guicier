package com.hubspot.dropwizard.guicier.objects;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

public class StashesTestModule implements Module {

  public static final int STASHED_INT_VALUE = 42;

  @Override
  public void configure(Binder binder) {
    binder.bind(StashesTestResource.class);
  }

  @Stashed
  @Provides
  public int providesStashedInt() {
    return STASHED_INT_VALUE;
  }
}
