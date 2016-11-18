package com.crossover.trial.weather.exception;

import java.util.ArrayList;
import java.util.List;

public class InvalidParamTypeException extends BusinessException {

    private static final long serialVersionUID = InvalidParamTypeException.class.getName().hashCode();;

    public InvalidParamTypeException(String paramName, String expected) {
        super(ErrorCodes.INVALID_PARAM_TYPE_EXCEPTION, "requested " + paramName + " in not type of " + expected);
    	List<String> params = new ArrayList<String>(2);
    	params.add(paramName);
    	params.add(expected);
    	this.setParams(params);

    }
}
