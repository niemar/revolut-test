package com.niemar.revolut.util;

import com.niemar.revolut.api.Account;
import org.junit.Assert;

public class TestUtil {

    public static void assertEquals(Account expected, Account actual) {
        Assert.assertNotNull(actual.getId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
        Assert.assertEquals(expected.getCurrency(), actual.getCurrency());
    }
}
