package com.niemar.revolut.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {

    private final String id;
    private final BigDecimal balance;
    private final String currency;

    @JsonCreator
    public Account(@JsonProperty("id") String id, @JsonProperty("balance") BigDecimal balance, @JsonProperty("currency") String currency) {
        this.id = id;
        this.balance = balance;
        this.currency = currency;
    }

    public Account(BigDecimal balance, String currency) {
        this(null, balance, currency);
    }

    public String getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        if (id != null ? !id.equals(account.id) : account.id != null) return false;
        if (balance != null ? !balance.equals(account.balance) : account.balance != null) return false;
        return currency != null ? currency.equals(account.currency) : account.currency == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }
}
