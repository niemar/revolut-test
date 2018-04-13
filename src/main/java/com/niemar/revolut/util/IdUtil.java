package com.niemar.revolut.util;

import java.util.UUID;

public class IdUtil {

    private IdUtil() {
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
