package com.crossover.trial.weather.properties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import com.crossover.trial.weather.exception.PropertyNotFoundException;
import com.crossover.trial.weather.exception.ResourceUnavaliableException;

public enum WeatherProperties{
	
	instance {
		
		private Properties props = new Properties();
		private URL url = WeatherProperties.class.getClassLoader().getResource("weather.properties");
		
		@Override
		public String getProperty(String key) {
			if (props.isEmpty()){
				reload(url);
			}
			if (props.containsKey(key)){
				return props.getProperty(key);
			} else {
				throw new PropertyNotFoundException(key);
			}
		}
	
		@Override
		public boolean getBoolean(String key) {
			return Boolean.valueOf(getProperty(key));
		}

		@Override
		public void reload(URL url){
			LOG.info("reload: property source: " + url.getPath());
			try {
				InputStream is = url.openStream();
				props.load(is);
				is.close();	
			} catch (IOException e) {
				throw new ResourceUnavaliableException(url.getPath());
			}
		}

	};
	
	public final static Logger LOG = Logger.getLogger(WeatherProperties.class.getName());	
	
	public abstract String getProperty(String key);
	
	public abstract boolean getBoolean(String key);

	/**
	 * Provides ability to reload application properties at runtime from the given URI
	 * @param url
	 */
	
	public abstract void reload(URL url);
}
