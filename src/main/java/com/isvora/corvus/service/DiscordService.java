package com.isvora.corvus.service;

import com.isvora.corvus.configuration.DiscordConfiguration;
import com.isvora.corvus.discord.Bender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class DiscordService extends ListenerAdapter {

    private final DiscordConfiguration discordConfiguration;
    private final StockTwitsService stockTwitsService;
    private final CommentsService commentsService;
    private JDA jda;

    public void postUpcomingTickers(String message) {
        if (jda == null) {
            buildBot();
        }
        List<TextChannel> channels = jda.getTextChannelsByName(discordConfiguration.getDiscordChannel(), true);
        for(TextChannel ch : channels) {
            ch.sendMessage(message).queue();
        }
    }

    public void buildBot() {
        JDABuilder builder = JDABuilder.createDefault(discordConfiguration.getDiscordToken());
        builder.setActivity(Activity.watching("Stocktwits"));
        builder.addEventListeners(new Bender(stockTwitsService, commentsService));
        try {
            jda = builder.build();
            upsertCommands();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            log.info("Exception while starting up Bender Discord Bot " + e);
        }
    }

    private void upsertCommands() {
        jda.upsertCommand("tickers", "Show the upcoming trending tickers on stocktwits").queue();
        jda.upsertCommand("comments", "Show the comments flow for a given ticker")
                .addOption(OptionType.STRING, "ticker", "Ticker to show comment flow for", true)
                .queue();
    }
}

