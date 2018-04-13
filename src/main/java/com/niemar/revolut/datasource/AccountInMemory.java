package com.niemar.revolut.datasource;

import com.niemar.revolut.api.Account;
import com.niemar.revolut.util.IdUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountInMemory implements AccountDAO {

    private Map<String, Account> accounts = new ConcurrentHashMap<>();

    public Account findById(String id) {
        return accounts.get(id);
    }

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    public Account create(Account account) {
        Account newAccount = new Account(IdUtil.generateId(), account.getBalance(), account.getCurrency());
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

    public Account delete(String id) {
        return accounts.remove(id);
    }
}
