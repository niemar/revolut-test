package com.niemar.revolut.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.math.BigDecimal;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class TransferTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();


    @Test
    public void serializesToJSON() throws Exception {
        final Transfer transfer = new Transfer("AAAA7ec2-f0b3-4c75-aa33-cacffa8ca38a",
                "BBBB7ec2-f0b3-4c75-aa33-cacffa8ca38a", BigDecimal.valueOf(123.45), "USD",
                "CCCC7ec2-f0b3-4c75-aa33-cacffa8ca38a", Transfer.Status.COMPLETED);

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/transfer.json"), Transfer.class));

        assertThat(MAPPER.writeValueAsString(transfer)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final Transfer expected = new Transfer("AAAA7ec2-f0b3-4c75-aa33-cacffa8ca38a",
                "BBBB7ec2-f0b3-4c75-aa33-cacffa8ca38a", BigDecimal.valueOf(123.45),
                "USD", "CCCC7ec2-f0b3-4c75-aa33-cacffa8ca38a", Transfer.Status.PENDING);

        Transfer actual = MAPPER.readValue(fixture("fixtures/transfer.json"), Transfer.class);

        assertThat(actual).isEqualTo(expected);
    }

}