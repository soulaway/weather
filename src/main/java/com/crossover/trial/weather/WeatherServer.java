package com.crossover.trial.weather;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URI;
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

import com.google.inject.servlet.GuiceFilter;

/**
 * This main method will be use by the automated functional grader. You
 * shouldn't move this class or remove the main method. You may change the
 * implementation, but we encourage caution.
 *
 * @author code test administrator
 */
public class WeatherServer {

	public static final URI BASE_URI = UriBuilder.fromUri("http://127.0.0.1/").port(9090).build();

	public static void main(String[] args) {
		try {
			System.out.println("Starting Weather App local testing server: " + BASE_URI.toString());

			HttpServer server = createGreezly(BASE_URI);
			createGuiceWebappContext(WeatherApplication.class).deploy(server);
			server.start();

			// the autograder waits for this output before running automated
			// tests, please don't remove it
			System.out.println(format("Weather Server started.\n url=%s\n", BASE_URI.toString()));

			// blocks until the process is terminated
			Thread.currentThread().join();
			server.shutdown();
		} catch (ServletException | IOException | InterruptedException ex) {
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

	public static HttpServer createGreezly(URI baseUri) {
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

		return server;
	}
}
