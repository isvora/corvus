package com.isvora.corvus.discord;

import com.isvora.corvus.model.RankResource;
import com.isvora.corvus.service.StockTwitsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class Bender extends ListenerAdapter {

    private final StockTwitsService stockTwitsService;
    private static final String UPCOMING_TICKERS = "Upcoming Tickers";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if (event.getName().equals("tickers")) {
            handleUpcomingTickers(event);
        }
    }

    private void handleUpcomingTickers(SlashCommandInteractionEvent event) {
        log.info("Finding upcoming tickers");
        var upcomingTickers = stockTwitsService.getStockTwitsResponse();
        event.reply(UPCOMING_TICKERS + "10-20").setEphemeral(true)
                .flatMap(v ->
                        event.getHook().editOriginal(upcomingTickers(upcomingTickers.getResponse().getRanks().subList(10, 20))))
                .queue();
    }

    private String upcomingTickers(List<RankResource> upcomingTickers) {
        StringBuilder stringBuilder = new StringBuilder();
        upcomingTickers.forEach(symbol -> stringBuilder.append(symbol.discordFormat()));
        return stringBuilder.toString();
    }
}
