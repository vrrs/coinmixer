package com.vrrs.coinmixer.coins.clients;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class TestJobCoinClient {
	
	private final CoinClient jobCoinClient = new JobCoinClient("http://jobcoin.gemini.com/clammy/");
	
	@Test
	public void willRetrieveBalanceOfAliceSister() {
		final String aliceSisterAddress = "KSkPEAmopl";
		assertThat(jobCoinClient.getBalance(aliceSisterAddress))
		.isNotEqualTo(0);
	}
	
	@Test
	public void willCreateNewAddress() {
		String newAddress = jobCoinClient.newAddress();
		assertThat(jobCoinClient.getBalance(newAddress)).isEqualTo(50);
	}
	
	@Test
	public void willSendCoinsFromAliceToBob() {
		final String aliceAddress = "Alice";
		final String bobAddress = "Bob";
		
		jobCoinClient.deposit(aliceAddress, bobAddress, 2);
		assertThat(jobCoinClient.getBalance(aliceAddress)).isEqualTo(35.5);
		assertThat(jobCoinClient.getBalance(bobAddress)).isEqualTo(14.5);
		
		jobCoinClient.deposit(bobAddress, aliceAddress, 2);
		assertThat(jobCoinClient.getBalance(aliceAddress)).isEqualTo(37.5);
		assertThat(jobCoinClient.getBalance(bobAddress)).isEqualTo(12.5);
	}

}
