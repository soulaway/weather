package com.crossover.trial.weather.test.service;

import org.junit.Before;

import com.crossover.trial.weather.WeatherCollectorEndpoint;
import com.crossover.trial.weather.WeatherQueryEndpoint;
import com.crossover.trial.weather.dto.WeatherPoint;
import com.crossover.trial.weather.rest.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.rest.RestWeatherQueryEndpoint;
import com.google.gson.Gson;

public class CollectorServiceTest {

    private WeatherQueryEndpoint _query = new RestWeatherQueryEndpoint();

    private WeatherCollectorEndpoint _update = new RestWeatherCollectorEndpoint();

    private Gson _gson = new Gson();

    private WeatherPoint _dp;
    
    @Before
    public void setUp() throws Exception {
        //RestWeatherQueryEndpoint.init();

        _dp = new WeatherPoint("wind").withFirst(22).withSecond(10).withMean(20).withThird(30).withCount(10);
        _update.updateWeather("BOS", "wind", _gson.toJson(_dp));
        _query.weather("BOS", "0").getEntity();
    }

/*    @Test
    public void testGet() throws Exception {
        List<WeatherPoint> ais = (List<WeatherPoint>) _query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), _dp);
    }

    @Test
    public void testGetNearby() throws Exception {
        // check datasize response
        _update.updateWeather("JFK", "wind", _gson.toJson(_dp));
        _dp.setMean(40);
        _update.updateWeather("EWR", "wind", _gson.toJson(_dp));
        _dp.setMean(30);
        _update.updateWeather("LGA", "wind", _gson.toJson(_dp));

        List<WeatherPoint> ais = (List<WeatherPoint>) _query.weather("JFK", "200").getEntity();
        assertEquals(3, ais.size());
    }

    @Test
    public void testUpdate() throws Exception {

        WeatherPoint windDp = new WeatherPoint(22, 10, 20, 30, 10);
        _update.updateWeather("BOS", "wind", _gson.toJson(windDp));
        _query.weather("BOS", "0").getEntity();

        String ping = _query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());

        WeatherPoint cloudCoverDp = new WeatherPoint(50, 10, 60, 100, 4);
        _update.updateWeather("BOS", "cloudcover", _gson.toJson(cloudCoverDp));

        List<WeatherPoint> ais = (List<WeatherPoint>) _query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), windDp);
        assertEquals(ais.get(0).getCloudCover(), cloudCoverDp);
    }*/

}