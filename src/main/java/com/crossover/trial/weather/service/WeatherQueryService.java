package com.crossover.trial.weather.service;

import java.util.List;
import java.util.Map;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;

public interface WeatherQueryService {
	
	List<AirportData> getAirportData();
	
	List<AtmosphericInformation> getAtmosphericInformation();
	
	AirportData findAirportData(String iataCode);
	
	int getAirportDataIdx(String iataCode);
	
	List<AtmosphericInformation> getWeather(String iata, String radiusString);
	
	Map<String, Object> getHelthStatus();
}
