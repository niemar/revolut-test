package com.niemar.revolut.datasource;

import com.niemar.revolut.api.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountInMemory implements AccountDAO {

    private Map<String, Account> accounts = new ConcurrentHashMap<String, Account>();


    public AccountInMemory() {
        //TODO remove
     //   accounts.put("12", new Account("12", BigDecimal.ONE, "USD"));
       // accounts.put("f5f17733-3152-4c83-888b-fa99ffc03591", new Account("f5f17733-3152-4c83-888b-fa99ffc03591", BigDecimal.TEN, "USD"));
        //accounts.put("14", new Account("14", BigDecimal.ZERO, "USD"));
    }


    public Account findById(String id) {
        return accounts.get(id);
    }

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    public Account create(Account account) {
        Account newAccount = new Account(generateId(), account.getBalance(), account.getCurrency());
        accounts.put(newAccount.getId(), newAccount);
        return newAccount;
    }

    public Account update(String id, Account account) {
        if (id == null) {
            return null;
        }
        // thread safe update
        return accounts.computeIfPresent(id, (idParam, updated) -> new Account(idParam, account.getBalance(), account.getCurrency()));

    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    public Account delete(String id) {
        return accounts.remove(id);
    }
}
