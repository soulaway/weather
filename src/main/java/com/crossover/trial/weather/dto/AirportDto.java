package com.crossover.trial.weather.dto;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportDto implements Serializable {

	private static final long serialVersionUID = AirportDto.class.getName().hashCode();

	/** the three letter IATA code */
	private String iata;

	/** latitude value in degrees */
	private double latitude;

	/** longitude value in degrees */
	private double longitude;

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
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
		AirportDto a = (AirportDto) o;

		return true && Objects.equals(iata, a.getIata()) && latitude == a.getLatitude()
				&& longitude == a.getLongitude();
	}

	@Override
	public int hashCode() {
		return iata.hashCode() + Double.hashCode(longitude) + Double.hashCode(latitude);
	}
}
