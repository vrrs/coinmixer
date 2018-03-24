package com.vrrs.coinmixer.coins.clients;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.http.Request;
import com.jcabi.http.request.ApacheRequest;
import com.jcabi.http.response.RestResponse;

import io.vavr.control.Try;

public final class JobCoinClient implements CoinClient {
	
	private static final Logger LOG = LoggerFactory.getLogger(JobCoinClient.class);
	
	private static final int ADDRESS_SIZE = 20;
	private static final int MAX_NUM_OF_RETRIES = 10;
	private final String baseURL;
	private final ObjectMapper jsonMapper;

	public JobCoinClient(String baseURL) {
		this.baseURL = baseURL;
		this.jsonMapper = new ObjectMapper();
	}

	@Override
	public void deposit(String fromAddress, String toAddress, double amount) {
		Try.of(() -> getDepositResponse(fromAddress, toAddress, amount))
		.mapTry(resp -> jsonMapper.readValue(resp, JobCoinResponse.class))
		.mapTry(resp -> resp.getStatus().equals("OK"))
		.getOrElseThrow(CoinClientAccessException::new);
	}
	
	private String getDepositResponse(String fromAddress, String toAddress, double amount) throws IOException {
		final String payload = jsonMapper.writeValueAsString(new JobCoinRequest().setAmount(String.valueOf(amount))
				.setFromAddress(fromAddress).setToAddress(toAddress));
		return new ApacheRequest(baseURL).uri().path("/api/transactions").back()
				.method(Request.POST)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.body().set(payload).back()
				.fetch()
				.as(RestResponse.class)
				.assertStatus(HttpURLConnection.HTTP_OK)
				.body();
	}

	@Override
	public double getBalance(String address) {
		return Try.of(() -> getBalanceResponse(address))
		.mapTry(resp -> jsonMapper.readValue(resp, JobCoinResponse.class))
		.mapTry(JobCoinResponse::getBalance)
		.mapTry(Double::parseDouble)
		.getOrElseThrow(CoinClientAccessException::new);
	}

	private String getBalanceResponse(String address) throws IOException {
		return new ApacheRequest(baseURL)
				.uri().path("/api/addresses/" + address).back()
				.method(Request.GET)
				.fetch()
				.as(RestResponse.class)
				.assertStatus(HttpURLConnection.HTTP_OK)
				.body();
	}

	@Override
	public String newAddress() {
		for (int retry = 0; retry < MAX_NUM_OF_RETRIES; retry++) {
			String newAddress = RandomStringUtils.randomAlphabetic(ADDRESS_SIZE);
			boolean isSuccess = Try.run(() -> getNewAddressResponse(newAddress))
					.onFailure(e -> LOG.info("Failed to create new address", e)).isSuccess();
			if (isSuccess)
				return newAddress;
		}
		throw new CoinClientAccessException("Failed to create new address");
	}
	
	private void getNewAddressResponse(String address) throws IOException {
		new ApacheRequest(baseURL)
				.uri().path("/create").back()
				.method(Request.POST)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
				.body().formParam("address", address).back()
				.fetch()
				.as(RestResponse.class)
				.assertStatus(HttpURLConnection.HTTP_SEE_OTHER);
	}
	
	@JsonInclude(Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class JobCoinResponse {
		
		private String balance;
		private String status;
		
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getBalance() {
			return balance;
		}
		public void setBalance(String balance) {
			this.balance = balance;
		}	
	}
	@JsonInclude(Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class JobCoinRequest {
		
		private String amount;
		private String fromAddress;
		private String toAddress;
		public String getAmount() {
			return amount;
		}
		public JobCoinRequest setAmount(String amount) {
			this.amount = amount;
			return this;
		}
		public String getFromAddress() {
			return fromAddress;
		}
		public JobCoinRequest setFromAddress(String fromAddress) {
			this.fromAddress = fromAddress;
			return this;
		}
		public String getToAddress() {
			return toAddress;
		}
		public JobCoinRequest setToAddress(String toAddress) {
			this.toAddress = toAddress;
			return this;
		}
	}

}
