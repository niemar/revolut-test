package com.niemar.revolut.service;

import com.niemar.revolut.api.Account;
import com.niemar.revolut.api.Transfer;
import com.niemar.revolut.datasource.AccountDAO;
import com.niemar.revolut.datasource.TransferDAO;

import java.math.BigDecimal;
import java.util.List;

public class TransferService {

    private final TransferDAO transferDAO;
    private final AccountDAO accountDAO;

    public TransferService(TransferDAO transferDAO, AccountDAO accountDAO) {
        this.transferDAO = transferDAO;
        this.accountDAO = accountDAO;
    }

    public Transfer findTransfer(String id) {
        return transferDAO.findById(id);
    }

    public Transfer createTransfer(Transfer transfer) {
        Account fromAccount = accountDAO.findById(transfer.getFromAccount());
        Account toAccount = accountDAO.findById(transfer.getToAccount());
        if (fromAccount == null || toAccount == null) {
            return createTransferWithStatus(transfer, Transfer.Status.DECLINED);
        }

        // deadlock prevention
        int result = fromAccount.getId().compareTo(toAccount.getId());
        Account lock1 = result > 0 ? fromAccount : toAccount;
        Account lock2 = result > 0 ? toAccount : fromAccount;

        synchronized (lock1) {
            synchronized (lock2) {
                // find again because accounts may have changed
                fromAccount = accountDAO.findById(transfer.getFromAccount());
                toAccount = accountDAO.findById(transfer.getToAccount());
                if (fromAccount == null || toAccount == null || !isTransferDataValid(transfer, fromAccount, toAccount)) {
                    return createTransferWithStatus(transfer, Transfer.Status.DECLINED);
                }

                BigDecimal moneyToTransfer = transfer.getAmount();

                BigDecimal updatedFromAccountBalance = fromAccount.getBalance().subtract(moneyToTransfer);
                Account updatedFromAccount = new Account(fromAccount.getId(), updatedFromAccountBalance, fromAccount.getCurrency());
                accountDAO.update(fromAccount.getId(), updatedFromAccount);

                BigDecimal updatedToAccountBalance = toAccount.getBalance().add(moneyToTransfer);
                Account updatedToAccount = new Account(toAccount.getId(), updatedToAccountBalance, toAccount.getCurrency());
                accountDAO.update(toAccount.getId(), updatedToAccount);
            }
        }
        return createTransferWithStatus(transfer, Transfer.Status.COMPLETED);
    }

    private Transfer createTransferWithStatus(Transfer transferData, Transfer.Status status) {
        return transferDAO.create(new Transfer(transferData.getFromAccount(), transferData.getToAccount(), transferData.getAmount(),
                transferData.getCurrency(), status));
    }

    private boolean isTransferDataValid(Transfer transfer, Account fromAccount, Account toAccount) {
        return isCurrencyTheSame(transfer.getCurrency(), fromAccount.getCurrency(), toAccount.getCurrency())
                && areAmountsValid(transfer.getAmount(), fromAccount.getBalance());
    }

    private boolean areAmountsValid(BigDecimal amount, BigDecimal fromAccountBalance) {
        return amount.doubleValue() > 0 && fromAccountBalance.doubleValue() > amount.doubleValue();
    }

    private boolean isCurrencyTheSame(String currency, String currency1, String currency2) {
        return currency.equals(currency1) && currency1.equals(currency2);
    }

    public Transfer cancelTransfer(String id) {
        Transfer transfer = findTransfer(id);
        if (transfer == null) {
            return null;
        }
        Transfer opposite = Transfer.createOppositeTransfer(transfer);
        return createTransfer(opposite);
    }

    public List<Transfer> findAll() {
        return transferDAO.findAll();
    }
}