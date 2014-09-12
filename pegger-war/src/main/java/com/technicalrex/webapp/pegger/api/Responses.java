package com.technicalrex.webapp.pegger.api;

import javax.ws.rs.core.Response;

public class Responses {
    private Responses() { }

    public static final Response BAD_REQUEST = Response.status(Response.Status.BAD_REQUEST).build();

    public static final Response NOT_FOUND = Response.status(Response.Status.NOT_FOUND).build();

    public static final Response CONFLICT = Response.status(Response.Status.CONFLICT).build();
}
