package org.zalando.apifirst.example.impl;

import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import de.zalando.api.UserApi;

import de.zalando.model.User;

import io.swagger.annotations.ApiParam;

public class UserApiImpl implements UserApi {

    @Override
    public Response createUser(@ApiParam(value = "Created user object") final User body) throws NotFoundException {
        return Response.ok().build();
    }

    @Override
    public Response createUsersWithArrayInput(@ApiParam(value = "List of user object") final List<User> body)
        throws NotFoundException {
        return Response.ok().build();
    }

    @Override
    public Response createUsersWithListInput(@ApiParam(value = "List of user object") final List<User> body)
        throws NotFoundException {
        return Response.ok().build();
    }

    @Override
    public Response loginUser(@ApiParam(value = "The user name for login") final String username,
            @ApiParam(value = "The password for login in clear text") final String password) throws NotFoundException {
        return Response.ok().build();
    }

    @Override
    public Response logoutUser() throws NotFoundException {
        return Response.ok().build();
    }

    @Override
    public Response getUserByName(
            @ApiParam(value = "The name that needs to be fetched. Use user1 for testing. ", required = true) final String username)
        throws NotFoundException {
        User user = new User();
        user.setUsername(username);
        return Response.ok(user).build();
    }

    @Override
    public Response updateUser(@ApiParam(value = "name that need to be deleted", required = true) final String username,
            @ApiParam(value = "Updated user object") final User body) throws NotFoundException {
        return Response.ok().build();
    }

    @Override
    public Response deleteUser(
            @ApiParam(value = "The name that needs to be deleted", required = true) final String username)
        throws NotFoundException {
        return Response.ok().build();
    }

}
