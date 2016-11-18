package com.crossover.trial.weather.exception;

import java.util.ArrayList;
import java.util.List;

public class MissingMandatoryAttrException extends BusinessException {

	private static final long serialVersionUID = MissingMandatoryAttrException.class.getName().hashCode();;

	public MissingMandatoryAttrException(String attrnName) {
		super(ErrorCodes.MISSING_MANDATORY_ATTRIBUTE_EXCEPTION, "Mandatory attribute " + attrnName + " has no value");
		List<String> params = new ArrayList<String>(1);
		params.add(attrnName);
		this.setParams(params);

	}
}
