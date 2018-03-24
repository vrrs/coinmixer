package com.vrrs.coinmixer;

import org.hibernate.validator.constraints.NotEmpty;

import io.dropwizard.Configuration;

public class CoinMixerAppConfig extends Configuration {
	
	@NotEmpty private String jobCoinBaseURL;
	@NotEmpty private String houseAddress;

	public String getJobCoinBaseURL() {
		return jobCoinBaseURL;
	}

	public void setJobCoinBaseURL(String jobCoinBaseURL) {
		this.jobCoinBaseURL = jobCoinBaseURL;
	}

	public String getHouseAddress() {
		return houseAddress;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}

}
