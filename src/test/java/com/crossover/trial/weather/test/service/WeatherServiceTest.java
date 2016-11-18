package com.crossover.trial.weather.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.crossover.trial.weather.dto.Airport;
import com.crossover.trial.weather.dto.WeatherPoint;
import com.crossover.trial.weather.enums.WeatherPointType;
import com.crossover.trial.weather.exception.InvalidEnumValueException;
import com.crossover.trial.weather.exception.MissingMandatoryAttrException;
import com.crossover.trial.weather.exception.UnknownIataCodeException;
import com.crossover.trial.weather.service.WeatherService;
import com.crossover.trial.weather.service.WeatherServiceImpl;

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
		for (WeatherPoint wp : weather) {
			// assuming @loadTestData updates only WIND WP with parameters
			if (wp.getTypeCode() != D_TYPE_WIND) {
				// rest WeatherPoints parameters expected to be 0
				assertEquals(wp.getFirst(), 0);
				assertEquals(wp.getSecond(), 0);
				assertEquals(wp.getThird(), 0);
				assertEquals(wp.getMean(), 0.0, 0);
				assertEquals(wp.getLastUpdateTime(), 0);
			} else {
				assertEquals(wp.getFirst(), D_FIRST);
				assertEquals(wp.getSecond(), D_SECOND);
				assertEquals(wp.getThird(), D_THIRD);
				assertEquals(wp.getMean(), D_MEAN, 0.1);
				assertNotEquals(wp.getLastUpdateTime(), 0);
			}
		}
		assertEquals(weather.size(), WeatherPointType.values().length);
	}

	@Test
	public void getWeatherWithRadius() {
		service.addAirport("EWR", 40.6925, -74.168667);
		service.addAirport("JFK", 40.639751, -73.778925);
		service.addAirport("LGA", 40.777245, -73.872608);
		List<WeatherPoint> weather = service.getWeather("JFK", 200.0);
		assertEquals(weather.size(), WeatherPointType.values().length * 3);
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
