package com.crossover.trial.weather.rest;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.WeatherCollectorEndpoint;
import com.crossover.trial.weather.dto.AirportDto;
import com.crossover.trial.weather.dto.DataPoint;
import com.crossover.trial.weather.exception.BusinessException;
import com.crossover.trial.weather.service.WeatherCollectorService;
import com.crossover.trial.weather.service.WeatherQueryService;
import com.google.gson.Gson;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {
    public final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());
    
    @Inject
    public WeatherQueryService queryService;
    
    @Inject
    public WeatherCollectorService collectorService;   
    
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
        	collectorService.addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).build();
    }


    @Override
    public Response getAirports() {
        Set<String> retval = new HashSet<>();
        for (AirportDto ad : queryService.getAirportData()) {
            retval.add(ad.getIata());
        }
        return Response.status(Response.Status.OK).entity(retval).build();
    }


    @Override
    public Response getAirport(@PathParam("iata") String iata) {
        AirportDto ad = queryService.findAirportData(iata);
        return Response.status(Response.Status.OK).entity(ad).build();
    }


    @Override
    public Response addAirport(@PathParam("iata") String iata,
                               @PathParam("lat") String latString,
                               @PathParam("long") String longString) {
    	collectorService.addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
        return Response.status(Response.Status.OK).build();
    }


    @Override
    public Response deleteAirport(@PathParam("iata") String iata) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }
}
