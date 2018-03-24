package com.vrrs.coinmixer.addresses.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SourceAddresses {
	
	private final List<String> sourceAddresses;
	
	@JsonCreator
	public SourceAddresses(@JsonProperty("source_addresses") List<String> sourceAddresses) {
		this.sourceAddresses = sourceAddresses;
	}

	@JsonProperty("source_addresses")
	public List<String> getSourceAddresses() {
		return sourceAddresses;
	}

}
