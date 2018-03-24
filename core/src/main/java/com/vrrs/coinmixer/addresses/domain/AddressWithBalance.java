package com.vrrs.coinmixer.addresses.domain;

public class AddressWithBalance {
	
	private final String address;
	private final double balance;
	
	public AddressWithBalance(String address, double balance) {
		this.address = address;
		this.balance = balance;
	}

	public String getAddress() {
		return address;
	}

	public double getBalance() {
		return balance;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof AddressWithBalance) {
			AddressWithBalance that = (AddressWithBalance) other;
			return this.getBalance() == that.getBalance() && 
					this.getAddress().equals(that.getAddress());
		} else {
			return false;
		}
	}

}
