package com.vrrs.coinmixer.addresses.services;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@FunctionalInterface
public interface WithdrawalStrategy {

	Map<String, Double> getWithdrawalPerSourceAddress(List<String> sourceAddresses, double withdrawlAmount);

	public static final class RandomFractionalKnapsackStrategy implements WithdrawalStrategy {
		
		private final DecimalFormat twoDecimalFormatter;
		
		public RandomFractionalKnapsackStrategy() {
			this.twoDecimalFormatter = new DecimalFormat("#.##");
			this.twoDecimalFormatter.setRoundingMode(RoundingMode.CEILING);
		}
		@Override
		public Map<String, Double> getWithdrawalPerSourceAddress(List<String> sourceAddresses, double amount) {
			List<Double> randomlyChosenNVector = getRandomlyChosenVector(sourceAddresses.size(), amount);
			Map<String, Double> withdrawalPerSourceAddress = Maps.newLinkedHashMap();
			for(int i = 0; i < sourceAddresses.size(); i++) {
				withdrawalPerSourceAddress.put(sourceAddresses.get(i), randomlyChosenNVector.get(i));
			}
			return withdrawalPerSourceAddress;
		}

		private List<Double> getRandomlyChosenVector(int n, double targetSum) {
			List<Double> vector = Lists.newArrayList();
			double upperBound = targetSum;
			for(int i = 0; i < n - 1; i++) {
				double entry = selectRandomFrom0To(upperBound);
				upperBound = targetSum - entry;
				vector.add(entry);
			}
			vector.add(upperBound);
			return vector;
		}

		private double selectRandomFrom0To(double upperBound) {
			Double d = Math.random() * upperBound;
			return Double.parseDouble(twoDecimalFormatter.format(d));
		}

	}
}
