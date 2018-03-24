package com.vrrs.coinmixer.addresses.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MixedAddresses {
	
	private final String depositAddress;
	private final List<String> sourceAddresses;
	
	@JsonCreator
	public MixedAddresses(@JsonProperty("deposit_addresses") String depositAddress, @JsonProperty("source_addresses") List<String> sourceAddresses) {
		this.sourceAddresses = sourceAddresses;
		this.depositAddress = depositAddress;
	}

	@JsonProperty("source_addresses")
	public List<String> getSourceAddresses() {
		return sourceAddresses;
	}
	@JsonProperty("deposit_addresses")
	public String getDepositAddress() {
		return depositAddress;
	}
	@Override
	public int hashCode() {
		return Objects.hash(depositAddress, sourceAddresses);
	}
	@Override
	public boolean equals(Object other) {
		if(other instanceof MixedAddresses) {
			MixedAddresses that = (MixedAddresses) other;
			return Objects.equals(this.depositAddress, that.getDepositAddress())
					&& Objects.equals(this.sourceAddresses, that.getSourceAddresses());
		} else {
			return false;
		}
	}

}
