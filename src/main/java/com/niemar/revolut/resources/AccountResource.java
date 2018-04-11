package com.niemar.revolut.resources;

import com.niemar.revolut.api.Account;
import com.niemar.revolut.datasource.AccountDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private static final String ID_PATH = "{id}";
    private static final String ID = "id";

    private AccountDAO accountDAO;

    public AccountResource(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @GET
    @Path(ID_PATH)
    public Response getAccount(@PathParam(ID) String id) {
        Account account = accountDAO.findById(id);
        if (account == null) {
            return buildNotFoundResponse(id);
        }
        return Response.ok(account).build();
    }

    private Response buildNotFoundResponse(@PathParam(ID) String id) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Entity not found for id: " + id)
                .build();
    }

    @GET
    public Response getAccounts() {
        List<Account> accounts = accountDAO.findAll();
        return Response.ok(accounts).build();
    }

    //TODO validation of account
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account) {
        Account created = accountDAO.create(account);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path(ID_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAccount(@PathParam(ID) String id, Account account) {
        Account updated = accountDAO.update(id, account);
        if (updated == null) {
            return buildNotFoundResponse(id);
        }
        return Response.ok().entity(updated).build();
    }

    @DELETE
    @Path(ID_PATH)
    public Response deleteAccount(@PathParam(ID) String id) {
        Account deleted = accountDAO.delete(id);
        if (deleted == null) {
            return buildNotFoundResponse(id);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
