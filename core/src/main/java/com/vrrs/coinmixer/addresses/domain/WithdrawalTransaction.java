package com.vrrs.coinmixer.addresses.domain;

public final class WithdrawalTransaction {
	
	private final double withdrawlAmount;
	private final MixedAddresses mixedAddresses;

	public WithdrawalTransaction(double withdrawlAmount, MixedAddresses mixedAddresses) {
		this.withdrawlAmount = withdrawlAmount;
		this.mixedAddresses = mixedAddresses;
	}

	public double getWithdrawlAmount() {
		return withdrawlAmount;
	}

	public MixedAddresses getMixedAddresses() {
		return mixedAddresses;
	}
	
	@Override
	public int hashCode() {
		return mixedAddresses.hashCode();
	}
	@Override
	public boolean equals(Object other) {
		if(other instanceof WithdrawalTransaction) {
			WithdrawalTransaction that = (WithdrawalTransaction) other;
			return that.getMixedAddresses().equals(this.mixedAddresses);
		} else {
			return false;
		}
	}

}
