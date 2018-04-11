package com.niemar.revolut;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

//TODO add tests
public class MoneyTransferConfiguration extends Configuration {



    private String template;

    @JsonCreator
    public MoneyTransferConfiguration(@JsonProperty("template") String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
