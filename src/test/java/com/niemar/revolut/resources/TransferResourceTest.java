package com.niemar.revolut.resources;

import com.niemar.revolut.api.Transfer;
import com.niemar.revolut.service.TransferService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferResourceTest {

    private static final TransferService SERVICE = mock(TransferService.class);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TransferResource(SERVICE))
            .build();

    private static final Transfer TRANSFER = new Transfer("AAAA7ec2-f0b3-4c75-aa33-cacffa8ca38a",
            "BBBB7ec2-f0b3-4c75-aa33-cacffa8ca38a", BigDecimal.valueOf(100.00), "USD",
            "f2447ec2-f0b3-4c75-aa33-cacffa8ca38a", Transfer.Status.COMPLETED);

    @Test
    public void getTransfer() {
        String id = TRANSFER.getId();
        when(SERVICE.findTransfer(eq(id))).thenReturn(TRANSFER);

        Transfer actual = resources.target("/transfers/" + id).request().get(Transfer.class);

        Assert.assertEquals(TRANSFER, actual);
    }

    @Test
    public void getTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(TRANSFER);
        transfers.add(new Transfer("AAAA7ec2-f0b3-4c75-aa33-cacffa8ca38a",
                "BBBB7ec2-f0b3-4c75-aa33-cacffa8ca38a", BigDecimal.valueOf(100.00), "USD",
                "12447ec2-f0b3-4c75-aa33-cacffa8ca38a", Transfer.Status.COMPLETED));
        when(SERVICE.findAll()).thenReturn(transfers);

        List<Transfer> actual = resources.target("/transfers/").request().get(new GenericType<List<Transfer>>() {
        });

        Assert.assertEquals(transfers, actual);
    }

    @Test
    public void createTransfer() {
        Transfer transfer = new Transfer("AAAA7ec2-f0b3-4c75-aa33-cacffa8ca38a",
                "BBBB7ec2-f0b3-4c75-aa33-cacffa8ca38a", BigDecimal.valueOf(100.00), "USD",
                Transfer.Status.COMPLETED);
        when(SERVICE.createTransfer(eq(transfer))).thenReturn(TRANSFER);

        Transfer created = resources.target("/transfers").request().post(Entity.json(transfer), Transfer.class);

        Assert.assertEquals(TRANSFER, created);
    }

    @Test
    public void cancelTransfer() {
        String id = TRANSFER.getId();
        when(SERVICE.cancelTransfer(eq(id))).thenReturn(TRANSFER);

        Transfer actual = resources.target("/transfers/" + id).request().delete(Transfer.class);

        Assert.assertEquals(TRANSFER, actual);
    }
}