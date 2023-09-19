package org.acme.kafka.observability;

import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

/**
 * Keep track of the concurrency (current and max), number of transaction and number of frauds.
 */
@ApplicationScoped
public class ObservabilityTracker {

	InternalTracker tracker = new InternalTracker();

	@Channel("observability")
	@OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 5000)
	MutinyEmitter<Tracker> emitter;

	public void fraud() {
		var c = tracker.fraud();
		emitter.sendAndAwait(c);
	}

	private static class InternalTracker {

		private int current = 0;
		private int max = 0;

		private long frauds;
		private long transactions;

		synchronized Tracker inc() {
			transactions++;
			current++;
			if (current > max) {
				max = current;
			}
			return Tracker.create(this);

		}

		synchronized Tracker dec() {
			current--;
			return Tracker.create(this);
		}

		synchronized Tracker fraud() {
			frauds++;
			return Tracker.create(this);
		}
	}

	public record Tracker(int c, int m, long tx, long f) {
		static Tracker create(InternalTracker c) {
			return new Tracker(c.current, c.max, c.transactions, c.frauds);
		}
	}

	public void inc() {
		var c = tracker.inc();
		emitter.sendAndAwait(c);
	}

	public void dec() {
		var c = tracker.dec();
		emitter.sendAndAwait(c);
	}

}
