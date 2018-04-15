package com.niemar.revolut;

import com.niemar.revolut.datasource.AccountInMemory;
import com.niemar.revolut.datasource.TransferInMemory;
import com.niemar.revolut.resources.AccountResource;
import com.niemar.revolut.resources.TransferResource;
import com.niemar.revolut.service.TransferService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MoneyTransferApplication extends Application<MoneyTransferConfiguration> {

    public static void main(String[] args) throws Exception {
        new MoneyTransferApplication().run(args);
    }

    @Override
    public String getName() {
        return "Money Transfer Application";
    }

    @Override
    public void initialize(Bootstrap<MoneyTransferConfiguration> bootstrap) {
        // nothing to do yet
    }

    public void run(MoneyTransferConfiguration moneyTransferApplication, Environment environment) {
        AccountInMemory accountInMemory = new AccountInMemory();
        final AccountResource accountResource = new AccountResource(accountInMemory);
        environment.jersey().register(accountResource);

        final TransferResource transferResource = new TransferResource(new TransferService(new TransferInMemory(), accountInMemory));
        environment.jersey().register(transferResource);
    }
}
