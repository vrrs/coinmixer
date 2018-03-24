package com.vrrs.coinmixer.addresses.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawalTransactionStatus {
	
	private final boolean withdrawn;
	
	@JsonCreator
	public WithdrawalTransactionStatus(@JsonProperty("withdrawn") boolean withdrawn) {
		this.withdrawn = withdrawn;
	}

	@JsonProperty("withdrawn")
	public boolean isWithdrawn() {
		return withdrawn;
	}

}
