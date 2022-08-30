package com.isvora.corvus.service;

import com.isvora.corvus.model.comments.CommentsResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private static final String BULLISH_SENTIMENT = "Bullish";
    private static final String BEARISH_SENTIMENT = "Bearish";

    public String handleComments(CommentsResource commentsResource, String ticker) {
        var nonNeutralComments = commentsResource.getMessages().stream().filter(messageResource ->
                messageResource.getEntities() != null && messageResource.getEntities().getSentiment() != null && messageResource.getEntities().getSentiment().getBasic() != null
        ).toList();
        var bullishComments = nonNeutralComments.stream().filter(messageResource ->
                Objects.requireNonNull(messageResource.getEntities().getSentiment().getBasic()).equals(BULLISH_SENTIMENT)).count();
        var bearishComments = nonNeutralComments.stream().filter(messageResource ->
                Objects.requireNonNull(messageResource.getEntities().getSentiment().getBasic()).equals(BEARISH_SENTIMENT)).count();

        var neutralComments = 30 - nonNeutralComments.size();

        return buildDiscordMessage(ticker, bullishComments, bearishComments, neutralComments);
    }

    private String buildDiscordMessage(String ticker, long bullishComments, long bearishComments, long neutralComments) {
        return "Sentiment for $" +
                ticker +
                "\n" +
                bullishComments +
                " Bullish :chart_with_upwards_trend: \n" +
                bearishComments +
                " Bearish :chart_with_downwards_trend: \n" +
                neutralComments +
                " Neutral :bar_chart: \n";
    }


}
