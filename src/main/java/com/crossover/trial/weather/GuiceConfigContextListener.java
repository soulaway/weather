package com.crossover.trial.weather;

import com.crossover.trial.weather.rest.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.rest.RestWeatherQueryEndpoint;
import com.crossover.trial.weather.service.WeatherServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

/**
 * Implementation of the @GuiceServletContextListener.
 * "Logical place to hold the Dependency Injector" (c) Guice Team.
 * 
 * Defines the producers of the application resources.
 * 
 * @author Dmitry Soloviev
 * 
 */

public class GuiceConfigContextListener extends GuiceServletContextListener {

	public static Injector injector;

	@Override
	protected Injector getInjector() {

		injector = Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				bind(WeatherServiceImpl.class);
				bind(RestWeatherCollectorEndpoint.class);
				bind(RestWeatherQueryEndpoint.class);
			}
		});

		return injector;

	}
}
