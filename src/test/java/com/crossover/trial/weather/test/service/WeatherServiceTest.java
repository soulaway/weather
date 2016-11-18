package com.crossover.trial.weather.test.service;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.crossover.trial.weather.dto.Airport;
import com.crossover.trial.weather.dto.WeatherPoint;
import com.crossover.trial.weather.enums.WeatherPointType;
import com.crossover.trial.weather.exception.InvalidEnumValueException;
import com.crossover.trial.weather.exception.UnknownIataCodeException;
import com.crossover.trial.weather.service.WeatherService;
import com.crossover.trial.weather.service.WeatherServiceImpl;

public class WeatherServiceTest {
	
	private WeatherService service = new WeatherServiceImpl();
	
	private static String D_IATA = "BOS";
	private static double D_LAT = 47.991397;
	private static double D_LON = 37.897943;
	private static double D_MEAN = 20;
	private static int D_FIRST = 22;
	private static int D_SECOND = 10;
	private static int D_THIRD = 30;
	private static int D_COUNT = 20;
	
	private Airport airport = new Airport();
	
	@Before
	public void loadTestData(){
		airport.withIata(D_IATA).withLatitude(D_LAT).withLongitude(D_LON);
		service.addAirport(D_IATA, D_LAT, D_LON);
		WeatherPointType.getFirstByCode(WeatherPointType.WIND.getCode(), airport.getWeather())
		.withFirst(D_FIRST)
		.withSecond(D_SECOND)
		.withMean(D_MEAN)
		.withThird(D_THIRD)
		.withCount(D_COUNT);
	} 
	
	@Test
	public void getAirports(){
		assertEquals(service.getAirports().size(), 1);
	}
	@Test
	public void addAirport(){
		service.addAirport("DOC", 222.2, 333.3);
		assertEquals(service.getAirports().size(), 2);
	}
	@Test
	public void findAirport(){
		assertEquals(service.findAirport(D_IATA), airport);
	}
	
	@Test(expected=UnknownIataCodeException.class)
	public void findAirportWrongIata(){
		assertEquals(service.findAirport("WRONG"), airport);
	}
	
/*	@Test
	public void getWeather(){
		List<WeatherPoint> weather = service.getWeather(D_IATA, 0.0);
		assertEquals(weather, airport.getWeather());
		assertEquals(weather.size(), WeatherPointType.values().length);
	}
	
	@Test
	public void getWeatherWithRadius(){
		List<WeatherPoint> weather = service.getWeather(D_IATA, 500.0);
		assertEquals(weather, airport.getWeather());
		//assertEquals(weather.size(), WeatherPointType.values().length);
	}*/
	
	@Test
	public void updateWeather(){
		String code = WeatherPointType.CLOUDCOVER.getCode();
		
		WeatherPoint w = new WeatherPoint(code)
		.withFirst(D_FIRST)
		.withSecond(D_SECOND)
		.withMean(D_MEAN)
		.withThird(D_THIRD)
		.withCount(D_COUNT);
		
		WeatherPoint responded = service.updateWeatherPoint(D_IATA, code, w);
		assertEquals(responded, w);
		List<WeatherPoint> airportWeather = service.getWeather(D_IATA, 0.0);
		WeatherPoint target = WeatherPointType.getFirstByCode(code, airportWeather);
		assertEquals(target, w);
	}
	
	@Test(expected=InvalidEnumValueException.class)
	public void updateWeatherWrongTypeCode(){
		String code = "WRONG";
		WeatherPoint w = new WeatherPoint(code);
		service.updateWeatherPoint(D_IATA, code, w);
	}
	
	@Test
	public void getHelthStatus(){
		Map<String, Object> stat = service.getHelthStatus();
		System.out.println(stat.toString());
	}
	@Test
	public void deleteAirport(){
		Airport a = service.deleteAirport(D_IATA);
		assertEquals(service.getAirports().size(), 0);
		assertEquals(airport, a);
	}
}
