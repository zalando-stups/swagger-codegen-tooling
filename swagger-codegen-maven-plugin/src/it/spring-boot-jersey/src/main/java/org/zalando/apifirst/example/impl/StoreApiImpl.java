package org.zalando.apifirst.example.impl;

import javax.ws.rs.core.Response;

import de.zalando.api.NotFoundException;
import de.zalando.api.StoreApi;

import de.zalando.model.Order;

public class StoreApiImpl implements StoreApi {

    @Override
    public Response getInventory() throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response placeOrder(final Order body) throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response getOrderById(final String orderId) throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response deleteOrder(final String orderId) throws NotFoundException {

        // TODO Auto-generated method stub
        return null;
    }
}
