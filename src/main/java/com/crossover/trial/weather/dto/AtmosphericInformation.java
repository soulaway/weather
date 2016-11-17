package com.crossover.trial.weather.dto;

import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * encapsulates sensor information for a particular location
 */
public class AtmosphericInformation {

	/** temperature in degrees celsius */
	private DataPoint temperature;

	/** wind speed in km/h */
	private DataPoint wind;

	/** humidity in percent */
	private DataPoint humidity;

	/** precipitation in cm */
	private DataPoint precipitation;

	/** pressure in mmHg */
	private DataPoint pressure;

	/** cloud cover percent from 0 - 100 (integer) */
	private DataPoint cloudCover;

	/** the last time this data was updated, in milliseconds since UTC epoch */
	private long lastUpdateTime;

	public AtmosphericInformation() {
		this.lastUpdateTime = System.currentTimeMillis();
	}

	public DataPoint getTemperature() {
		return temperature;
	}

	public void setTemperature(DataPoint temperature) {
		this.temperature = temperature;
	}

	public DataPoint getWind() {
		return wind;
	}

	public void setWind(DataPoint wind) {
		this.wind = wind;
	}

	public DataPoint getHumidity() {
		return humidity;
	}

	public void setHumidity(DataPoint humidity) {
		this.humidity = humidity;
	}

	public DataPoint getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(DataPoint precipitation) {
		this.precipitation = precipitation;
	}

	public DataPoint getPressure() {
		return pressure;
	}

	public void setPressure(DataPoint pressure) {
		this.pressure = pressure;
	}

	public DataPoint getCloudCover() {
		return cloudCover;
	}

	public void setCloudCover(DataPoint cloudCover) {
		this.cloudCover = cloudCover;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
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
		AtmosphericInformation ai = (AtmosphericInformation) o;

		return true && Objects.equals(temperature, ai.getTemperature()) && Objects.equals(wind, ai.getWind())
				&& Objects.equals(humidity, ai.getHumidity()) && Objects.equals(precipitation, ai.getPrecipitation())
				&& Objects.equals(pressure, ai.getPressure()) && Objects.equals(cloudCover, ai.getCloudCover())
				&& lastUpdateTime == ai.getLastUpdateTime();
	}

	@Override
	public int hashCode() {
		return Objects.hash(temperature, wind, humidity, precipitation, pressure, cloudCover)
				+ Double.hashCode(lastUpdateTime);
	}
}
