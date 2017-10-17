package com.hubspot.dropwizard.guicier.objects;

import com.google.inject.Inject;

public class ExplicitDAO {

    @Inject
    public ExplicitDAO() {}

    public String getMessage() {
        return "this DAO was bound explicitly";
    }

}
