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
public class GuiceConfigContextListener extends GuiceServletContextListener {

	public static Injector injector;
    
	@Override
    protected Injector getInjector() {
        System.out.println("Getting injector");
        injector = Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
            	bind(WeatherQueryServiceImpl.class);
            	bind(WeatherCollectorServiceImpl.class);
            }
            
            @Provides
            WeatherQueryService createAndIngectQueryService(){
            	return new WeatherQueryServiceImpl();
            }
            
            @Provides
            WeatherCollectorService createAndIngectCollectorService(){
            	return new WeatherCollectorServiceImpl();
            }
            
        });
        
        return injector;

	}	
}
