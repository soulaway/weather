package com.crossover.trial.weather.exception;

/**
 * 
 * List of throwable codes during business operations
 * 
 * @author soul
 * 
 */

public interface ErrorCodes {
	public static final int BUSINESS_EXCEPTION = 600;
	public static final int UNKNOWN_IATA_CODE_EXCEPTION = 601;
	public static final int INVALID_ENUM_VALUE_EXCEPTION = 602;
	public static final int INVALID_PARAM_TYPE_EXCEPTION = 603;
	public static final int MISSING_MANDATORY_ATTRIBUTE_EXCEPTION = 604;
}
