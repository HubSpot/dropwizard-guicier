package com.hubspot.dropwizard.guicier.objects;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ExplicitDAO.class);
        bindConstant().annotatedWith(Names.named("TestTaskName")).to("test task");

        bind(InjectedManaged.class).asEagerSingleton();
        bind(InjectedTask.class).asEagerSingleton();
        bind(InjectedHealthCheck.class).asEagerSingleton();
        bind(InjectedServerLifecycleListener.class).asEagerSingleton();

        bind(InjectedProvider.class);

        bind(ExplicitResource.class);
    }
}

