package org.acme.kafka.observability;

import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.jboss.resteasy.reactive.RestStreamElementType;

/**
 * Provides the current and max concurrency, number of transactions and number of frauds as Server-Sent Events.
 */
@Path("/observability")
public class ObservabilityResource {

	@Channel("observability")
	@OnOverflow(OnOverflow.Strategy.DROP)
	Multi<ObservabilityTracker.Tracker> tracking;


	@GET
	@RestStreamElementType(MediaType.APPLICATION_JSON)
	public Multi<ObservabilityTracker.Tracker> stream() {
		return tracking;
	}
}
