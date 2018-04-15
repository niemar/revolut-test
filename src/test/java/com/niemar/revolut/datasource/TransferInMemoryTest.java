package com.niemar.revolut.datasource;

import com.niemar.revolut.api.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class TransferInMemoryTest {

    private static final Transfer TRANSFER = new Transfer("AAAA7ec2-f0b3-4c75-aa33-cacffa8ca38a",
            "BBBB7ec2-f0b3-4c75-aa33-cacffa8ca38a", BigDecimal.valueOf(100.00), "USD", Transfer.Status.COMPLETED);

    private TransferDAO transferDAO;

    @Before
    public void createDAO() {
        transferDAO = new TransferInMemory();
    }

    @Test
    public void create() {
        Transfer created = transferDAO.create(TRANSFER);

        Assert.assertNotNull(created.getId());
        assertEqualsIgnoringId(TRANSFER, created);
    }

    private void assertEqualsIgnoringId(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getFromAccount(), actual.getFromAccount());
        Assert.assertEquals(expected.getToAccount(), actual.getToAccount());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
        Assert.assertEquals(expected.getCurrency(), actual.getCurrency());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    public void findById() {
        Transfer created = transferDAO.create(TRANSFER);

        Transfer founded = transferDAO.findById(created.getId());

        Assert.assertEquals(created, founded);
    }

    @Test
    public void findAll() {
        transferDAO.create(TRANSFER);
        transferDAO.create(TRANSFER);

        List<Transfer> all = transferDAO.findAll();

        Assert.assertEquals(2, all.size());
    }
}