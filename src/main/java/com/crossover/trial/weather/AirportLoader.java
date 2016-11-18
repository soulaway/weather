package com.crossover.trial.weather;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.dto.Airport;

/**
 * A simple airport loader which reads a file from disk and sends entries to the
 * webservice
 *
 * @author code test administrator
 */
public class AirportLoader {

	private static final int IATA_CSV_HEADER_IDX = 4;
	private static final int LAT_CSV_HEADER_IDX = 6;
	private static final int LON_CSV_HEADER_IDX = 7;

	/** end point to supply updates */
	private WebTarget collect;

	public AirportLoader(URI uri) {
		Client client = ClientBuilder.newClient();
		collect = client.target(uri + "collect");
	}

	public static Function<String, Airport> mapToAirport = (line) -> {
		String[] p = line.split(",");
		return new Airport().withIata(p[IATA_CSV_HEADER_IDX].substring(1, p[IATA_CSV_HEADER_IDX].length() - 1))
				.withLatitude(Double.parseDouble(p[LAT_CSV_HEADER_IDX]))
				.withLongitude(Double.parseDouble(p[LON_CSV_HEADER_IDX]));
	};

	public boolean isServerResponds() {
		WebTarget path = collect.path("/ping");
		Response response = path.request().get();
		boolean result = !response.readEntity(String.class).isEmpty();
		response.close();
		return result;
	}

	@SuppressWarnings("resource")
	public void upload(String filePath) throws IOException {
		Stream<String> stream = Files.lines(Paths.get(filePath));
		List<Airport> airports = stream.map(mapToAirport).collect(Collectors.toList());
		if (isServerResponds()) {
			for (Airport airport : airports) {
				WebTarget wt = collect.path(
						"/airport/" + airport.getIata() + "/" + airport.getLatitude() + "/" + airport.getLongitude());
				wt.request().post(Entity.json(null));
			}
		}
	}

	public static void main(String args[]) throws IOException {
		File airportDataFile = new File(args[0]);
		if (!airportDataFile.exists() || airportDataFile.length() == 0) {
			System.err.println(airportDataFile + " is not a valid input");
			System.exit(1);
		}

		AirportLoader al = new AirportLoader(WeatherServer.BASE_URI);
		al.upload(args[0]);
		System.exit(0);
	}
}
