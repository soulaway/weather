package com.crossover.trial.weather.exception;

import java.util.ArrayList;
import java.util.List;

public class InvalidEnumValueException extends BusinessException {

	private static final long serialVersionUID = InvalidEnumValueException.class.getName().hashCode();;

	public InvalidEnumValueException(String enumName, String value) {
		super(ErrorCodes.INVALID_ENUM_VALUE_EXCEPTION, enumName + " code " + value + " is undefined.");
		List<String> params = new ArrayList<String>(2);
		params.add(enumName);
		params.add(value);
		this.setParams(params);

	}
}
