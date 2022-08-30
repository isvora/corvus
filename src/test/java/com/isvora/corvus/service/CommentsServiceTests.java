package com.isvora.corvus.service;

import com.isvora.corvus.model.comments.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class CommentsServiceTests {

    private CommentsService commentsService;
    private static final String TSLA = "TSLA";

    @BeforeEach
    void init() {
        commentsService = new CommentsService();
    }

    @Test
    void testHandleComments() {
        // given
        var messageResource1 = MessageResource.builder()
                .body("Bullish " + TSLA)
                .entities(EntitiesResource.builder()
                        .sentiment(SentimentResource.builder()
                                .basic("Bullish")
                                .build())
                        .build())
                .build();
        var messageResource2 = MessageResource.builder()
                .body("Bearish " + TSLA)
                .entities(EntitiesResource.builder()
                        .sentiment(SentimentResource.builder()
                                .basic("Bearish")
                                .build())
                        .build())
                .build();
        var messageResource3 = MessageResource.builder()
                .body("Neutral " + TSLA)
                .entities(EntitiesResource.builder()
                        .sentiment(SentimentResource.builder()
                                .basic(null)
                                .build())
                        .build())
                .build();
        var commentsResource = CommentsResource.builder()
                .messages(List.of(messageResource1, messageResource2, messageResource3))
                .build();

        // when
        var discordMessage = commentsService.handleComments(commentsResource, TSLA);

        // then
        Assertions.assertThat(discordMessage).isNotNull();
        Assertions.assertThat(discordMessage).isEqualTo("""
                Sentiment for $TSLA
                1 Bullish :chart_with_upwards_trend:\s
                1 Bearish :chart_with_downwards_trend:\s
                28 Neutral :bar_chart:\s
                """);
    }

}
