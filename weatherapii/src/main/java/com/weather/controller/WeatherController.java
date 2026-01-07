package com.weather.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.weather.controller.model.WeatherResponse;

@RestController
@RequestMapping("/weather")
public class WeatherController {

	@Value("${weather.api.key}")
	private String apiKey;

	@Value("${weather.api.url}")
	private String apiUrl;

	@GetMapping("/{city}")
	public WeatherResponse getWeather(@PathVariable String city) {

		try {
			String fullUrl = apiUrl + "?key=" + apiKey + "&q=" + city;
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(fullUrl, WeatherResponse.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
