package com.niemar.revolut.util;

import com.niemar.revolut.api.ErrorResponse;

import javax.ws.rs.core.Response;

public class ResourceUtil {

    private ResourceUtil() {
    }

    public static final String ID_PATH = "{id}";
    public static final String ID = "id";
    private static final String ENTITY_NOT_FOUND_FOR_ID = "Entity not found for id: ";

    public static Response buildNotFoundResponse(String id) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(createErrorResponse(id))
                .build();
    }

    public static ErrorResponse createErrorResponse(String id) {
        return new ErrorResponse(ENTITY_NOT_FOUND_FOR_ID + id);
    }
}
