package com.crossover.trial.weather.service;

import java.util.List;
import java.util.Map;

import com.crossover.trial.weather.dto.AirportDto;
import com.crossover.trial.weather.dto.AtmosphericInformation;

public interface WeatherQueryService {
	
	List<AirportDto> getAirportData();
	
	List<AtmosphericInformation> getAtmosphericInformation();
	
	AirportDto findAirportData(String iataCode);
	
	int getAirportDataIdx(String iataCode);
	
	List<AtmosphericInformation> getWeather(String iata, String radiusString);
	
	Map<String, Object> getHelthStatus();
}
