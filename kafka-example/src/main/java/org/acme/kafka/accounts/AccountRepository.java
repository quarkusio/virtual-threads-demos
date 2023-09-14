package org.acme.kafka.accounts;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class AccountRepository {

	private final List<String> accounts = new CopyOnWriteArrayList<>();

	public void init(@Observes StartupEvent ev) {
		for (int i = 0; i < 100; i++) {
			accounts.add(UUID.randomUUID().toString());
		}
	}

	public String getRandomAccount() {
		Random random = new Random();
		int idx = random.nextInt(100);
		return accounts.get(idx);
	}


	public List<String> getAllAccounts() {
		return accounts;
	}
}
