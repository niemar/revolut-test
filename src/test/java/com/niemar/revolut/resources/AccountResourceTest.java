package com.niemar.revolut.resources;

import com.niemar.revolut.api.Account;
import com.niemar.revolut.datasource.AccountDAO;
import com.niemar.revolut.datasource.AccountInMemory;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        Assert.assertEquals(MONEY_IN_USD, resources.target("/accounts/f2447ec2-f0b3-4c75-aa33-cacffa8ca38a").request().get(Account.class));
    }

    @Test
    public void getAccounts() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("f2447ec2-f0b3-4c75-aa33-cacffa8ca38a", BigDecimal.valueOf(123.45), "USD"));
        accounts.add(new Account("a341c615-1d03-4e09-bd9b-0883bb702fb8", BigDecimal.valueOf(456.12), "EUR"));

        when(ACCOUNT_DAO.findAll()).thenReturn(accounts);


        Assert.assertEquals(accounts, resources.target("/accounts").request().get(List.class));
    }

    @Test
    public void createAccount() {
    }

    @Test
    public void updateAccount() {
    }

    @Test
    public void deleteAccount() {
    }
}