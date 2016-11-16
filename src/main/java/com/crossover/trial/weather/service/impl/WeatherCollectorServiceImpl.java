package com.crossover.trial.weather.service.impl;

import java.io.Serializable;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;
import com.crossover.trial.weather.dto.DataPoint;
import com.crossover.trial.weather.dto.DataPointType;
import com.crossover.trial.weather.exception.BaseException;
import com.crossover.trial.weather.service.WeatherCollectorService;
import com.crossover.trial.weather.service.WeatherQueryService;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

@RequestScoped
public class WeatherCollectorServiceImpl implements WeatherCollectorService, Serializable{

	private static final long serialVersionUID = WeatherCollectorServiceImpl.class.getName().hashCode();

    @Inject
    public WeatherQueryService queryService;
    /**
     * Update the airports weather data with the collected data.
     *
     * @param iataCode the 3 letter IATA code
     * @param pointType the point type {@link DataPointType}
     * @param dp a datapoint object holding pointType data
     *
     * @throws BaseException if the update can not be completed
     */
    @Override
    public void addDataPoint(String iataCode, String pointType, DataPoint dp) throws BaseException {
        int airportDataIdx = queryService.getAirportDataIdx(iataCode);
        AtmosphericInformation ai = queryService.getAtmosphericInformation().get(airportDataIdx);
        updateAtmosphericInformation(ai, pointType, dp);
    }

    /**
     * update atmospheric information with the given data point for the given point type
     *
     * @param ai the atmospheric information object to update
     * @param pointType the data point type as a string
     * @param dp the actual data point
     */
    @Override
    public void updateAtmosphericInformation(AtmosphericInformation ai, String pointType, DataPoint dp) throws BaseException {
        final DataPointType dptype = DataPointType.valueOf(pointType.toUpperCase());

        if (pointType.equalsIgnoreCase(DataPointType.WIND.name())) {
            if (dp.getMean() >= 0) {
                ai.setWind(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.TEMPERATURE.name())) {
            if (dp.getMean() >= -50 && dp.getMean() < 100) {
                ai.setTemperature(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.HUMIDTY.name())) {
            if (dp.getMean() >= 0 && dp.getMean() < 100) {
                ai.setHumidity(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.PRESSURE.name())) {
            if (dp.getMean() >= 650 && dp.getMean() < 800) {
                ai.setPressure(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.CLOUDCOVER.name())) {
            if (dp.getMean() >= 0 && dp.getMean() < 100) {
                ai.setCloudCover(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.PRECIPITATION.name())) {
            if (dp.getMean() >=0 && dp.getMean() < 100) {
                ai.setPrecipitation(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        throw new IllegalStateException("couldn't update atmospheric data");
    }
    
    /**
     * Add a new known airport to our list.
     *
     * @param iataCode 3 letter code
     * @param latitude in degrees
     * @param longitude in degrees
     *
     * @return the added airport
     */
	@Override
    public AirportData addAirport(String iataCode, double latitude, double longitude) {
        AirportData ad = new AirportData();
        queryService.getAirportData().add(ad);

        AtmosphericInformation ai = new AtmosphericInformation();
        queryService.getAtmosphericInformation().add(ai);
        ad.setIata(iataCode);
        ad.setLatitude(latitude);
        ad.setLatitude(longitude);
        return ad;
    }

}
