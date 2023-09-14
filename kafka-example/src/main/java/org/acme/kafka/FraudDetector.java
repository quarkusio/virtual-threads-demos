package org.acme.kafka;

import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.timeseries.AddArgs;
import io.quarkus.redis.datasource.timeseries.Aggregation;
import io.quarkus.redis.datasource.timeseries.CreateArgs;
import io.quarkus.redis.datasource.timeseries.DuplicatePolicy;
import io.quarkus.redis.datasource.timeseries.RangeArgs;
import io.quarkus.redis.datasource.timeseries.TimeSeriesCommands;
import io.quarkus.redis.datasource.timeseries.TimeSeriesRange;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.acme.kafka.accounts.AccountRepository;
import org.acme.kafka.observability.ObservabilityTracker;
import org.acme.kafka.models.Fraud;
import org.acme.kafka.models.Transaction;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.time.Duration;
import java.time.ZoneOffset;

/**
 * Analyze transactions and detect frauds.
 * <p>
 * This code has been implemented to use more I/O than necessary.
 * It allows the virtual thread to yield twice and to increase the concurrency in terms of number of virtual threads.
 */
@ApplicationScoped
public class FraudDetector {

	private final TimeSeriesCommands<String> timeseries;
	private final ObservabilityTracker tracker;
	private final AccountRepository repository;
	private final KeyCommands<String> keys;


	public FraudDetector(RedisDataSource ds, AccountRepository repository, ObservabilityTracker tracker) {
		this.tracker = tracker;
		this.timeseries = ds.timeseries();
		this.keys = ds.key();
		this.repository = repository;
	}

	/**
	 * Creates a time series per account.
	 *
	 * @param ev the startup event, ignored.
	 */
	public void init(@Observes StartupEvent ev) {
		for (String account : repository.getAllAccounts()) {
			String key = "account:transactions:" + account;
			if (!keys.exists(key)) {
				timeseries.tsCreate(key,
						new CreateArgs().duplicatePolicy(DuplicatePolicy.SUM)
								.setRetention(Duration.ofDays(1)));
			}
		}
	}


	/**
	 * Receives and analyzes the transaction.
	 * Each transaction is processed on a separate virtual threads.
	 *
	 * @param tx the transaction
	 * @return a {@code Fraud} is a fraud is detected (and sent downstream), {@code null} otherwise (so no emission).
	 */
	@Incoming("tx")
	@Outgoing("frauds")
	@RunOnVirtualThread
	public Fraud detect(Transaction tx) {
		tracker.inc();
		try {
			String key = "account:transactions:" + tx.account;

			// Add sample
			long timestamp = tx.date.toInstant(ZoneOffset.UTC).toEpochMilli();
			timeseries.tsAdd(key, timestamp, tx.amount, new AddArgs().onDuplicate(DuplicatePolicy.SUM));

			// Retrieve the last sum.
			var range = timeseries.tsRevRange(key, TimeSeriesRange.fromTimeSeries(),
					// 1 min for demo purpose.
					new RangeArgs().aggregation(Aggregation.SUM, Duration.ofMinutes(1))
							.count(1));

			if (!range.isEmpty()) {
				// Analysis
				var sum = range.get(0).value;
				if (sum > 10_000) {
					Log.warnf("Fraud detected for account %s: %.2f", tx.account, sum);
					tracker.fraud();
					return new Fraud(tx.account, sum);
				}
			}
			return null;
		} finally {
			tracker.dec();
		}
	}

}
