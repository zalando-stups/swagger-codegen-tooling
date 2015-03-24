package org.zalando.apifirst.example;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import org.springframework.stereotype.Component;

import org.zalando.apifirst.example.impl.PetApiImpl;
import org.zalando.apifirst.example.impl.StoreApiImpl;
import org.zalando.apifirst.example.impl.UserApiImpl;

/**
 * @author  jbellmann
 */
@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        register(StoreApiImpl.class);
        register(UserApiImpl.class);

        register(PetApiImpl.class);

        register(MultiPartFeature.class);

        //
        property("jersey.config.server.wadl.disableWadl", true);
    }
}
