package com.niemar.revolut.resources;

import com.niemar.revolut.api.Account;
import com.niemar.revolut.api.ErrorResponse;
import com.niemar.revolut.datasource.AccountDAO;
import com.niemar.revolut.datasource.AccountInMemory;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// TODO add more tests to verify returned status codes
public class AccountResourceTest {

    private static final AccountDAO ACCOUNT_DAO = mock(AccountInMemory.class);

    private static final Account MONEY_IN_USD = new Account("f2447ec2-f0b3-4c75-aa33-cacffa8ca38a", BigDecimal.valueOf(123.45), "USD");

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new AccountResource(ACCOUNT_DAO))
            .build();

    @Test
    public void getAccount() {
        when(ACCOUNT_DAO.findById(eq("f2447ec2-f0b3-4c75-aa33-cacffa8ca38a"))).thenReturn(MONEY_IN_USD);

        Account actual = resources.target("/accounts/f2447ec2-f0b3-4c75-aa33-cacffa8ca38a").request().get(Account.class);
        Assert.assertEquals(MONEY_IN_USD, actual);
    }

    @Test
    public void accountNotFound() {
        String id = "doesntExist";
        ErrorResponse expected = new ErrorResponse(AccountResource.ENTITY_NOT_FOUND_FOR_ID + id);
        when(ACCOUNT_DAO.findById(eq(id))).thenReturn(null);

        Response actual = resources.target("/accounts/" + id).request().get();

        Assert.assertEquals(Response.Status.NOT_FOUND, actual.getStatusInfo());
        Assert.assertEquals(expected, actual.readEntity(ErrorResponse.class));
    }


    @Test
    public void getAccounts() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("f2447ec2-f0b3-4c75-aa33-cacffa8ca38a", BigDecimal.valueOf(123.45), "USD"));
        accounts.add(new Account("a341c615-1d03-4e09-bd9b-0883bb702fb8", BigDecimal.valueOf(456.12), "EUR"));

        when(ACCOUNT_DAO.findAll()).thenReturn(accounts);


        List<Account> accountsList = resources.target("/accounts").request().get(new GenericType<List<Account>>() {
        });
        Assert.assertEquals(accounts, accountsList);
    }


    @Test
    public void createAccount() {
        Account money = new Account(BigDecimal.valueOf(123.45), "USD");
        when(ACCOUNT_DAO.create(eq(money))).thenReturn(MONEY_IN_USD);

        Account createdAccount = resources.target("/accounts").request().post(Entity.json(money), Account.class);

        Assert.assertEquals(MONEY_IN_USD, createdAccount);
    }

    @Test
    public void updateAccount() {
        Account money = new Account(BigDecimal.valueOf(123.45), "USD");
        when(ACCOUNT_DAO.update(eq(MONEY_IN_USD.getId()), eq(money))).thenReturn(MONEY_IN_USD);

        Account createdAccount = resources.target("/accounts/" + MONEY_IN_USD.getId()).request().put(Entity.json(money), Account.class);

        Assert.assertEquals(MONEY_IN_USD, createdAccount);
    }

    @Test
    public void deleteAccount() {
        when(ACCOUNT_DAO.delete(eq(MONEY_IN_USD.getId()))).thenReturn(MONEY_IN_USD);
        Response response = resources.target("/accounts/" + MONEY_IN_USD.getId()).request().delete();

        Assert.assertEquals(Response.Status.NO_CONTENT, response.getStatusInfo());
    }
}