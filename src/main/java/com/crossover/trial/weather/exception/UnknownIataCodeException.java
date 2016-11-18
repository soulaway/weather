package com.crossover.trial.weather.exception;

import java.util.ArrayList;
import java.util.List;

public class UnknownIataCodeException extends BusinessException {

    private static final long serialVersionUID = UnknownIataCodeException.class.getName().hashCode();;

    public UnknownIataCodeException(String iataCode) {
        super(ErrorCodes.UNKNOWN_IATA_CODE_EXCEPTION, "Given IATA code " + iataCode + " is unknown");
    	List<String> params = new ArrayList<String>(1);
    	params.add(iataCode);
    	this.setParams(params);
    }

}
