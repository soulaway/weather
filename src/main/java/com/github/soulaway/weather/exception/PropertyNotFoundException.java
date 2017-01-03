package com.github.soulaway.weather.exception;

import java.util.ArrayList;
import java.util.List;

public class PropertyNotFoundException extends BusinessException {

	private static final long serialVersionUID = PropertyNotFoundException.class.getName().hashCode();;

	public PropertyNotFoundException(String name) {
		super(ErrorCodes.PROPERTY_NOT_FOUND_EXCEPTION, name + " is not a found");
		List<String> params = new ArrayList<String>(1);
		params.add(name);
		this.setParams(params);
	}
}
