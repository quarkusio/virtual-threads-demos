package org.acme.pulsar.models;

public class Fraud {

	public String account;
	public double amount;

	public Fraud(String account, double amount) {
		this.account = account;
		this.amount = amount;
	}
}
