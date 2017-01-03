package com.github.soulaway.weather;

import javax.inject.Inject;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

/**
 * 
 * Realization of the javax.ws.rs.Application that is configured to use Guice
 * Servlet within HK2 (implementation of JSR-330), provided by Glassfish.
 * 
 * @author Dmitry Soloviev
 *
 */

public class WeatherApplication extends ResourceConfig {

	/**
	 * Provides "auto configuration" for the REST Resources assuming that
	 * WeatherApplication lays in the root package
	 * 
	 * @param serviceLocator
	 *            Provider of auto discovering by HK2 API
	 */

	@Inject
	public WeatherApplication(ServiceLocator serviceLocator) {
		packages(WeatherApplication.class.getPackage().getName());
		GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
		GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
		guiceBridge.bridgeGuiceInjector(GuiceConfigContextListener.injector);
	}
}
