package com.vrrs.coinmixer.addresses.domain;

import java.util.List;

public class MixedAddressesWithBalances {

	private final double totalBalance;
	private final String depositAddress;
	private final List<AddressWithBalance> addressesWithBalance;

	public MixedAddressesWithBalances(double totalBalance, String depositAddress, List<AddressWithBalance> addressesWithBalance) {
		this.totalBalance = totalBalance;
		this.depositAddress = depositAddress;
		this.addressesWithBalance = addressesWithBalance; 
	}

	public double getTotalBalance() {
		return totalBalance;
	}

	public String getDepositAddress() {
		return depositAddress;
	}

	public List<AddressWithBalance> getAddressesWithBalance() {
		return addressesWithBalance;
	}

}
