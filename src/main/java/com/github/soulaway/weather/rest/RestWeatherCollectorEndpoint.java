package com.github.soulaway.weather.rest;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.soulaway.weather.WeatherCollectorEndpoint;
import com.github.soulaway.weather.dto.Airport;
import com.github.soulaway.weather.dto.WeatherPoint;
import com.github.soulaway.weather.exception.BusinessException;
import com.github.soulaway.weather.exception.InvalidParamTypeException;
import com.github.soulaway.weather.exception.MissingMandatoryAttrException;
import com.github.soulaway.weather.exception.UnknownIataCodeException;
import com.github.soulaway.weather.service.WeatherServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.servlet.RequestScoped;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport
 * weather collection sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
@RequestScoped
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {

	public final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());

	@Inject
	public WeatherServiceImpl queryService;

	@GET
	@Path("/ping")
	@Override
	public Response ping() {
		return Response.status(Response.Status.OK).entity("ready").build();
	}

	@POST
	@Path("/weather/{iata}/{pointType}")
	@Override
	public Response updateWeather(@PathParam("iata") String iataCode, @PathParam("pointType") String pointType,
			String datapointJson) {
		try {
			Gson gson = new GsonBuilder().create();
			queryService.updateWeatherPoint(iataCode, pointType, gson.fromJson(datapointJson, WeatherPoint.class));
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/airports")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getAirports() {
		Set<String> retval = new HashSet<>();
		for (Airport ad : queryService.getAirports()) {
			retval.add(ad.getIata());
		}
		return Response.status(Response.Status.OK).entity(retval).build();
	}

	@GET
	@Path("/airport/{iata}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getAirport(@PathParam("iata") String iata) {
		try {
			Airport a = queryService.findAirport(iata);
			return Response.status(Response.Status.OK).entity(a).build();
		} catch (MissingMandatoryAttrException | UnknownIataCodeException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/airport/{iata}/{lat}/{long}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addAirport(@PathParam("iata") String iata, @PathParam("lat") String latString,
			@PathParam("long") String longString) {
		try {
			if (latString == null || latString.isEmpty()) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new MissingMandatoryAttrException("lattitude").getMessage()).build();
			}
			if (longString == null || longString.isEmpty()) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new MissingMandatoryAttrException("longitude").getMessage()).build();
			}
			queryService.addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
			return Response.status(Response.Status.OK).build();
		} catch (NumberFormatException | MissingMandatoryAttrException | UnknownIataCodeException e) {
			if (e.getClass().equals(NumberFormatException.class)) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new InvalidParamTypeException("Radius", Double.class.getSimpleName()).getMessage())
						.build();
			}
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@DELETE
	@Path("/airport/{iata}")
	@Override
	public Response deleteAirport(@PathParam("iata") String iata) {
		try {
			queryService.deleteAirport(iata);
			return Response.status(Response.Status.OK).build();
		} catch (MissingMandatoryAttrException | UnknownIataCodeException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
		}
	}

	@GET
	@Path("/exit")
	@Override
	public Response exit() {
		System.exit(0);
		return Response.noContent().build();
	}
}
