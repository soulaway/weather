package com.crossover.trial.weather;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class GuiceConfigContextListener extends GuiceServletContextListener {
    @Path("/hello")
    public static class Resource {
        
        @Inject 
        Counter counter;
                
        @GET
        public String get() {
            return "Hello, User number " + counter.getNext();
        }
    }
    
    @Singleton
    public static class Counter {
        private final AtomicInteger counter = new AtomicInteger(0);
        public int getNext() {
            return counter.incrementAndGet();
        }
    }
    
	public static Injector injector;
    
	@Override
    protected Injector getInjector() {
        System.out.println("Getting injector");
        injector = Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(Resource.class);
            }
        });
        
        return injector;

}	
/*	@Override
    protected Injector getInjector() {
    	System.out.println("getInjector");
        return Guice.createInjector(new ServletModule(){
            @Override
            protected void configureServlets() {
            	System.out.println("binding GuiceContainer");
                // excplictly bind GuiceContainer before binding Jersey resources
                // otherwise resource won't be available for GuiceContainer
                // when using two-phased injection
                bind(GuiceContainer.class);

                // bind Jersey resources
                PackagesResourceConfig resourceConfig = new PackagesResourceConfig("com.crossover.trial.weather");
                for (Class<?> resource : resourceConfig.getClasses()) {
                    bind(resource);
                }
                // Serve resources with Jerseys GuiceContainer
                serve("/*").with(GuiceContainer.class);
            }               
        });
    }*/
}
