package org.acme.kafka.models;

import java.time.LocalDateTime;

public class Transaction {

	public String account;
	public double amount;
	public LocalDateTime date;

	public Transaction(String account, double amount, LocalDateTime date) {
		this.account = account;
		this.amount = amount;
		this.date = date;
	}
}
