package org.acme.pulsar;

import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.acme.pulsar.models.Fraud;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.reactive.RestStreamElementType;

@Path("/frauds")
public class FraudResource {

	@Channel("frauds")
	Multi<Fraud> frauds;


	@GET
	@RestStreamElementType(MediaType.APPLICATION_JSON)
	public Multi<Fraud> stream() {
		return frauds;
	}
}
