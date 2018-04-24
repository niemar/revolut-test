package com.niemar.revolut.datasource;

import com.niemar.revolut.api.Account;

import java.util.List;

public interface AccountDAO {

    Account findById(String id);
    List<Account> findAll();
    Account create(Account account);
    Account update(String id, Account account);
    Account delete(String id);
    String getLockFor(Account account);
}
