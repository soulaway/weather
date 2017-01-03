package com.github.soulaway.weather;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.HttpServerFilter;
import org.glassfish.grizzly.http.server.HttpServerProbe;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.servlet.FilterRegistration;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.servlet.ServletContainer;

import com.github.soulaway.weather.exception.ResourceUnavaliableException;
import com.github.soulaway.weather.properties.WeatherProperties;
import com.google.inject.servlet.GuiceFilter;

public class WeatherServer {
	
	private static String url =  WeatherProperties.instance.getProperty("com.crossover.trial.weather.base-url");
	private static int port =  Integer.parseInt(WeatherProperties.instance.getProperty("com.crossover.trial.weather.port"));
	
	public static final URI BASE_URI = UriBuilder.fromUri(url).port(port).build();

	public static void main(String[] args) {
		try {
			System.out.println("Starting Weather App local testing server: " + BASE_URI.toString());

			HttpServer server = startGreezly(BASE_URI);

			// blocks until the process is terminated
			Thread.currentThread().join();
			server.shutdown();
		} catch (InterruptedException ex) {
			Logger.getLogger(WeatherServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Creates a @ServletContainer with provided @Application class
	 * And applies the @GuiceFilter for being able to DI and IOC with application resources within
	 * predefined @GuiceConfigContextListener context listener
	 * 
	 * @param rsApplicationClass Application class
	 * @return @ServletContainer
	 * @throws ServletException
	 */
	
	public static WebappContext createGuiceWebappContext(Class<? extends Application> rsApplicationClass)
			throws ServletException {

		final WebappContext context = new WebappContext("GuiceWebappContext", "");
		context.addListener(GuiceConfigContextListener.class);

		ServletRegistration registration = context.addServlet("ServletContainer", ServletContainer.class);
		registration.addMapping("/*");
		registration.setInitParameter("javax.ws.rs.Application", rsApplicationClass.getName());

		final FilterRegistration filter = context.addFilter("GuiceFilter", GuiceFilter.class);
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), "/*");
		context.createListener(GuiceConfigContextListener.class);

		return context;
	}
	
	public static void loadInitialData(){
    	AirportLoader loader = new AirportLoader(BASE_URI);
		URL url = WeatherServer.class.getClassLoader().getResource("airports.dat");
		try {
			loader.upload(url.getPath());
		} catch (IOException e) {
			throw new ResourceUnavaliableException(url.getPath());
		}			
	}
	
	public static HttpServer startGreezly(URI baseUri) {
		final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, false);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			server.shutdownNow();
		}));

		HttpServerProbe probe = new HttpServerProbe.Adapter() {
			@SuppressWarnings("rawtypes")
			public void onRequestReceiveEvent(HttpServerFilter filter, Connection connection, Request request) {
				System.out.println(request.getRequestURI());
			}
		};

		server.getServerConfiguration().getMonitoringConfig().getWebServerConfig().addProbes(probe);
		
		try {
			createGuiceWebappContext(WeatherApplication.class).deploy(server);
			server.start();
		} catch (IOException | ServletException e) {
			Logger.getLogger(WeatherServer.class.getName()).log(Level.SEVERE, null, e);
		}

		boolean isLoadData = WeatherProperties.instance.getBoolean("com.crossover.trial.weather.load-initial-data");
		if (isLoadData){
			loadInitialData();
		}
		return server;
	}
}
