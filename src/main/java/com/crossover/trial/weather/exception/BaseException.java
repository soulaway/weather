package com.crossover.trial.weather.exception;

import java.util.List;

/**
 * An internal exception marker
 */
public abstract class BaseException extends RuntimeException implements ErrorCodes {

	private static final long serialVersionUID = BaseException.class.getName().hashCode();
	
	private int code = ErrorCodes.BUSINESS_EXCEPTION;
	private List<String> params;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}
}
