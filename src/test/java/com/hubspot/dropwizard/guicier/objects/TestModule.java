package com.hubspot.dropwizard.guicier.objects;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ExplicitDAO.class);
        bindConstant().annotatedWith(Names.named("TestTaskName")).to("injected task");
        bindConstant().annotatedWith(Names.named("ProvidedTaskName")).to("provided task");

        bind(InjectedManaged.class).asEagerSingleton();
        bind(InjectedTask.class).asEagerSingleton();
        bind(InjectedHealthCheck.class).asEagerSingleton();
        bind(InjectedServerLifecycleListener.class).asEagerSingleton();

        bind(InjectedProvider.class);

        bind(ExplicitResource.class);
        bind(JerseyContextResource.class);
    }

    @Provides
    @Singleton
    public ProvidedManaged provideManaged() {
        return new ProvidedManaged();
    }

    @Provides
    @Singleton
    public ProvidedTask provideTask(@Named("ProvidedTaskName") String name) {
        return new ProvidedTask(name);
    }

    @Provides
    @Singleton
    public ProvidedHealthCheck provideHealthCheck() {
        return new ProvidedHealthCheck();
    }

    @Provides
    @Singleton
    public ProvidedServerLifecycleListener provideServerLifecycleListener() {
        return new ProvidedServerLifecycleListener();
    }

    @Provides
    public ProvidedProvider provideProvider() {
        return new ProvidedProvider();
    }
}

