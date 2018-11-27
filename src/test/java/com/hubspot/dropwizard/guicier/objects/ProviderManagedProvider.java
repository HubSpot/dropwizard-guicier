package com.hubspot.dropwizard.guicier.objects;

import javax.inject.Inject;
import javax.inject.Provider;

public class ProviderManagedProvider implements Provider<ProviderManaged> {
    @Inject
    public ProviderManagedProvider() {
    }

    @Override
    public ProviderManaged get() {
        return new ProviderManaged();
    }
}
