package com.crossover.trial.weather.service;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;
import com.crossover.trial.weather.dto.DataPoint;
import com.crossover.trial.weather.exception.BusinessException;

public interface WeatherCollectorService {
	AirportData addAirport(String iataCode, double latitude, double longitude);
	void addDataPoint(String iataCode, String pointType, DataPoint dp) throws BusinessException;
	public void updateAtmosphericInformation(AtmosphericInformation ai, String pointType, DataPoint dp) throws BusinessException;
}
