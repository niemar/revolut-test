package com.niemar.revolut.datasource;

import com.niemar.revolut.api.Transfer;

import java.util.List;

public interface TransferDAO {

    Transfer findById(String id);
    List<Transfer> findAll();
    Transfer create(Transfer transfer);
}
