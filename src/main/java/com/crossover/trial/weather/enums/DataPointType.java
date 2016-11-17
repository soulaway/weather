package com.crossover.trial.weather.enums;

import com.crossover.trial.weather.exception.InvalidEnumValueException;

/**
 * The various types of data points we can collect.
 *
 * @author code test administrator
 */
public enum DataPointType {
    WIND("WIND"),
    TEMPERATURE("TEMPERATURE"),
    HUMIDTY("HUMIDTY"),
    PRESSURE("PRESSURE"),
    CLOUDCOVER("CLOUDCOVER"),
    PRECIPITATION("PRECIPITATION");

    private final String code;

    /*package */ DataPointType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DataPointType getMeterReadingsType(String code) {
        if (code == null){
            return null;
        }
        for (DataPointType ut: DataPointType.values())              {
            if (ut.getCode().equalsIgnoreCase(code)) {
                return ut;
            }
        }
        throw new InvalidEnumValueException(DataPointType.class.getSimpleName(), code);
    }
}
