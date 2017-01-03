package com.github.soulaway.weather.test.server;

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

import com.github.soulaway.weather.AirportLoader;
import com.github.soulaway.weather.WeatherServer;
import com.github.soulaway.weather.dto.Airport;
import com.github.soulaway.weather.dto.WeatherPoint;
import com.github.soulaway.weather.properties.WeatherProperties;

public class IntegrationTest {
	private static String url =  WeatherProperties.instance.getProperty("com.crossover.trial.weather.base-url");
	
	private static int port =  Integer.parseInt(WeatherProperties.instance.getProperty("com.crossover.trial.weather.port"));
	
	private static URI TEST_URL = UriBuilder.fromUri(url).port(port).build();

	private static HttpServer server;

	private AirportLoader loader = new AirportLoader(TEST_URL);

	private static Client client = ClientBuilder.newClient();

	@BeforeClass
	public static void init() throws IOException, InterruptedException, ServletException {
		server = WeatherServer.startGreezly(TEST_URL);
	}

	@AfterClass
	public static void stop() {
		server.shutdown();
	}

	@Test
	public void testQueryPingOk() {
		Response response = client.target("http://localhost:8282/query/ping").request().get();
		Assert.assertEquals(200, response.getStatus());
		response.close();
	}

	@Test
	public void testQueryGetWeather() {
		Response response = client.target("http://localhost:8282/query/weather/BOS/0").request().get();
		Assert.assertEquals(200, response.getStatus());
		response.close();
	}

	@Test
	public void testQueryGetWeatherWrongRadius() {
		Response response = client.target("http://localhost:8282/query/weather/BOS/radius").request().get();
		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		response.close();
	}

	@Test
	public void testCollectPingOk() {
		Response response = client.target("http://localhost:8282/collect/ping").request().get();
		Assert.assertEquals(200, response.getStatus());
		response.close();
	}

	@Test
	public void testAirportPost() {
		Response response = client.target("http://localhost:8282/collect/airport/BOS/20/30").request()
				.post(Entity.json(null));
		Assert.assertEquals(200, response.getStatus());
		response.close();
	}

	@Test
	public void testGetAirports() {
		Response response = client.target("http://localhost:8282/collect/airports").request().get();
		Assert.assertEquals(200, response.getStatus());
		response.close();
	}

	@Test
	public void testGetAirportByCode() {
		Response response = client.target("http://localhost:8282/collect/airport/BOS").request().get();
		Assert.assertEquals(200, response.getStatus());
		response.close();
	}

	@Test
	public void testUpdateWeather() {
		Response response = client.target("http://localhost:8282/collect/weather/BOS/WIND").request()
				.post(Entity.json(new WeatherPoint("WIND").withFirst(10).withSecond(40)));
		Assert.assertEquals(200, response.getStatus());
		response.close();
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
		response.close();
	}
}
