package com.niemar.revolut.datasource;

import com.niemar.revolut.api.Transfer;
import com.niemar.revolut.util.IdUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransferInMemory implements TransferDAO {

    private Map<String, Transfer> transfers = new ConcurrentHashMap<>();

    @Override
    public Transfer findById(String id) {
        return transfers.get(id);
    }

    @Override
    public List<Transfer> findAll() {
        return new ArrayList<>(transfers.values());
    }

    @Override
    public Transfer create(Transfer transfer) {
        Transfer newTransfer = new Transfer(transfer.getFromAccount(), transfer.getToAccount(), transfer.getAmount(),
                transfer.getCurrency(), IdUtil.generateId(), transfer.getStatus());
        transfers.put(newTransfer.getId(), newTransfer);
        return newTransfer;
    }

}
