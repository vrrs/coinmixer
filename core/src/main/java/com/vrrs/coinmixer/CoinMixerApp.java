package com.vrrs.coinmixer;

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
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws Exception {
		new CoinMixerApp().run(args);
	}

}
