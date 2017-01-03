package com.github.soulaway.weather.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.soulaway.weather.dto.Airport;
import com.github.soulaway.weather.dto.WeatherPoint;
import com.github.soulaway.weather.enums.WeatherPointType;
import com.github.soulaway.weather.exception.InvalidEnumValueException;
import com.github.soulaway.weather.exception.MissingMandatoryAttrException;
import com.github.soulaway.weather.exception.UnknownIataCodeException;
import com.github.soulaway.weather.service.WeatherService;
import com.github.soulaway.weather.service.WeatherServiceImpl;

public class WeatherServiceTest {

	private WeatherService service = new WeatherServiceImpl();

	private static String D_IATA = "BOS";
	private static String D_TYPE_WIND = WeatherPointType.WIND.getCode();
	private static double D_LAT = 42.364347;
	private static double D_LON = -71.005181;
	private static double D_MEAN = 20;
	private static int D_FIRST = 22;
	private static int D_SECOND = 10;
	private static int D_THIRD = 30;
	private static int D_COUNT = 20;

	private Airport airport = new Airport().withIata(D_IATA).withLatitude(D_LAT).withLongitude(D_LON);
	private WeatherPoint weatherPoint = new WeatherPoint(D_TYPE_WIND).withFirst(D_FIRST).withSecond(D_SECOND)
			.withMean(D_MEAN).withThird(D_THIRD).withCount(D_COUNT);
	
	@Before
	public void loadTestData() {
		service.addAirport(D_IATA, D_LAT, D_LON);
		service.updateWeatherPoint(D_IATA, D_TYPE_WIND, weatherPoint);
	}

	@Test
	public void getAirports() {
		assertEquals(service.getAirports().size(), 1);
	}

	@Test
	public void addAirport() {
		service.addAirport("DOC", 222.2, 333.3);
		assertEquals(service.getAirports().size(), 2);
	}

	@Test(expected = MissingMandatoryAttrException.class)
	public void addAirportEmptyIata() {
		service.addAirport("", 222.2, 333.3);
		assertEquals(service.getAirports().size(), 2);
	}

	@Test
	public void findAirport() {
		assertEquals(service.findAirport(D_IATA), airport);
	}

	@Test(expected = UnknownIataCodeException.class)
	public void findAirportWrongIata() {
		assertEquals(service.findAirport("WRONG"), airport);
	}

	@Test
	public void getWeather() {
		List<WeatherPoint> weather = service.getWeather(D_IATA, 0.0);
		assertEquals(weather.size(), 1);
		assertWeatherPointValues(weather.get(0), weatherPoint);
		assertNotEquals(weather.get(0).getLastUpdateTime(), 0);
	}

	@Test
	public void getWeatherWithRadius() {
		service.addAirport("EWR", 40.6925, -74.168667);
		WeatherPoint windPoint = new WeatherPoint(WeatherPointType.WIND.getCode()).withCount(D_COUNT).withFirst(D_FIRST).withMean(D_MEAN).withSecond(D_SECOND).withThird(D_THIRD);
		WeatherPoint tempPoint = new WeatherPoint(WeatherPointType.TEMPERATURE.getCode()).withCount(D_COUNT).withFirst(D_FIRST).withMean(D_MEAN).withSecond(D_SECOND).withThird(D_THIRD);
		WeatherPoint cloudPoint = new WeatherPoint(WeatherPointType.CLOUDCOVER.getCode()).withCount(D_COUNT).withFirst(D_FIRST).withMean(D_MEAN).withSecond(D_SECOND).withThird(D_THIRD);

		service.addAirport("EWR", 40.6925, -74.168667);
		service.updateWeatherPoint("EWR", WeatherPointType.WIND.getCode(), windPoint);
		
		service.addAirport("JFK", 40.639751, -73.778925);
		service.updateWeatherPoint("JFK", WeatherPointType.TEMPERATURE.getCode(), tempPoint);
		
		service.addAirport("LGA", 40.777245, -73.872608);
		service.updateWeatherPoint("LGA", WeatherPointType.CLOUDCOVER.getCode(), cloudPoint);

		List<WeatherPoint> weather = service.getWeather("JFK", 200.0);
		assertTrue(weather.contains(windPoint));
		assertTrue(weather.contains(tempPoint));
		assertTrue(weather.contains(cloudPoint));
		assertEquals(weather.size(), 3);
	}

	@Test
	public void updateWeather() {
		String code = WeatherPointType.CLOUDCOVER.getCode();

		WeatherPoint w = new WeatherPoint(code).withFirst(D_FIRST).withSecond(D_SECOND).withMean(D_MEAN)
				.withThird(D_THIRD).withCount(D_COUNT);

		WeatherPoint responded = service.updateWeatherPoint(D_IATA, code, w);
		assertEquals(responded, w);
		List<WeatherPoint> airportWeather = service.getWeather(D_IATA, 0.0);
		WeatherPoint target = WeatherPointType.getFirstByCode(code, airportWeather);
		assertEquals(target, w);
	}
	
	private void assertWeatherPointValues(WeatherPoint actual, WeatherPoint expected){
		assertTrue(actual.getTypeCode() == expected.getTypeCode() );
		assertEquals(actual.getFirst(), expected.getFirst());
		assertEquals(actual.getSecond(), expected.getSecond());
		assertEquals(actual.getThird(), expected.getThird());
		assertEquals(actual.getMean(), expected.getMean(), 0.1);		
	}
	
	@Test(expected = InvalidEnumValueException.class)
	public void updateWeatherWrongTypeCode() {
		String code = "WRONG";
		WeatherPoint w = new WeatherPoint(code);
		service.updateWeatherPoint(D_IATA, code, w);
	}

	@Test
	public void getHelthStatus() {
		Map<String, Object> stat = service.getHelthStatus();
		assertTrue(stat.containsKey("iata_freq"));
		assertTrue(stat.containsKey("radius_freq"));
		assertTrue(stat.containsKey("datasize"));
	}

	@Test
	public void deleteAirport() {
		Airport a = service.deleteAirport(D_IATA);
		assertEquals(service.getAirports().size(), 0);
		assertEquals(airport, a);
	}
}
