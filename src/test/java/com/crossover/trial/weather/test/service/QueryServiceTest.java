package com.crossover.trial.weather.test.service;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.crossover.trial.weather.dto.AirportDto;
import com.crossover.trial.weather.dto.AtmosphericInformation;
import com.crossover.trial.weather.service.WeatherQueryService;
import com.crossover.trial.weather.service.impl.WeatherQueryServiceImpl;

public class QueryServiceTest {
	
	private WeatherQueryService service = new WeatherQueryServiceImpl();
	
	@Test
	public void findAirportDataTest(){
		AirportDto result = service.findAirportData("");
	}
	
	@Test
	public void getAirportDataTest(){
		List<AirportDto> result = service.getAirportData();
	}
	
	@Test
	public void getAirportDataIdxTest(){
		int result = service.getAirportDataIdx("");
	}
	
	@Test
	public void getAtmosphericInformationTest(){
		List<AtmosphericInformation> result = service.getAtmosphericInformation();
	}
	
	@Test
	public void getHelthStatusTest(){
		Map<String, Object> result = service.getHelthStatus();
	}
	
	@Test
	public void getWeatherTest(){
		List<AtmosphericInformation> result = service.getWeather("", "");
	}	
}
