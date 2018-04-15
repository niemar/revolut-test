package com.niemar.revolut.resources;

import com.niemar.revolut.api.Account;
import com.niemar.revolut.datasource.AccountDAO;
import com.niemar.revolut.util.ResourceUtil;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private AccountDAO accountDAO;

    public AccountResource(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @GET
    @Path(ResourceUtil.ID_PATH)
    public Response getAccount(@PathParam(ResourceUtil.ID) String id) {
        Account account = accountDAO.findById(id);
        if (account == null) {
            return ResourceUtil.buildNotFoundResponse(id);
        }
        return Response.ok(account).build();
    }

    @GET
    public Response getAccounts() {
        List<Account> accounts = accountDAO.findAll();
        return Response.ok(accounts).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(@Valid Account account) {
        Account created = accountDAO.create(account);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path(ResourceUtil.ID_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAccount(@PathParam(ResourceUtil.ID) String id, Account account) {
        Account updated = accountDAO.update(id, account);
        if (updated == null) {
            return ResourceUtil.buildNotFoundResponse(id);
        }
        return Response.ok().entity(updated).build();
    }

    @DELETE
    @Path(ResourceUtil.ID_PATH)
    public Response deleteAccount(@PathParam(ResourceUtil.ID) String id) {
        Account deleted = accountDAO.delete(id);
        if (deleted == null) {
            return ResourceUtil.buildNotFoundResponse(id);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
