package com.vrrs.coinmixer.addresses.services;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.vrrs.coinmixer.addresses.domain.AddressWithBalance;
import com.vrrs.coinmixer.addresses.domain.MixedAddresses;
import com.vrrs.coinmixer.addresses.domain.MixedAddressesWithBalances;
import com.vrrs.coinmixer.addresses.domain.WithdrawalTransactionStatus;
import com.vrrs.coinmixer.coins.clients.CoinClient;

public final class AddressesServiceFacade {
	
	private final AddressPool addressPool;
	private final HouseTransactionService houseTransactionService;
	private final CoinClient coinClient;

	public AddressesServiceFacade(AddressPool addressPool, HouseTransactionService houseTransactionService, CoinClient coinClient) {
		this.addressPool = addressPool;
		this.houseTransactionService = houseTransactionService;
		this.coinClient = coinClient;
	}

	public MixedAddressesWithBalances getNewMixedAddresses(List<String> sourceAddresses) {
		String depositAddress = coinClient.newAddress();
		addressPool.setMixedAddresses(new MixedAddresses(depositAddress, sourceAddresses));
		List<AddressWithBalance> sourceAddressesWithBalance = getSourceAddressesWithBalance(sourceAddresses, address -> 0d);
		return new MixedAddressesWithBalances(0, depositAddress, sourceAddressesWithBalance);
	}

	public MixedAddressesWithBalances getMixedAddresses(String address) {
		MixedAddresses mixedAddresses = addressPool.getMixedAddresses(address);
		List<AddressWithBalance> sourceAddressesWithBalance = getSourceAddressesWithBalance(
				mixedAddresses.getSourceAddresses(), coinClient::getBalance);
		double totalBalance = sourceAddressesWithBalance.stream().mapToDouble(AddressWithBalance::getBalance).sum();
		return new MixedAddressesWithBalances(totalBalance, mixedAddresses.getDepositAddress(),
				sourceAddressesWithBalance);
	}
	
	private List<AddressWithBalance> getSourceAddressesWithBalance(List<String> sourceAddresses,
			Function<String, Double> balance) {
		return sourceAddresses.stream().map(address -> new AddressWithBalance(address, balance.apply(address)))
				.collect(Collectors.toList());
	}

	public WithdrawalTransactionStatus initiateWithdraw(String address) {
		houseTransactionService.withdrawFromHouseAddress(address);
		return new WithdrawalTransactionStatus(true);
	}
	
	public MixedAddressesWithBalances getNewMixedAddressesAndWithdraw(List<String> sourceAddresses) {
		String depositAddress = coinClient.newAddress();
		addressPool.setMixedAddresses(new MixedAddresses(depositAddress, sourceAddresses));
		houseTransactionService.depositToHouseAddress();
		houseTransactionService.withdrawFromHouseAddress(depositAddress);
		return getMixedAddresses(depositAddress);
	}

}
