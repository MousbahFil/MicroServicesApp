package com.in28minutes.microservices.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CurrencyExchangeServiceProxy proxy;

	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;

	@GetMapping("/currency-converter-resttemplate/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertUsingRestTemplate(@PathVariable String from, @PathVariable String to,
														   @PathVariable BigDecimal quantity) throws Exception{

		RestTemplate restTemplate=new RestTemplate();
		String uri="http://localhost:8000/currency-exchange/from/{from}/to/{to}";
		Map<String, String> data=new LinkedHashMap<>();
		data.put("from", from);
		data.put("to",to);
		ResponseEntity<CurrencyConversionBean> entity = restTemplate.getForEntity(uri, CurrencyConversionBean.class, data);
		System.out.println(entity.getStatusCode());
		System.out.println(entity.getHeaders());
		CurrencyConversionBean conversionBean = entity.getBody();
		logger.info(conversionBean.toString());
		return new CurrencyConversionBean(conversionBean.getId(),
				from,
				to,
				conversionBean.getConversionMultiple(),
				quantity,
				conversionBean.getConversionMultiple().multiply(quantity),
				conversionBean.getPort());

	}

	@GetMapping("/currency-converter-myfeign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertUsingFeign(@PathVariable String from, @PathVariable String to,
														   @PathVariable BigDecimal quantity) throws Exception{


		CurrencyConversionBean conversionBean = currencyExchangeProxy.retrieveExchangeValue(from,to);
		logger.info(conversionBean.toString());
		return new CurrencyConversionBean(conversionBean.getId(),
				from,
				to,
				conversionBean.getConversionMultiple(),
				quantity,
				conversionBean.getConversionMultiple().multiply(quantity),
				conversionBean.getPort());

	}

	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		// Feign - Problem 1
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);

		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class,
				uriVariables);

		CurrencyConversionBean response = responseEntity.getBody();

		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity,
				quantity.multiply(response.getConversionMultiple()), response.getPort());
	}

	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		CurrencyConversionBean response = proxy.retrieveExchangeValue(from, to);

		logger.info("{}", response);
		
		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity,
				quantity.multiply(response.getConversionMultiple()), response.getPort());
	}

}
