package com.niemar.revolut;

import com.niemar.revolut.datasource.AccountInMemory;
import com.niemar.revolut.resources.AccountResource;
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
        final AccountResource accountResource = new AccountResource(new AccountInMemory());
        environment.jersey().register(accountResource);
    }
}
