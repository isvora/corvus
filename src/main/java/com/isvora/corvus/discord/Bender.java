package com.isvora.corvus.discord;

import com.isvora.corvus.model.RankResource;
import com.isvora.corvus.service.CommentsService;
import com.isvora.corvus.service.StockTwitsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class Bender extends ListenerAdapter {

    private final StockTwitsService stockTwitsService;
    private final CommentsService commentsService;
    private static final String UPCOMING_TICKERS = "Upcoming Tickers";
    private static final String TICKERS_EVENT = "tickers";
    private static final String COMMENTS_EVENT = "comments";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        switch (event.getName()) {
            case TICKERS_EVENT:
                handleUpcomingTickersEvent(event);
                break;
            case COMMENTS_EVENT:
                handleCommentsEvent(event);
                break;
        }
    }

    private void handleUpcomingTickersEvent(SlashCommandInteractionEvent event) {
        log.info("Finding upcoming tickers");
        var upcomingTickers = stockTwitsService.getStockTwitsResponse();
        event.reply(UPCOMING_TICKERS + "10-20").setEphemeral(true)
                .flatMap(v ->
                        event.getHook().editOriginal(upcomingTickers(upcomingTickers.getResponse().getRanks().subList(10, 20))))
                .queue();
    }

    private void handleCommentsEvent(SlashCommandInteractionEvent event) {
        var ticker = event.getOption("ticker");
        if (ticker == null) {
            event.reply("Please provide ticker").setEphemeral(true).queue();
            return;
        }

        var comments = stockTwitsService.getStockTwitsComments(ticker.getAsString());
        var message = commentsService.handleComments(comments, ticker.getAsString());
        event.reply(message).setEphemeral(true).queue();
    }

    private String upcomingTickers(List<RankResource> upcomingTickers) {
        StringBuilder stringBuilder = new StringBuilder();
        upcomingTickers.forEach(symbol -> stringBuilder.append(symbol.discordFormat()));
        return stringBuilder.toString();
    }
}
