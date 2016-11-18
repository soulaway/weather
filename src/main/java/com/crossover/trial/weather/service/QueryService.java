package com.crossover.trial.weather.service;

import java.util.List;
import java.util.Map;

import com.crossover.trial.weather.dto.Airport;
import com.crossover.trial.weather.dto.WeatherPoint;

public interface QueryService {
	
	List<Airport> getAirports();
	
	Airport findAirport(String iataCode);
	
	List<WeatherPoint> getWeather(String iata, double radius);
	
	Map<String, Object> getHelthStatus();
	
	Airport deleteAirport(String iataCode);
	
	Airport addAirport(String iataCode, double latitude, double longitude);
	
	WeatherPoint updateWeatherPoint(String iataCode, String pointType, WeatherPoint dp);
}
