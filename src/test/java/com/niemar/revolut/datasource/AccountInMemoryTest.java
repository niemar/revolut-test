package com.niemar.revolut.datasource;

import com.niemar.revolut.api.Account;
import com.niemar.revolut.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class AccountInMemoryTest {

    private static final Account MONEY_IN_USD = new Account(BigDecimal.valueOf(123.45), "USD");
    private AccountDAO accountDAO;

    @Before
    public void createDao() {
        accountDAO = new AccountInMemory();
    }

    @Test
    public void create() {
        Account created = accountDAO.create(MONEY_IN_USD);

        TestUtil.assertEquals(MONEY_IN_USD, created);
    }

    @Test
    public void testThatIdIsGeneratedInternally() {
        Account accountData = new Account("f2447ec2-f0b3-4c75-aa33-cacffa8ca38a", BigDecimal.valueOf(123.45), "USD");

        Account created = accountDAO.create(accountData);

        Assert.assertNotEquals(accountData.getId(), created.getId());
    }

    @Test
    public void findAll() {
        accountDAO.create(MONEY_IN_USD);
        accountDAO.create(MONEY_IN_USD);

        List<Account> founded = accountDAO.findAll();

        Assert.assertEquals(2, founded.size());
    }

    @Test
    public void updateBalance() {
        Account created = accountDAO.create(MONEY_IN_USD);
        Account newAccountData = new Account(BigDecimal.valueOf(456.12), "EUR");

        Account updated = accountDAO.update(created.getId(), newAccountData);

        Assert.assertEquals(newAccountData.getBalance(), updated.getBalance());
    }

    @Test
    public void updateCurrency() {
        Account created = accountDAO.create(MONEY_IN_USD);
        Account newAccountData = new Account(BigDecimal.valueOf(456.12), "EUR");

        Account updated = accountDAO.update(created.getId(), newAccountData);

        Assert.assertEquals(newAccountData.getCurrency(), updated.getCurrency());
    }

    @Test
    public void updateNotExistingAccount() {
        Account newAccountData = new Account(BigDecimal.valueOf(456.12), "EUR");

        Account updated = accountDAO.update("noSuchId", newAccountData);

        Assert.assertNull(updated);
    }

    @Test
    public void testThatIdIsNeverUpdated() {
        Account created = accountDAO.create(MONEY_IN_USD);
        Account newAccountData = new Account("a341c615-1d03-4e09-bd9b-0883bb702fb8", BigDecimal.valueOf(456.12), "EUR");

        Account updated = accountDAO.update(created.getId(), newAccountData);

        Assert.assertEquals(created.getId(), updated.getId());
    }

    @Test
    public void delete() {
        Account created = accountDAO.create(MONEY_IN_USD);

        accountDAO.delete(created.getId());

        Assert.assertNull(accountDAO.findById(created.getId()));
    }
}