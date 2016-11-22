package com.crossover.trial.weather.exception;

import java.util.ArrayList;
import java.util.List;

public class ResourceUnavaliableException extends BusinessException {

	private static final long serialVersionUID = ResourceUnavaliableException.class.getName().hashCode();;

	public ResourceUnavaliableException(String path) {
		super(ErrorCodes.RESOURCE_UNAVALIABLE_EXCEPTION, path + " is not a valid input");
		List<String> params = new ArrayList<String>(1);
		params.add(path);
		this.setParams(params);
	}
}
