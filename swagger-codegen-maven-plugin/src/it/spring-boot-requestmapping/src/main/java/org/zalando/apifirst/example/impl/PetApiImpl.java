package org.zalando.apifirst.example.impl;

import java.io.InputStream;

import java.util.List;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import de.zalando.api.NotFoundException;
import de.zalando.api.PetApi;

import de.zalando.model.Pet;

public class PetApiImpl implements PetApi {

    @Override
    public Response updatePet(final Pet body) throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response addPet(final Pet body) throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response findPetsByStatus(final List<String> status) throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response findPetsByTags(final List<String> tags) throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response getPetById(final Long petId) throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response updatePetWithForm(final String petId, final String name, final String status)
        throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response deletePet(final String api_key, final Long petId) throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response uploadFile(final Long petId, final String additionalMetadata, final InputStream inputStream,
            final FormDataContentDisposition fileDetail) throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }
}
