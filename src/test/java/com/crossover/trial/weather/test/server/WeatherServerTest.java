package com.crossover.trial.weather.test.server;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crossover.trial.weather.WeatherApplication;
import com.crossover.trial.weather.WeatherServer;

public class WeatherServerTest {
	
	private static URI uri = UriBuilder.fromUri("http://localhost/").port(8282).build();
	
	private static HttpServer server;
	
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
    public void testCollectPingOk() {
        Response response = client.target("http://localhost:8282/collect/ping").request().get();
        Assert.assertEquals(200, response.getStatus());
    }
}
