package com.technicalrex.webapp.pegger.internal;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JacksonObjectMapperConfig implements ContextResolver<ObjectMapper> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(MapperFeature.AUTO_DETECT_CREATORS)
            .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
            .registerModule(new GuavaModule())
            .registerModule(new JodaModule());

    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        return OBJECT_MAPPER;
    }
}
