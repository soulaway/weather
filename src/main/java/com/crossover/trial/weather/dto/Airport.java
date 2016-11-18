package com.crossover.trial.weather.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.crossover.trial.weather.enums.WeatherPointType;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class Airport implements Serializable {

	private static final long serialVersionUID = Airport.class.getName().hashCode();

	private final List<WeatherPoint> weather;

	public List<WeatherPoint> getWeather() {
		return weather;
	}

	/** the three letter IATA code */
	private String iata;

	/** latitude value in degrees */
	private double latitude;

	/** longitude value in degrees */
	private double longitude;

	/**
	 * Each Airport already have several WeatherPoints one per
	 * each @WeatherPointType
	 */
	public Airport() {
		weather = new ArrayList<WeatherPoint>(WeatherPointType.values().length);
		for (WeatherPointType wp : WeatherPointType.values()) {
			weather.add(new WeatherPoint(wp.getCode()));
		}
	}

	public String getIata() {
		return iata;
	}

	public Airport withIata(String iata) {
		this.iata = iata;
		return this;
	}

	public double getLatitude() {
		return latitude;
	}

	public Airport withLatitude(double latitude) {
		this.latitude = latitude;
		return this;
	}

	public double getLongitude() {
		return longitude;
	}

	public Airport withLongitude(double longitude) {
		this.longitude = longitude;
		return this;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Airport a = (Airport) o;

		return true && Objects.equals(iata, a.getIata()) && latitude == a.getLatitude()
				&& longitude == a.getLongitude();
	}

	@Override
	public int hashCode() {
		return iata.hashCode() + Double.hashCode(longitude) + Double.hashCode(latitude);
	}
}
