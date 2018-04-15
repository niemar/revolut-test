package com.niemar.revolut.resources;

import com.niemar.revolut.api.Transfer;
import com.niemar.revolut.service.TransferService;
import com.niemar.revolut.util.ResourceUtil;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/transfers")
@Produces(MediaType.APPLICATION_JSON)
public class TransferResource {

    private final TransferService transferService;

    public TransferResource(TransferService transferService) {
        this.transferService = transferService;
    }

    @GET
    @Path(ResourceUtil.ID_PATH)
    public Response getTransfer(@PathParam(ResourceUtil.ID) String id) {
        Transfer transfer = transferService.findTransfer(id);
        if (transfer == null) {
            return ResourceUtil.buildNotFoundResponse(id);
        }
        return Response.ok(transfer).build();
    }

    @GET
    public Response getTransfers() {
        List<Transfer> transfers = transferService.findAll();
        return Response.ok(transfers).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTransfer(@Valid Transfer transfer) {
        Transfer created = transferService.createTransfer(transfer);
        if (created == null) {
            return ResourceUtil.buildNotFoundResponse(ResourceUtil.ID);
        }
        return Response.ok(created).build();
    }

    @DELETE
    @Path(ResourceUtil.ID_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cancelTransfer(@PathParam(ResourceUtil.ID) String id) {
        Transfer created = transferService.cancelTransfer(id);
        if (created == null) {
            return ResourceUtil.buildNotFoundResponse(ResourceUtil.ID);
        }
        return Response.ok(created).build();
    }


}
