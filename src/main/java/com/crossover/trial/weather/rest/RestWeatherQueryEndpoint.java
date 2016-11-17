package com.crossover.trial.weather.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.WeatherQueryEndpoint;
import com.crossover.trial.weather.dto.WeatherPoint;
import com.crossover.trial.weather.service.QueryService;
import com.google.gson.Gson;
import com.google.inject.servlet.RequestScoped;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@Path("/query")
@RequestScoped
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

    public final static Logger LOGGER = Logger.getLogger(RestWeatherQueryEndpoint.class.getName());
    
    @Inject
    public QueryService queryService;
    
    /**
     * Retrieve service health including total size of valid data points and request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    public String ping() {
    	Gson gson = new Gson();
    	String result = gson.toJson(queryService.getHelthStatus());
    	System.out.println("ping:: "+ result);
    	return Response.status(Response.Status.OK).build().toString();
    }

    /**
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the requested airport information and
     * return a list of matching atmosphere information.
     *
     * @param iata the iataCode
     * @param radiusString the radius in km
     *
     * @return a list of atmospheric information
     */
    @Override
    public Response weather(String iata, String radiusString) {
        List<WeatherPoint> retval = queryService.getWeather(iata, radiusString);
        return Response.status(Response.Status.OK).entity(retval).build();
    }
}
