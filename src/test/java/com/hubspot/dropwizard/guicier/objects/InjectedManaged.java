package com.hubspot.dropwizard.guicier.objects;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.dropwizard.lifecycle.Managed;

@Singleton
public class InjectedManaged implements Managed {

    @Inject
    InjectedManaged() {}

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {

    }
}
