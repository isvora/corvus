package com.isvora.corvus.service;

import com.isvora.corvus.configuration.StocktwitsConfiguration;
import com.isvora.corvus.model.RankResource;
import com.isvora.corvus.model.ResponseResource;
import com.isvora.corvus.model.StocktwitsResource;
import com.isvora.corvus.model.comments.CommentsResource;
import com.isvora.corvus.model.comments.EntitiesResource;
import com.isvora.corvus.model.comments.MessageResource;
import com.isvora.corvus.model.comments.SentimentResource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StocktwitsServiceTests {

    @Mock
    private StocktwitsConfiguration stocktwitsConfiguration;

    @Mock(name = "restTemplate")
    private RestTemplate restTemplate;

    private StockTwitsService stockTwitsService;

    @BeforeEach
    void init() {
        stockTwitsService = new StockTwitsService(stocktwitsConfiguration, restTemplate);
    }

    @Test
    void testGetStockTwitsResponse() {
        // given
        var stockTwitsRepsonse = StocktwitsResource.builder()
                .response(ResponseResource.builder()
                        .ranks(List.of(RankResource.builder()
                                        .value(10)
                                        .rank(1)
                                .build()))
                        .build())
                .build();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.of(Optional.of(stockTwitsRepsonse)));
        when(stocktwitsConfiguration.getApiUrl()).thenReturn("https://stocktwits.com/api/2");

        // when
        var response = stockTwitsService.getStockTwitsResponse();

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getResponse()).isNotNull();
        Assertions.assertThat(response.getResponse().getRanks()).isNotNull();
        Assertions.assertThat(response.getResponse().getRanks().size()).isEqualTo(1);
        Assertions.assertThat(response.getResponse().getRanks().get(0).getRank()).isEqualTo(1);
        Assertions.assertThat(response.getResponse().getRanks().get(0).getValue()).isEqualTo(10);
    }

    @Test
    void testGetStockTwitsComments() {
        // given
        var commentsResource = CommentsResource.builder()
                .messages(List.of(MessageResource.builder()
                        .entities(EntitiesResource.builder()
                                .sentiment(SentimentResource.builder()
                                        .basic("Bullish")
                                        .build())
                                .build())
                        .build()))
                .build();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.of(Optional.of(commentsResource)));
        when(stocktwitsConfiguration.getCommentsApiUrl()).thenReturn("https://stocktwits.com/api/2/comments/%s");

        // when
        var response = stockTwitsService.getStockTwitsComments("TSLA");

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessages().get(0).getEntities().getSentiment().getBasic()).isEqualTo("Bullish");
    }

}
