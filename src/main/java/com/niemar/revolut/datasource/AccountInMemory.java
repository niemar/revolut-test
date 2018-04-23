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

        Account lock = findById(id);
        if (lock == null) {
            return null;
        }

        synchronized (lock) {
            // don't update account that was already removed
            if (!contains(lock)) {
                return null;
            }

            Account newData = new Account(id, account.getBalance(), account.getCurrency());
            accounts.put(id, newData);
            return newData;
        }
    }

    public Account delete(String id) {
        Account lock = findById(id);
        if (lock == null) {
            return null;
        }

        synchronized (lock) {
            return accounts.remove(id);
        }

    }

    private boolean contains(Account account) {
        return accounts.containsValue(account);
    }
}
