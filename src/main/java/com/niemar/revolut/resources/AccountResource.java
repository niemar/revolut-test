package com.niemar.revolut.resources;

import com.niemar.revolut.api.Account;
import com.niemar.revolut.datasource.AccountDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    public Account getAccount(@PathParam(ID) String id) {
        return accountDAO.findById(id);
    }

    @GET
    public List<Account> getAccounts() {
        return accountDAO.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Account createAccount(Account account) {
        return accountDAO.create(account);
    }

    @PUT
    @Path(ID_PATH)
    public Account updateAccount(@PathParam(ID) String id, Account account) {
        return accountDAO.update(id, account);
    }

    @DELETE
    @Path(ID_PATH)
    public Account deleteAccount(@PathParam(ID) String id) {
        return accountDAO.delete(id);
    }
}
