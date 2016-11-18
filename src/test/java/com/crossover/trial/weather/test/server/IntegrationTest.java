package com.crossover.trial.weather.test.server;

import java.awt.List;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.servlet.ServletException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crossover.trial.weather.AirportLoader;
import com.crossover.trial.weather.WeatherApplication;
import com.crossover.trial.weather.WeatherServer;
import com.crossover.trial.weather.dto.Airport;
import com.crossover.trial.weather.dto.WeatherPoint;

public class IntegrationTest {

	private static URI uri = UriBuilder.fromUri("http://localhost/").port(8282).build();

	private static HttpServer server;

	private AirportLoader loader = new AirportLoader(uri);

	private static Client client = ClientBuilder.newClient();

	@BeforeClass
	public static void init() throws IOException, InterruptedException, ServletException {
		server = WeatherServer.createGreezly(uri);
		WeatherServer.createGuiceWebappContext(WeatherApplication.class).deploy(server);
		server.start();
	}

	@AfterClass
	public static void stop() {
		server.shutdown();
	}

	@Test
	public void testQueryPingOk() {
		Response response = client.target("http://localhost:8282/query/ping").request().get();
		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void testQueryGetWeather() {
		Response response = client.target("http://localhost:8282/query/weather/BOS/0").request().get();
		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void testQueryGetWeatherWrongRadius() {
		Response response = client.target("http://localhost:8282/query/weather/BOS/radius").request().get();
		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}

	@Test
	public void testCollectPingOk() {
		Response response = client.target("http://localhost:8282/collect/ping").request().get();
		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void testAirportPost() {
		Response response = client.target("http://localhost:8282/collect/airport/BOS/20/30").request()
				.post(Entity.json(null));
		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void testGetAirports() {
		Response response = client.target("http://localhost:8282/collect/airports").request().get();
		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void testGetAirportByCode() {
		Response response = client.target("http://localhost:8282/collect/airport/BOS").request().get();
		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void testUpdateWeather() {
		Response response = client.target("http://localhost:8282/collect/weather/BOS/WIND").request()
				.post(Entity.json(new WeatherPoint("WIND").withFirst(10).withSecond(40)));
		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void loaderTest() throws IOException {
		// test resource csv file
		URL url = IntegrationTest.class.getClassLoader().getResource("airports.dat");
		loader.upload(url.getPath());
		Response response = client.target("http://localhost:8282/collect/airport/STN").request().get();
		Assert.assertEquals(200, response.getStatus());
		Airport stnAirport = response.readEntity(Airport.class);
		Assert.assertTrue(51.885 == stnAirport.getLatitude());
		Assert.assertTrue(0.235 == stnAirport.getLongitude());
	}
}
