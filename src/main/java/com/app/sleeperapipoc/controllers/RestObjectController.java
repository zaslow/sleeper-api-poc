package com.app.sleeperapipoc.controllers;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

abstract public class RestObjectController {

	protected static ObjectMapper objectMapper = new ObjectMapper();

	protected static RestTemplate restTemplate = new RestTemplate();
}
