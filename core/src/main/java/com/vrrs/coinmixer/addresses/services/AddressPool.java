package com.vrrs.coinmixer.addresses.services;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.vrrs.coinmixer.addresses.domain.MixedAddresses;
import com.vrrs.coinmixer.addresses.domain.WithdrawalTransaction;

import com.google.common.collect.Sets;

public final class AddressPool {
	
	private final String houseAddress;
	private final Map<String, MixedAddresses> mixedAddressesByAddress;
	private final Deque<WithdrawalTransaction> withdrawalQueue;

	AddressPool(String houseAddress, Map<String, MixedAddresses> mixedAddressesByAddress, Deque<WithdrawalTransaction> withdrawalQueue) {
		this.houseAddress = houseAddress;
		this.mixedAddressesByAddress = mixedAddressesByAddress;
		this.withdrawalQueue = withdrawalQueue;
	}
	public AddressPool(String houseAddress) {
		this(houseAddress, Maps.newConcurrentMap(), new ConcurrentLinkedDeque<>());
	}

	public void setMixedAddresses(MixedAddresses mixedAddresses) {
		mixedAddressesByAddress.put(mixedAddresses.getDepositAddress(), mixedAddresses);
		mixedAddresses.getSourceAddresses().forEach(address -> mixedAddressesByAddress.put(address, mixedAddresses));
	}
	
	public MixedAddresses getMixedAddresses(String address) {
		return mixedAddressesByAddress.get(address);
	}

	public List<String> getDepositAddresses() {
		return Sets.newLinkedHashSet(mixedAddressesByAddress.values()).stream()
				.map(MixedAddresses::getDepositAddress)
				.collect(Collectors.toList());
	}

	public String getHouseAddress() {
		return houseAddress;
	}

	public void enqueueWithdrawal(String depositAddress, double amountToWithdraw) {
		WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(amountToWithdraw, getMixedAddresses(depositAddress));
		withdrawalQueue.push(withdrawalTransaction);
	}
	
	public int getWithdrawalQueueSize() {
		return withdrawalQueue.size();
	}

	public boolean isWithdrawalQueueEmpty() {
		return getWithdrawalQueueSize() == 0;
	}

	public WithdrawalTransaction dequeueWithdrawal() {
		return withdrawalQueue.pop();
	}

	public void dequeueWithdrawal(String depositAddress, Consumer<WithdrawalTransaction> withdrawalConsumer) {
		WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(0, getMixedAddresses(depositAddress));
		withdrawalQueue.iterator().forEachRemaining(w -> {
			if(w.equals(withdrawalTransaction)) {
				withdrawalQueue.remove(w);
				withdrawalConsumer.accept(w);
			}
		});
	}
}
