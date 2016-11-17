package com.crossover.trial.weather.exception;

import java.util.ArrayList;
import java.util.List;

public class UnexpectedException extends BusinessException {

    private static final long serialVersionUID = UnexpectedException.class.getName().hashCode();;

    public UnexpectedException(String msg) {
        super(ErrorCodes.UNEXPECTED_EXCEPTION, "Unexpected error: "+msg);
    	List<String> params = new ArrayList<String>(1);
    	params.add(msg);
    	this.setParams(params);
    }

}
