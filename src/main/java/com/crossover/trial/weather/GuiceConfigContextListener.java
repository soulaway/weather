package com.crossover.trial.weather;

import com.crossover.trial.weather.rest.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.rest.RestWeatherQueryEndpoint;
import com.crossover.trial.weather.service.QueryService;
import com.crossover.trial.weather.service.QueryServiceImpl;
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
				bind(QueryServiceImpl.class);
				bind(RestWeatherCollectorEndpoint.class);
				bind(RestWeatherQueryEndpoint.class);
			}

			@Provides
			QueryService produceQueryService() {
				return new QueryServiceImpl();
			}

			@Provides
			WeatherCollectorEndpoint produceWeatherCollectorEndpoint() {
				return new RestWeatherCollectorEndpoint();
			}
			
			@Provides
			WeatherQueryEndpoint produceWeatherQueryEndpoint() {
				return new RestWeatherQueryEndpoint();
			}

		});

		return injector;

	}
}
