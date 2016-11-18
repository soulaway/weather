package com.crossover.trial.weather.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.WeatherQueryEndpoint;
import com.crossover.trial.weather.dto.WeatherPoint;
import com.crossover.trial.weather.exception.InvalidParamTypeException;
import com.crossover.trial.weather.exception.MissingMandatoryAttrException;
import com.crossover.trial.weather.exception.UnknownIataCodeException;
import com.crossover.trial.weather.service.WeatherServiceImpl;
import com.google.gson.Gson;
import com.google.inject.servlet.RequestScoped;

/**
 * The Weather App REST endpoint allows clients to query, update and check
 * health stats. Currently, all data is held in memory. The end point deploys to
 * a single container
 *
 * @author code test administrator
 */
@Path("/query")
@RequestScoped
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

	public final static Logger LOGGER = Logger.getLogger(RestWeatherQueryEndpoint.class.getName());

	@Inject
	public WeatherServiceImpl queryService;

	/**
	 * Retrieve service health including total size of valid data points and
	 * request frequency information.
	 *
	 * @return health stats for the service as a string
	 */

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String ping() {
		Gson gson = new Gson();
		String result = gson.toJson(queryService.getHelthStatus());
		return Response.status(Response.Status.OK).entity(result).build().toString();
	}

	/**
	 * Given a query in json format {'iata': CODE, 'radius': km} extracts the
	 * requested airport information and return a list of matching atmosphere
	 * information.
	 *
	 * @param iata
	 *            the iataCode
	 * @param radiusString
	 *            the radius in km
	 *
	 * @return a list of atmospheric information
	 */
	@GET
	@Path("/weather/{iata}/{radius}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response weather(@PathParam("iata") String iata, @PathParam("radius") String radiusString) {
		try {
			double radius = radiusString == null || radiusString.trim().isEmpty() ? 0 : Double.valueOf(radiusString);
			List<WeatherPoint> retval = queryService.getWeather(iata, radius);
			return Response.status(Response.Status.OK).entity(retval).build();
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new InvalidParamTypeException("Radius", Double.class.getSimpleName()).getMessage()).build();
		} catch (MissingMandatoryAttrException | UnknownIataCodeException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
}
