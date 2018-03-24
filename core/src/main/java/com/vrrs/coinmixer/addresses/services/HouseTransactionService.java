package com.vrrs.coinmixer.addresses.services;

import java.util.Map;

import com.vrrs.coinmixer.addresses.domain.WithdrawalTransaction;
import com.vrrs.coinmixer.coins.clients.CoinClient;

public final class HouseTransactionService {
	
	private final AddressPool addressPool;
	private final CoinClient coinClient;
	private final WithdrawalStrategy withdrawalStrategy;

	public HouseTransactionService(AddressPool addressPool, CoinClient coinClient, WithdrawalStrategy withdrawalStrategy) {
		this.addressPool = addressPool;
		this.coinClient = coinClient;
		this.withdrawalStrategy = withdrawalStrategy;
	}

	public void depositToHouseAddress() {
		final String houseAddress = addressPool.getHouseAddress();
		for(String depositAddress : addressPool.getDepositAddresses()) {
			double balance = coinClient.getBalance(depositAddress);
			coinClient.deposit(depositAddress, houseAddress, balance);
			addressPool.enqueueWithdrawal(depositAddress, balance);
		}
	}
	
	public void withdrawFromHouseAddress() {
		final String houseAddress = addressPool.getHouseAddress();
		while(!addressPool.isWithdrawalQueueEmpty()) {
			WithdrawalTransaction withdrawlTransaction = addressPool.dequeueWithdrawal();
			withdraw(houseAddress, withdrawlTransaction);
		}
	}
	
	public void withdrawFromHouseAddress(String address) {
		String depositAddress = addressPool.getMixedAddresses(address).getDepositAddress();
		final String houseAddress = addressPool.getHouseAddress();
		addressPool.dequeueWithdrawal(depositAddress, w -> withdraw(houseAddress, w));
	}

	private void withdraw(String address, WithdrawalTransaction withdrawlTransaction) {
		Map<String, Double> withdrawalPerSourceAddress = withdrawalStrategy.getWithdrawalPerSourceAddress(
				withdrawlTransaction.getMixedAddresses().getSourceAddresses(),
				withdrawlTransaction.getWithdrawlAmount());
		for (Map.Entry<String, Double> sourceAddressAndAmount : withdrawalPerSourceAddress.entrySet()) {
			String sourceAddress = sourceAddressAndAmount.getKey();
			Double amountToWithdraw = sourceAddressAndAmount.getValue();
			coinClient.deposit(address, sourceAddress, amountToWithdraw);
		}
	}

}
