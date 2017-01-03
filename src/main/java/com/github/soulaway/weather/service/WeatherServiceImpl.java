package com.github.soulaway.weather.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.github.soulaway.weather.dto.Airport;
import com.github.soulaway.weather.dto.WeatherPoint;
import com.github.soulaway.weather.enums.WeatherPointType;
import com.github.soulaway.weather.exception.InvalidEnumValueException;
import com.github.soulaway.weather.exception.MissingMandatoryAttrException;
import com.github.soulaway.weather.exception.UnknownIataCodeException;
import com.google.inject.Singleton;

/**
 * WeatherServiceImpl, represents a state-full service object.
 * 
 * Implements the application business logic @WeatherService 
 * Holds data resources: @airports; @requestFrequency; @radiusFreq
 * 
 * @author soul
 *
 */

@Singleton
public class WeatherServiceImpl implements WeatherService, Serializable {

	private static final long serialVersionUID = WeatherServiceImpl.class.getName().hashCode();
	
	public final static Logger LOGGER = Logger.getLogger(WeatherServiceImpl.class.getName());

	/** earth radius in KM */
	public static final double R = 6372.8;

	public static final long DAY_LENGTH_MILIS = TimeUnit.DAYS.toMillis(1);

	/**
	 * Parameters below are approximate, assuming 4xcore testing environment.
	 * Relevant values should be calculated under the load. 16 An initial
	 * capacity of map 0.9 Load factor ensures a dense packaging inside
	 * ConcurrentHashMap which will optimize memory use. 10 The concurrencyLevel
	 * will ensure that 10 threads will be able to maintain the Map
	 * concurrently.
	 */

	// key: iataCode; value: Airport
	private Map<String, Airport> airports = new ConcurrentHashMap<String, Airport>(16, 0.9f, 10);

	// key: iataCode; value: updateCounter
	public static Map<String, AtomicInteger> requestFrequency = new HashMap<String, AtomicInteger>();

	// key: radius; value: updateCounter
	public static Map<Double, AtomicInteger> radiusFreq = new HashMap<Double, AtomicInteger>();

	@Override
	public List<Airport> getAirports() {
		List<Airport> result = new LinkedList<Airport>();
		result.addAll(airports.values());
		return result;
	}

	@Override
	public Airport findAirport(String iataCode) {
		checkIata(iataCode);
		return airports.get(iataCode);
	}

	@Override
	public List<WeatherPoint> getWeather(String iata, double radius) {
		checkIata(iata);
		updateRequestFrequency(iata, radius);
		if (radius == 0) {
			List<WeatherPoint> weather = airports.get(iata).getWeather();
			List<WeatherPoint> result = new ArrayList<>(weather.size());
			weather.stream().filter((w) -> w.getLastUpdateTime()  > 0).forEach(w -> result.add(w));
			return result;
		} else {
			return findWeatherByRadius(iata, radius);
		}
	}

	private List<WeatherPoint> findWeatherByRadius(String iata, double radius) {
		List<WeatherPoint> result = new ArrayList<>();
		Airport ad = airports.get(iata);
		List<Airport> airports = getAirports();
		airports.stream().forEach(a -> {
			if (calculateDistance(ad, a) <= radius) {
				a.getWeather().stream().filter((w) -> w.getLastUpdateTime()  > 0).forEach(w -> result.add(w));
			}
		});
		return result;
	}

	private void checkIata(String iataCode) {
		if (iataCode == null || iataCode.isEmpty()) {
			throw new MissingMandatoryAttrException(iataCode);
		}
		if (!airports.containsKey(iataCode)) {
			throw new UnknownIataCodeException(iataCode);
		}
	}

	private void updateRequestFrequency(String iata, Double radius) {
		AtomicInteger prevReqF = requestFrequency.putIfAbsent(iata, new AtomicInteger(1));
		AtomicInteger prevRadF = radiusFreq.putIfAbsent(radius, new AtomicInteger(1));
		
		if (prevReqF != null) {
			prevReqF.incrementAndGet();
		}
		if (prevRadF != null) {
			prevRadF.incrementAndGet();
		}
	}

	@Override
	public Map<String, Object> getHelthStatus() {
		Map<String, Object> retval = new HashMap<>();
		List<Airport> airports = getAirports();
		// counts all WeatherPoints updated today
		long count = 0;
		for (Airport a : airports) {
			count += a.getWeather().stream()
					.filter(wp -> wp.getLastUpdateTime() > (System.currentTimeMillis() - DAY_LENGTH_MILIS)).count();
		}
		retval.put("datasize", count);
		retval.put("iata_freq", countResponceFrequency());
		retval.put("radius_freq", calculateRadiusHistory());
		return retval;
	}

	private Map<String, Double> countResponceFrequency() {
		Map<String, Double> freq = new HashMap<>();
		if (requestFrequency.size() > 0) {
			for (String iata : airports.keySet()) {
				if (requestFrequency.containsKey(iata)) {
					int counter = requestFrequency.get(iata).get();
					double frac = (double) (counter) / requestFrequency.size();
					freq.put(iata, frac);
				}
			}
		}
		return freq;
	}

	private List<Integer> calculateRadiusHistory() {
		List<Integer> hist = new ArrayList<Integer>(radiusFreq.size());
		if (radiusFreq.size() > 0) {
			for (Map.Entry<Double, AtomicInteger> e : radiusFreq.entrySet()) {
				hist.add(e.getValue().get());
			}
		}
		return hist;
	}

	/**
	 * Haversine distance between two airports.
	 *
	 * @param ad1
	 *            airport 1
	 * @param ad2
	 *            airport 2
	 * @return the distance in KM
	 */
	private double calculateDistance(Airport ad1, Airport ad2) {
		double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
		double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
		double a = Math.pow(Math.sin(deltaLat / 2), 2)
				+ Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(ad1.getLatitude()) * Math.cos(ad2.getLatitude());
		double c = 2 * Math.asin(Math.sqrt(a));
		return R * c;
	}

	@Override
	public Airport deleteAirport(String iataCode) {
		checkIata(iataCode);
		return airports.remove(iataCode);
	}

	@Override
	public WeatherPoint updateWeatherPoint(String iataCode, String pointType, WeatherPoint dp) {
		checkIata(iataCode);
		Airport a = findAirport(iataCode);
		Optional<WeatherPoint> w = a.getWeather().stream().filter(wp -> wp.getTypeCode().equals(pointType)).findFirst();
		if (w.isPresent()) {
			WeatherPointType wpt = WeatherPointType.getWeatherPointType(pointType);
			WeatherPoint wp = w.get();
			wp.withCount(wpt.isMeanFilterApply(dp.getMean()) ? dp.getCount() : wp.getCount())
					.withFirst(wpt.isMeanFilterApply(dp.getMean()) ? dp.getFirst() : wp.getFirst())
					.withSecond(wpt.isMeanFilterApply(dp.getMean()) ? dp.getSecond() : wp.getSecond())
					.withThird(wpt.isMeanFilterApply(dp.getMean()) ? dp.getThird() : wp.getThird())
					.withMean(dp.getMean()).setLastUpdateTime(System.currentTimeMillis());
			return w.get();
		} else {
			throw new InvalidEnumValueException("pointType", pointType);
		}
	}

	@Override
	public Airport addAirport(String iataCode, double latitude, double longitude) {
		if (iataCode == null || iataCode.isEmpty()) {
			throw new MissingMandatoryAttrException(iataCode);
		}
		Airport result = new Airport().withIata(iataCode).withLatitude(latitude).withLongitude(longitude);
		airports.put(iataCode, result);
		return result;
	}
}
