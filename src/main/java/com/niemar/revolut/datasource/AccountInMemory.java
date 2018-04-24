package com.niemar.revolut.datasource;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.niemar.revolut.api.Account;
import com.niemar.revolut.util.IdUtil;

import java.util.ArrayList;
import java.util.List;

public class AccountInMemory implements AccountDAO {

    private BiMap<String, Account> accounts = HashBiMap.create();

    public Account findById(String id) {
        return accounts.get(id);
    }

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    public Account create(Account account) {
        String id = IdUtil.generateId();
        Account newAccount = new Account(id, account.getBalance(), account.getCurrency());
        accounts.put(id, newAccount);
        return newAccount;
    }

    public Account update(String id, Account account) {
        if (id == null) {
            return null;
        }

        Account founded = findById(id);
        if (founded == null) {
            return null;
        }

        String lock = getLockFor(founded);
        if (lock == null) {
            return null;
        }

        synchronized (lock) {
            // find again to avoid changes like deletion in other thread
            founded = findById(id);
            if (founded == null) {
                return null;
            }

            Account newData = new Account(id, account.getBalance(), account.getCurrency());
            accounts.put(id, newData);
            return newData;
        }
    }



    /**
     * The reason of using id as lock than account is that id after account creation is not modified and is kept in
     * the memory until account exists.
     * @return Returns lock which is id of the account
     */
    public String getLockFor(Account account) {
        return accounts.inverse().get(account);
    }

    public Account delete(String id) {
        Account founded = findById(id);
        if (founded == null) {
            return null;
        }

        String lock = getLockFor(founded);
        if (lock == null) {
            return null;
        }

        synchronized (lock) {
            // no need to check changes in other thread if there is no such element method will return null
            return accounts.remove(id);
        }

    }

    private boolean contains(Account account) {
        return accounts.containsValue(account);
    }
}
