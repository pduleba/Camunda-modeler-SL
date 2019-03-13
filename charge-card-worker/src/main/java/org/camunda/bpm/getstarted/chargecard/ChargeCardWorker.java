package org.camunda.bpm.getstarted.chargecard;

import org.camunda.bpm.client.ExternalTaskClient;

import java.util.logging.Logger;

public class ChargeCardWorker {

    private final static Logger LOGGER = Logger.getLogger(ChargeCardWorker.class.getName());

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();

        // Subscribe
        client.subscribe("charge-card")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {

                    // Process
                    Object name = externalTask.getVariable("name");
                    Object price = externalTask.getVariable("price");
                    LOGGER.info("Payment " + price + "€ for '" + name);

                    // Complete
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}