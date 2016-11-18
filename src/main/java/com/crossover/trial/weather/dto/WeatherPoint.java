package com.crossover.trial.weather.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A collected point, including some information about the range of collected
 * values
 *
 * @author code test administrator
 */
public class WeatherPoint implements Serializable {

	private static final long serialVersionUID = WeatherPoint.class.getName().hashCode();

	private String typeCode;

	private double mean;

	private int first;

	private int second;

	private int third;

	private int count;

	private long lastUpdateTime;

	@Deprecated
	public WeatherPoint() {
	}

	public WeatherPoint(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public WeatherPoint withTypeCode(String typeCode) {
		this.typeCode = typeCode;
		return this;
	}

	public double getMean() {
		return mean;
	}

	public WeatherPoint withMean(double mean) {
		this.mean = mean;
		this.lastUpdateTime = System.currentTimeMillis();
		return this;
	}

	public int getFirst() {
		return first;
	}

	public WeatherPoint withFirst(int first) {
		this.first = first;
		return this;
	}

	public int getSecond() {
		return second;
	}

	public WeatherPoint withSecond(int second) {
		this.second = second;
		return this;
	}

	public int getThird() {
		return third;
	}

	public WeatherPoint withThird(int third) {
		this.third = third;
		return this;
	}

	public int getCount() {
		return count;
	}

	public WeatherPoint withCount(int count) {
		this.count = count;
		return this;
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
		WeatherPoint dp = (WeatherPoint) o;

		return true && typeCode.equals(dp.getTypeCode()) && mean == dp.getMean() && first == dp.getFirst()
				&& second == dp.getSecond() && third == dp.getThird() && count == dp.getCount();
	}

	@Override
	public int hashCode() {
		return typeCode.hashCode() + Double.hashCode(mean) + first + second + third + count;
	}
}
