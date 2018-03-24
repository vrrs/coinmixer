package com.vrrs.coinmixer.addresses.services;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.vrrs.coinmixer.addresses.domain.MixedAddressesWithBalances;
import com.vrrs.coinmixer.coins.clients.CoinClient;
import com.vrrs.coinmixer.coins.clients.JobCoinClient;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

public class TestAddressesServiceFacade {
	
	private static final String HOUSE_ADDRESS = "victorAddress01";
	private static final List<String> ALICE_ADDRESSES = ImmutableList.of("aliceAddress01", "aliceAddress02", "aliceAddress03",
			"aliceAddress04", "aliceAddress05");
	private static final List<String> BOB_ADDRESSES = ImmutableList.of("BOBAddress01", "BOBAddress02", "BOBAddress03",
			"BOBAddress04", "BOBAddress05");
	private static final String VICTOR_ADDRESS = "victorAddress02";

	@Test
	public void willMixAliceAddresses() {
		final String depositAddress = RandomStringUtils.randomAlphabetic(10);
		CoinClient jobCoinClient = new JobCoinClient("http://jobcoin.gemini.com/clammy/", () -> depositAddress);
		AddressesServiceFacade serviceFacade = newAddressServiceFacade(depositAddress, jobCoinClient);
		
		MixedAddressesWithBalances aliceMixedAddresses = serviceFacade.getNewMixedAddresses(BOB_ADDRESSES);
		
		assertThat(aliceMixedAddresses.getDepositAddress()).isEqualTo(depositAddress);
		assertThat(aliceMixedAddresses.getTotalBalance()).isEqualTo(0);
		assertThat(serviceFacade.getMixedAddresses(BOB_ADDRESSES.get(4)))
			.isEqualTo(aliceMixedAddresses);
	}

	private AddressesServiceFacade newAddressServiceFacade(final String depositAddress, CoinClient jobCoinClient) {
		AddressPool addressPool = new AddressPool(HOUSE_ADDRESS);
		WithdrawalStrategy withdrawalStrategy = new WithdrawalStrategy.RandomFractionalKnapsackStrategy();
		HouseTransactionService houseTransactionService = new HouseTransactionService(addressPool, jobCoinClient, withdrawalStrategy);
		AddressesServiceFacade serviceFacade = new AddressesServiceFacade(addressPool,houseTransactionService, jobCoinClient);
		return serviceFacade;
	}
	
	@Test
	public void willInitiateWithdrawlOfMixedAliceAddresses() {
		final String depositAddress = RandomStringUtils.randomAlphabetic(10);
		CoinClient jobCoinClient = new JobCoinClient("http://jobcoin.gemini.com/clammy/", () -> depositAddress);
		AddressesServiceFacade serviceFacade = newAddressServiceFacade(depositAddress, jobCoinClient);
		
		serviceFacade.getNewMixedAddresses(ALICE_ADDRESSES);
		jobCoinClient.deposit(VICTOR_ADDRESS, depositAddress, 2);
		assertThat(serviceFacade.initiateWithdraw(ALICE_ADDRESSES.get(3)).isWithdrawn())
			.isTrue();
	}

}
