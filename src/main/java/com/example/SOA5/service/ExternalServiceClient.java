package com.example.SOA5.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class ExternalServiceClient {

    private final RestTemplate restTemplate;

    @Value("${product.service-url}")
    private String productServiceUrl;

    @Value("${order.service-url}")
    private String orderServiceUrl;

    public ExternalServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JsonNode getProduct(Integer productId, String authorization) {
        return get(productServiceUrl + "/products/" + productId, authorization);
    }

    public JsonNode getOrder(Integer orderId, String authorization) {
        return get(orderServiceUrl + "/orders/" + orderId, authorization);
    }

    private JsonNode get(String url, String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization);
        return restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(headers), JsonNode.class).getBody();
    }
}
