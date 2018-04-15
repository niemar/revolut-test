package com.niemar.revolut.integration;

import com.niemar.revolut.MoneyTransferApplication;
import com.niemar.revolut.MoneyTransferConfiguration;
import com.niemar.revolut.api.Account;
import com.niemar.revolut.api.ErrorResponse;
import com.niemar.revolut.api.Transfer;
import com.niemar.revolut.util.ResourceUtil;
import com.niemar.revolut.util.TestUtil;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;

public class IntegrationTest {

    private static final String HOST = "http://localhost:";
    private static final Account MONEY_IN_USD = new Account("f2447ec2-f0b3-4c75-aa33-cacffa8ca38a",
            BigDecimal.valueOf(100.00), "USD");

    @ClassRule
    public static final DropwizardAppRule<MoneyTransferConfiguration> RULE =
            new DropwizardAppRule<>(MoneyTransferApplication.class,
                    resourceFilePath("config.yaml"));

    @Test
    public void createAndGetAccount() {
        Account created = createAccount(MONEY_IN_USD);
        Account getAccount = getAccount(created);

        TestUtil.assertEquals(MONEY_IN_USD, getAccount);
    }

    @Test
    public void createAndDeleteAccount() {
        Account created = createAccount(MONEY_IN_USD);
        Response response = RULE.client().target(HOST + RULE.getLocalPort() + "/accounts/"
                + created.getId()).request().delete();

        Response actual = RULE.client().target(HOST + RULE.getLocalPort() + "/accounts/"
                + created.getId()).request().get();

        Assert.assertEquals(Response.Status.NO_CONTENT.toString(), response.getStatusInfo().toString());
        Assert.assertEquals(Response.Status.NOT_FOUND.toString(), actual.getStatusInfo().toString());
        Assert.assertEquals(ResourceUtil.createErrorResponse(created.getId()), actual.readEntity(ErrorResponse.class));
    }

    private Account getAccount(Account created) {
        return RULE.client().target(HOST + RULE.getLocalPort() + "/accounts/"
                + created.getId()).request().get(Account.class);
    }

    @Test
    public void transferMoney() {
        Account from = createAccount(MONEY_IN_USD);
        Account to = createAccount(MONEY_IN_USD);

        BigDecimal moneyToTransfer = BigDecimal.valueOf(30.0);
        Transfer transfer30USD = new Transfer(from.getId(), to.getId(), moneyToTransfer, "USD");

        Transfer completedTransfer = RULE.client().target(HOST + RULE.getLocalPort() + "/transfers").request()
                .post(Entity.json(transfer30USD), Transfer.class);

        Assert.assertEquals(Transfer.Status.COMPLETED, completedTransfer.getStatus());
        Assert.assertEquals(BigDecimal.valueOf(70.0), getAccount(from).getBalance());
        Assert.assertEquals(BigDecimal.valueOf(130.0), getAccount(to).getBalance());
    }

    @Test
    public void transferAndCancel() {
        Account from = createAccount(MONEY_IN_USD);
        Account to = createAccount(MONEY_IN_USD);

        BigDecimal moneyToTransfer = BigDecimal.valueOf(30.0);
        Transfer transfer30USD = new Transfer(from.getId(), to.getId(), moneyToTransfer, "USD");

        Transfer completedTransfer = RULE.client().target(HOST + RULE.getLocalPort() + "/transfers").request()
                .post(Entity.json(transfer30USD), Transfer.class);

        Transfer delete = RULE.client().target(HOST + RULE.getLocalPort() + "/transfers/" + completedTransfer.getId()).request()
                .delete(Transfer.class);

        Assert.assertEquals(Transfer.Status.COMPLETED, delete.getStatus());
        Assert.assertEquals(BigDecimal.valueOf(100.0), getAccount(from).getBalance());
        Assert.assertEquals(BigDecimal.valueOf(100.0), getAccount(to).getBalance());
    }

    private Account createAccount(Account account) {
        return RULE.client().target(HOST + RULE.getLocalPort() + "/accounts").request()
                .post(Entity.json(account), Account.class);
    }
}