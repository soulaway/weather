package com.crossover.trial.weather.test.service;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.crossover.trial.weather.dto.Airport;
import com.crossover.trial.weather.dto.WeatherPoint;
import com.crossover.trial.weather.enums.WeatherPointType;
import com.crossover.trial.weather.service.QueryService;
import com.crossover.trial.weather.service.QueryServiceImpl;

public class QueryServiceTest {
	
	private QueryService service = new QueryServiceImpl();
	
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
		((QueryServiceImpl)service).putAirport(airport);
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
		assertEquals(service.findAirport("BOS"), airport);
	}
	@Test
	public void getWeather(){
		List<WeatherPoint> weather = service.getWeather("BOS", "0");
		assertEquals(weather, airport.getWeather());
		assertEquals(weather.size(), WeatherPointType.values().length);
	}
	@Test
	public void getHelthStatus(){
		Map<String, Object> stat = service.getHelthStatus();
		System.out.println(stat.toString());
	}
	@Test
	public void deleteAirport(){
		Airport a = service.deleteAirport("BOS");
		assertEquals(service.getAirports().size(), 0);
		assertEquals(airport, a);
	}
}
