package com.crossover.trial.weather.rest;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.WeatherCollectorEndpoint;
import com.crossover.trial.weather.dto.Airport;
import com.crossover.trial.weather.dto.WeatherPoint;
import com.crossover.trial.weather.exception.BusinessException;
import com.crossover.trial.weather.exception.InvalidParamTypeException;
import com.crossover.trial.weather.exception.MissingMandatoryAttrException;
import com.crossover.trial.weather.exception.UnknownIataCodeException;
import com.crossover.trial.weather.service.QueryService;
import com.google.gson.Gson;
import com.google.inject.Singleton;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
@Singleton
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {
	
	public final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());
    
    @Inject
    public QueryService queryService;
    
    @Override
    public Response ping() {
    	System.out.println("Response ping");
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    @Override
    public Response updateWeather(@PathParam("iata") String iataCode,
                                  @PathParam("pointType") String pointType,
                                  String datapointJson) {
        try {
        	Gson gson = new Gson();
        	queryService.updateWeatherPoint(iataCode, pointType, gson.fromJson(datapointJson, WeatherPoint.class));
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).build();
    }


    @Override
    public Response getAirports() {
        Set<String> retval = new HashSet<>();
        for (Airport ad : queryService.getAirports()) {
            retval.add(ad.getIata());
        }
        return Response.status(Response.Status.OK).entity(retval).build();
    }


    @Override
    public Response getAirport(@PathParam("iata") String iata) {
    	try{
    		Airport a = queryService.findAirport(iata);
    		return Response.status(Response.Status.OK).entity(a).build();
    	} catch (MissingMandatoryAttrException | UnknownIataCodeException e) {
    		return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
    	}        
    }


    @Override
    public Response addAirport(@PathParam("iata") String iata,
                               @PathParam("lat") String latString,
                               @PathParam("long") String longString) {
		try {
			if (latString == null || latString.isEmpty()){
				return Response.status(Response.Status.BAD_REQUEST).entity(new MissingMandatoryAttrException("lattitude")).build();
			}
			if (longString == null || longString.isEmpty()){
				return Response.status(Response.Status.BAD_REQUEST).entity(new MissingMandatoryAttrException("longitude")).build();
			}
	    	queryService.addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
	        return Response.status(Response.Status.OK).build();
		} catch (NumberFormatException | MissingMandatoryAttrException | UnknownIataCodeException e) {
			if (e.getClass().equals(NumberFormatException.class)){
				return Response.status(Response.Status.BAD_REQUEST).entity(new InvalidParamTypeException("Radius", Double.class.getSimpleName())).build();
			}
			return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
	    }
    }


    @Override
    public Response deleteAirport(@PathParam("iata") String iata) {
    	try{
    		queryService.deleteAirport(iata);
    		return Response.status(Response.Status.OK).build();
    	} catch (MissingMandatoryAttrException | UnknownIataCodeException e) {
    		return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
    	}
    }

    @Override
    public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }
}
