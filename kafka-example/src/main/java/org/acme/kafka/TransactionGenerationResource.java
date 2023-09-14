package org.acme.kafka;


import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.kafka.accounts.AccountRepository;
import org.acme.kafka.models.Transaction;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.jboss.resteasy.reactive.RestQuery;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * An endpoint to generate random transactions.
 */
@Path("/transactions")
@RunOnVirtualThread
public class TransactionGenerationResource {

	@Inject
	AccountRepository accounts;

	@Channel("transactions")
	@OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10000)
	MutinyEmitter<Transaction> emitter;

	/**
	 * Generates n random transaction.
	 * It does not wait for the message to be acknowledged by Kafka.
	 *
	 * @param count the number of transactions.
	 * @return the number of transactions.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public int generate(@RestQuery int count) {
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			var account = accounts.getRandomAccount();
			var amount = random.nextInt(1000);
			var tx = new Transaction(account, amount, LocalDateTime.now());
			send(tx);
		}
		return count;
	}

	@GET
	@Path("/fraud")
	public void generateFraud() {
		var account = accounts.getRandomAccount();
		for (int i = 0; i < 15; i++) {
			send(new Transaction(account, 1000, LocalDateTime.now()));
		}
	}

	private void send(Transaction tx) {
		emitter.sendAndForget(tx);
	}

}
