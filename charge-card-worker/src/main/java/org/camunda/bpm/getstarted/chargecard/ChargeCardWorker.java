package org.camunda.bpm.getstarted.chargecard;

import java.util.logging.Logger;

import org.camunda.bpm.client.ExternalTaskClient;

public class ChargeCardWorker {
	private final static Logger LOGGER = Logger.getLogger(ChargeCardWorker.class.getName());

	public static void main(String[] args) {
		ExternalTaskClient client = ExternalTaskClient.create().baseUrl("http://localhost:8080/engine-rest")
				.asyncResponseTimeout(10000) // long polling timeout
				.build();

		// subscribe to an external task topic as specified in the process
		client.subscribe("charge-card").lockDuration(1000) // the default lock duration is 20 seconds

				.handler((externalTask, externalTaskService) -> {
					// Get
					String name = (String) externalTask.getVariable("name");
					Long price = (Long) externalTask.getVariable("price");

					// Process
					LOGGER.info("Charging credit card :: price '" + price + "'$ of '" + name + "'...");

					// Complete
					externalTaskService.complete(externalTask);
				}).open();
	}
}