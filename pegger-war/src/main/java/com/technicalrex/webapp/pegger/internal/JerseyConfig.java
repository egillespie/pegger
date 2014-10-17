package com.technicalrex.webapp.pegger.internal;

import com.technicalrex.webapp.pegger.api.ToolResource;
import com.technicalrex.webapp.pegger.api.games.GameResource;
import com.technicalrex.webapp.pegger.api.games.PegResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Enable Spring DI and Jackson configuration
        register(RequestContextFilter.class);
        register(JacksonObjectMapperConfig.class);

        // Application resources
        register(GameResource.class);
        register(PegResource.class);
        register(ToolResource.class);
    }
}
