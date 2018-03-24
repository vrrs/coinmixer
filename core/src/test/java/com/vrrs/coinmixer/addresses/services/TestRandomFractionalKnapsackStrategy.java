package com.vrrs.coinmixer.addresses.services;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

public class TestRandomFractionalKnapsackStrategy {
	
	private final WithdrawalStrategy rfkStrategy = new WithdrawalStrategy.RandomFractionalKnapsackStrategy();
	
	@Test
	public void willSplitRandomlyAlice50CoinIn5() {
		final double amount = 50;
		List<String> aliceAddresses = ImmutableList.of("aliceAddress01", "aliceAddress02", "aliceAddress03",
				"aliceAddress04", "aliceAddress05");
		Map<String, Double> coinDist = rfkStrategy.getWithdrawalPerSourceAddress(aliceAddresses, amount);

		assertThat(coinDist).hasSize(5);
		assertThat(coinDist.values().stream().mapToDouble(x -> x).sum()).isEqualTo(amount);
	}
	
	

}
