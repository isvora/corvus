package com.isvora.corvus.service;

import com.isvora.corvus.configuration.StocktwitsConfiguration;
import com.isvora.corvus.model.StocktwitsResource;
import com.isvora.corvus.model.comments.CommentsResource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class StockTwitsService {

    private final StocktwitsConfiguration stocktwitsConfiguration;
    private final RestTemplate restTemplate = new RestTemplateBuilder().build();

    public StocktwitsResource getStockTwitsResponse() {
        var response = restTemplate.exchange(
                stocktwitsConfiguration.getApiUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<StocktwitsResource>() {});

        return response.getBody() == null ? null : response.getBody();
    }

    public CommentsResource getStockTwitsComments(String ticker) {
        var response = restTemplate.exchange(
                String.format(stocktwitsConfiguration.getCommentsApiUrl(), ticker),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CommentsResource>() {});

        return response.getBody() == null ? null : response.getBody();
    }
}
