package com.vrrs.coinmixer.coins.clients;

public interface CoinClient {

	void deposit(String fromAddress, String toAddress, double amount) throws CoinClientAccessException;
	double getBalance(String address) throws CoinClientAccessException;
	String newAddress() throws CoinClientAccessException;
	
	public static final class CoinClientAccessException extends RuntimeException {

		private static final long serialVersionUID = 1L;
		
		public CoinClientAccessException(String msg, Throwable e) {
			super(msg, e);
		}
		public CoinClientAccessException(Throwable e) {
			super(e);
		}
		public CoinClientAccessException(String msg) {
			super(msg);
		}
	}

}
