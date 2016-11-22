package com.crossover.trial.weather.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.crossover.trial.weather.dto.WeatherPoint;
import com.crossover.trial.weather.exception.InvalidEnumValueException;

/**
 * The various types of data points we can collect.
 *
 * @author code test administrator
 */
public enum WeatherPointType {
	WIND("WIND") {
		@Override
		public boolean isMeanFilterApply(double mean) {
			return mean >= 0;
		}
	},
	TEMPERATURE("TEMPERATURE") {
		@Override
		public boolean isMeanFilterApply(double mean) {
			return (mean >= -50 && mean < 100);
		}
	},
	HUMIDTY("HUMIDTY") {
		@Override
		public boolean isMeanFilterApply(double mean) {
			return (mean >= 0 && mean < 100);
		}
	},
	PRESSURE("PRESSURE") {
		@Override
		public boolean isMeanFilterApply(double mean) {
			return (mean >= 650 && mean < 800);
		}
	},
	CLOUDCOVER("CLOUDCOVER") {
		@Override
		public boolean isMeanFilterApply(double mean) {
			return (mean >= 0 && mean < 100);
		}
	},
	PRECIPITATION("PRECIPITATION") {
		@Override
		public boolean isMeanFilterApply(double mean) {
			return (mean >= 0 && mean < 100);
		}
	};

	private final String code;

	/* package */ WeatherPointType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static WeatherPointType getWeatherPointType(String code) {
		if (code == null) {
			return null;
		}
		for (WeatherPointType wt : WeatherPointType.values()) {
			if (wt.getCode().equalsIgnoreCase(code)) {
				return wt;
			}
		}
		throw new InvalidEnumValueException(WeatherPointType.class.getSimpleName(), code);
	}

	public static WeatherPoint getFirstByCode(String code, List<WeatherPoint> points) {
		Optional<WeatherPoint> result = points.stream().filter(p -> p.getTypeCode().equals(code)).findFirst();
		if (result.isPresent()) {
			return result.get();
		} else {
			throw new InvalidEnumValueException(WeatherPointType.class.getSimpleName(), code);
		}
	}

	public static List<WeatherPoint> getAllByCode(String code, List<WeatherPoint> points) {
		List<WeatherPoint> result = new ArrayList<WeatherPoint>();
		points.stream().forEach(p -> {
			if (p.getTypeCode().equals(code)) {
				result.add(p);
			}
		});
		return result;
	}

	/**
	 * Indicates if particular value of the WeatherPoint needs to be updated
	 * 
	 * @param mean
	 *            filtered value
	 * @return true - value should be updated
	 */
	public abstract boolean isMeanFilterApply(double mean);
}
