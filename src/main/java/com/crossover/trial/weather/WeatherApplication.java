package com.crossover.trial.weather;

import javax.inject.Inject;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

public class WeatherApplication extends ResourceConfig{
    @Inject
    public WeatherApplication(ServiceLocator serviceLocator) {
        // Set package to look for resources in
        packages("com.crossover.trial.weather");
        System.out.println("Registering injectables...");
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(GuiceConfigContextListener.injector);
    }
}
