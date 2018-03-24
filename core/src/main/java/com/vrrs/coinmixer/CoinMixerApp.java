package com.vrrs.coinmixer;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vrrs.coinmixer.addresses.services.AddressPool;
import com.vrrs.coinmixer.addresses.services.AddressesServiceFacade;
import com.vrrs.coinmixer.addresses.services.HouseTransactionService;
import com.vrrs.coinmixer.addresses.services.WithdrawalStrategy;
import com.vrrs.coinmixer.addresses.web.AddressesController;
import com.vrrs.coinmixer.coins.clients.CoinClient;
import com.vrrs.coinmixer.coins.clients.JobCoinClient;

import io.dropwizard.Application;
import io.dropwizard.bundles.version.VersionBundle;
import io.dropwizard.bundles.version.VersionSupplier;
import io.dropwizard.bundles.version.suppliers.MavenVersionSupplier;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public final class CoinMixerApp extends Application<CoinMixerAppConfig> {
	
	private static final String COIN_MIXER_ARTIFACT_ID = "coinmixer-core";
	private static final String COIN_MIXER_GROUP_ID = "com.vrrs";
	
	@Override
	public void initialize(Bootstrap<CoinMixerAppConfig> bootstrap) {
		VersionSupplier versionSupplier = new MavenVersionSupplier(COIN_MIXER_GROUP_ID, COIN_MIXER_ARTIFACT_ID);
		bootstrap.addBundle(new VersionBundle(versionSupplier));
		bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
				bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
	}

	@Override
	public void run(CoinMixerAppConfig config, Environment env) throws Exception {
		ObjectMapper jsonMapper = env.getObjectMapper();
		jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
		jsonMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		jsonMapper.setDateFormat(new SimpleDateFormat("YYYY-MM-DD"));
		
		AddressPool addressPool = new AddressPool(config.getHouseAddress());
		CoinClient coinClient = new JobCoinClient(config.getJobCoinBaseURL());
		WithdrawalStrategy withdrawalStrategy = new WithdrawalStrategy.RandomFractionalKnapsackStrategy();
		HouseTransactionService houseTransactionService = new HouseTransactionService(addressPool, coinClient, withdrawalStrategy);
		AddressesServiceFacade addressesServiceFacade = new AddressesServiceFacade(addressPool, houseTransactionService, coinClient);
		env.jersey().register(new AddressesController(addressesServiceFacade));
	}
	
	public static void main(String[] args) throws Exception {
		new CoinMixerApp().run(args);
	}

}
