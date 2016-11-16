package com.crossover.trial.weather;

import com.crossover.trial.weather.service.WeatherCollectorService;
import com.crossover.trial.weather.service.WeatherQueryService;
import com.crossover.trial.weather.service.impl.WeatherCollectorServiceImpl;
import com.crossover.trial.weather.service.impl.WeatherQueryServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

/**
 * Implementation of the ServletContextListener. Holder of the Guice Injector.
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
				bind(WeatherQueryServiceImpl.class);
				bind(WeatherCollectorServiceImpl.class);
			}

			@Provides
			WeatherQueryService createAndIngectQueryService() {
				return new WeatherQueryServiceImpl();
			}

			@Provides
			WeatherCollectorService createAndIngectCollectorService() {
				return new WeatherCollectorServiceImpl();
			}

		});

		return injector;

	}
}
