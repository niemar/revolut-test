package com.niemar.revolut.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Transfer {

    @NotEmpty
    private final String fromAccount;
    @NotEmpty
    private final String toAccount;
    private final String id;
    @DecimalMax(value = "1000000")
    @DecimalMin(value = "0")
    @NotNull
    private final BigDecimal amount;
    @NotEmpty
    private final String currency;
    private final Status status;

    @JsonCreator
    public Transfer(@JsonProperty("fromAccount") String fromAccount, @JsonProperty("toAccount") String toAccount,
                    @JsonProperty("amount") BigDecimal amount, @JsonProperty("currency") String currency,
                    @JsonProperty("id") String id, @JsonProperty("status") Status status) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.currency = currency;
        this.id = id;
        this.status = status;
    }

    public Transfer(String fromAccount, String toAccount, BigDecimal amount, String currency, Status status) {
        this(fromAccount, toAccount, amount, currency, null, status);
    }

    public Transfer(String fromAccount, String toAccount, BigDecimal amount, String currency) {
        this(fromAccount, toAccount, amount, currency, null, Status.PENDING);
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        PENDING, COMPLETED, DECLINED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transfer)) return false;

        Transfer transfer = (Transfer) o;

        if (fromAccount != null ? !fromAccount.equals(transfer.fromAccount) : transfer.fromAccount != null)
            return false;
        if (toAccount != null ? !toAccount.equals(transfer.toAccount) : transfer.toAccount != null) return false;
        if (id != null ? !id.equals(transfer.id) : transfer.id != null) return false;
        if (amount != null ? !amount.equals(transfer.amount) : transfer.amount != null) return false;
        if (currency != null ? !currency.equals(transfer.currency) : transfer.currency != null) return false;
        return status == transfer.status;
    }

    @Override
    public int hashCode() {
        int result = fromAccount != null ? fromAccount.hashCode() : 0;
        result = 31 * result + (toAccount != null ? toAccount.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", id='" + id + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status=" + status +
                '}';
    }

    public static Transfer createOppositeTransfer(Transfer transfer) {
        return new Transfer(transfer.getToAccount(), transfer.getFromAccount(), transfer.getAmount(),
                transfer.getCurrency());
    }
}