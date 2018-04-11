package com.niemar.revolut.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.math.BigDecimal;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final Account ACCOUNT = new Account("f2447ec2-f0b3-4c75-aa33-cacffa8ca38a", BigDecimal.valueOf(123.45), "USD");

    @Test
    public void serializesToJSON() throws Exception {
        final Account actual = ACCOUNT;

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/account.json"), Account.class));

        assertThat(MAPPER.writeValueAsString(actual)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final Account expected = ACCOUNT;

        Account actual = MAPPER.readValue(fixture("fixtures/account.json"), Account.class);

        assertThat(actual).isEqualTo(expected);
    }

}