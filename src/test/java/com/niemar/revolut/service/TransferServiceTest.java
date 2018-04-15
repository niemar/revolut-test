package com.niemar.revolut.service;

import com.niemar.revolut.api.Account;
import com.niemar.revolut.api.Transfer;
import com.niemar.revolut.datasource.AccountDAO;
import com.niemar.revolut.datasource.AccountInMemory;
import com.niemar.revolut.datasource.TransferInMemory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class TransferServiceTest {

    private static TransferService SERVICE;
    private static AccountDAO ACCOUNT_DAO;
    private static final Account MONEY_IN_USD = new Account(BigDecimal.valueOf(100.00), "USD");

    @Before
    public void setUp() {
        ACCOUNT_DAO = new AccountInMemory();
        SERVICE = new TransferService(new TransferInMemory(), ACCOUNT_DAO);
    }

    @Test
    public void createTransferTest() {
        Account from = ACCOUNT_DAO.create(MONEY_IN_USD);
        Account to = ACCOUNT_DAO.create(MONEY_IN_USD);
        BigDecimal moneyToTransfer = BigDecimal.valueOf(30.0);
        Transfer transfer = new Transfer(from.getId(), to.getId(), moneyToTransfer, "USD");

        Transfer completedTransfer = SERVICE.createTransfer(transfer);

        Assert.assertEquals(Transfer.Status.COMPLETED, completedTransfer.getStatus());
        Assert.assertEquals(BigDecimal.valueOf(70.0), getBalanceAfterTransfer(from));
        Assert.assertEquals(BigDecimal.valueOf(130.0), getBalanceAfterTransfer(to));
    }

    @Test
    public void transferShouldBeDeclinedWhenCurrenciesAreDifferent() {
        Account from = ACCOUNT_DAO.create(MONEY_IN_USD);
        Account to = ACCOUNT_DAO.create(new Account(BigDecimal.valueOf(100.00), "EUR"));
        BigDecimal moneyToTransfer = BigDecimal.valueOf(30.0);
        Transfer transfer = new Transfer(from.getId(), to.getId(), moneyToTransfer, "USD");

        Transfer completedTransfer = SERVICE.createTransfer(transfer);
        Assert.assertEquals(Transfer.Status.DECLINED, completedTransfer.getStatus());
    }

    @Test
    public void transferShouldBeDeclinedWhenFromBalanceIsToSmall() {
        Account from = ACCOUNT_DAO.create(MONEY_IN_USD);
        Account to = ACCOUNT_DAO.create(MONEY_IN_USD);
        BigDecimal moneyToTransfer = BigDecimal.valueOf(200.0);
        Transfer transfer = new Transfer(from.getId(), to.getId(), moneyToTransfer, "USD");

        Transfer completedTransfer = SERVICE.createTransfer(transfer);
        Assert.assertEquals(Transfer.Status.DECLINED, completedTransfer.getStatus());
    }

    @Test
    public void transferShouldBeDeclinedWhenTransferAmountIsNegative() {
        Account from = ACCOUNT_DAO.create(MONEY_IN_USD);
        Account to = ACCOUNT_DAO.create(MONEY_IN_USD);
        BigDecimal moneyToTransfer = BigDecimal.valueOf(-20.0);
        Transfer transfer = new Transfer(from.getId(), to.getId(), moneyToTransfer, "USD");

        Transfer completedTransfer = SERVICE.createTransfer(transfer);
        Assert.assertEquals(Transfer.Status.DECLINED, completedTransfer.getStatus());
    }

    @Test
    public void transferShouldBeDeclinedWhenFromAccountDoesntExist() {
        Account to = ACCOUNT_DAO.create(MONEY_IN_USD);
        BigDecimal moneyToTransfer = BigDecimal.valueOf(20.0);
        Transfer transfer = new Transfer("doesntExist", to.getId(), moneyToTransfer, "USD");

        Transfer completedTransfer = SERVICE.createTransfer(transfer);
        Assert.assertEquals(Transfer.Status.DECLINED, completedTransfer.getStatus());
    }

    @Test
    public void transferShouldBeDeclinedWhenToAccountDoesntExist() {
        Account from = ACCOUNT_DAO.create(MONEY_IN_USD);
        BigDecimal moneyToTransfer = BigDecimal.valueOf(20.0);
        Transfer transfer = new Transfer(from.getId(), "doesntExist", moneyToTransfer, "USD");

        Transfer completedTransfer = SERVICE.createTransfer(transfer);
        Assert.assertEquals(Transfer.Status.DECLINED, completedTransfer.getStatus());
    }

    private BigDecimal getBalanceAfterTransfer(Account account) {
        return ACCOUNT_DAO.findById(account.getId()).getBalance();
    }

    @Test
    public void testCancelTransfer() {
        Account from = ACCOUNT_DAO.create(MONEY_IN_USD);
        Account to = ACCOUNT_DAO.create(MONEY_IN_USD);
        BigDecimal moneyToTransfer = BigDecimal.valueOf(30.0);
        Transfer transfer = new Transfer(from.getId(), to.getId(), moneyToTransfer, "USD");

        Transfer completedTransfer = SERVICE.createTransfer(transfer);
        Transfer cancellation = Transfer.createOppositeTransfer(completedTransfer);
        Transfer result = SERVICE.createTransfer(cancellation);

        Assert.assertEquals(Transfer.Status.COMPLETED, result.getStatus());
        Assert.assertEquals(BigDecimal.valueOf(100.0), getBalanceAfterTransfer(from));
        Assert.assertEquals(BigDecimal.valueOf(100.0), getBalanceAfterTransfer(to));
    }

    @Test
    public void testThatCannotCancelTransferWhenThereIsNoEnoughMoney() {
        Account from = ACCOUNT_DAO.create(MONEY_IN_USD);
        Account to = ACCOUNT_DAO.create(MONEY_IN_USD);
        BigDecimal moneyToTransfer = BigDecimal.valueOf(30.0);
        Transfer transfer = new Transfer(from.getId(), to.getId(), moneyToTransfer, "USD");

        Transfer completedTransfer = SERVICE.createTransfer(transfer);
        Transfer cancellation = Transfer.createOppositeTransfer(completedTransfer);
        ACCOUNT_DAO.update(to.getId(), new Account(BigDecimal.valueOf(1.00), "USD"));
        Transfer result = SERVICE.createTransfer(cancellation);


        Assert.assertEquals(Transfer.Status.DECLINED, result.getStatus());
        Assert.assertEquals(BigDecimal.valueOf(70.0), getBalanceAfterTransfer(from));
        Assert.assertEquals(BigDecimal.valueOf(1.0), getBalanceAfterTransfer(to));
    }
}