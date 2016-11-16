package com.crossover.trial.weather.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;
import com.crossover.trial.weather.service.WeatherQueryService;
import com.google.inject.Singleton;

@Singleton
public class WeatherQueryServiceImpl implements WeatherQueryService , Serializable{

	private static final long serialVersionUID = WeatherQueryServiceImpl.class.getName().hashCode();
	
    /** earth radius in KM */
    public static final double R = 6372.8;
	
    /** all known airports */
    protected static List<AirportData> airportData = new ArrayList<>();

    /** atmospheric information for each airport, idx corresponds with airportData */
    protected static List<AtmosphericInformation> atmosphericInformation = new LinkedList<>();

    /**
     * Internal performance counter to better understand most requested information, this map can be improved but
     * for now provides the basis for future performance optimizations. Due to the stateless deployment architecture
     * we don't want to write this to disk, but will pull it off using a REST request and aggregate with other
     * performance metrics {@link #ping()}
     */
    public static Map<AirportData, Integer> requestFrequency = new HashMap<AirportData, Integer>();

    public static Map<Double, Integer> radiusFreq = new HashMap<Double, Integer>();

    
	@Override
	public List<AirportData> getAirportData() {
		return airportData;
	}
	
	@Override
	public List<AtmosphericInformation> getAtmosphericInformation() {
		return atmosphericInformation;
	}
	
	@Override
	public List<AtmosphericInformation> getWeather(String iata, String radiusString) {
		List<AtmosphericInformation> retval = new ArrayList<>();
        double radius = radiusString == null || radiusString.trim().isEmpty() ? 0 : Double.valueOf(radiusString);
        updateRequestFrequency(iata, radius);
        		
        if (radius == 0) {
            int idx = getAirportDataIdx(iata);
            retval.add(atmosphericInformation.get(idx));
        } else {
            AirportData ad = findAirportData(iata);
            for (int i=0;i< airportData.size(); i++){
                if (calculateDistance(ad, airportData.get(i)) <= radius){
                    AtmosphericInformation ai = atmosphericInformation.get(i);
                    if (ai.getCloudCover() != null || ai.getHumidity() != null || ai.getPrecipitation() != null
                       || ai.getPressure() != null || ai.getTemperature() != null || ai.getWind() != null){
                        retval.add(ai);
                    }
                }
            }
        }
		return atmosphericInformation;
	}

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
		
	@Override
    public AirportData findAirportData(String iataCode) {
        return airportData.stream()
            .filter(ap -> ap.getIata().equals(iataCode))
            .findFirst().orElse(null);
    }


    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
	
	@Override
	public int getAirportDataIdx(String iataCode) {
        AirportData ad = findAirportData(iataCode);
        return airportData.indexOf(ad);
	}

	@Override
	public Map<String, Object> getHelthStatus() {
        Map<String, Object> retval = new HashMap<>();

        int datasize = 0;
        for (AtmosphericInformation ai : atmosphericInformation) {
            // we only count recent readings
            if (ai.getCloudCover() != null
                || ai.getHumidity() != null
                || ai.getPressure() != null
                || ai.getPrecipitation() != null
                || ai.getTemperature() != null
                || ai.getWind() != null) {
                // updated in the last day
                if (ai.getLastUpdateTime() > System.currentTimeMillis() - 86400000) {
                    datasize++;
                }
            }
        }
        retval.put("datasize", datasize);

        Map<String, Double> freq = new HashMap<>();
        // fraction of queries
        for (AirportData data : airportData) {
            double frac = (double)requestFrequency.getOrDefault(data, 0) / requestFrequency.size();
            freq.put(data.getIata(), frac);
        }
        retval.put("iata_freq", freq);

        int m = radiusFreq.keySet().stream()
                .max(Double::compare)
                .orElse(1000.0).intValue() + 1;

        int[] hist = new int[m];
        for (Map.Entry<Double, Integer> e : radiusFreq.entrySet()) {
            int i = e.getKey().intValue() % 10;
            hist[i] += e.getValue();
        }
        retval.put("radius_freq", hist);

        return retval;
	}



    /**
     * Records information about how often requests are made
     *
     * @param iata an iata code
     * @param radius query radius
     */
    private void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = findAirportData(iata);
        requestFrequency.put(airportData, requestFrequency.getOrDefault(airportData, 0) + 1);
        radiusFreq.put(radius, radiusFreq.getOrDefault(radius, 0));
    }

    /**
     * Haversine distance between two airports.
     *
     * @param ad1 airport 1
     * @param ad2 airport 2
     * @return the distance in KM
     */
    private double calculateDistance(AirportData ad1, AirportData ad2) {
        double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
        double a =  Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2)
                * Math.cos(ad1.getLatitude()) * Math.cos(ad2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    /**
     * A dummy init method that loads hard coded data
     */
/*    protected void init() {
        airportData.clear();
        atmosphericInformation.clear();
        requestFrequency.clear();

        collectorService.addAirport("BOS", 42.364347, -71.005181);
        collectorService.addAirport("EWR", 40.6925, -74.168667);
        collectorService.addAirport("JFK", 40.639751, -73.778925);
        collectorService.addAirport("LGA", 40.777245, -73.872608);
        collectorService.addAirport("MMU", 40.79935, -74.4148747);
    }*/
	
}
