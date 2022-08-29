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
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class DiscordService extends ListenerAdapter {

    private final DiscordConfiguration discordConfiguration;
    private final StockTwitsService stockTwitsService;
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
        builder.addEventListeners(new Bender(stockTwitsService));
        try {
            jda = builder.build();
            jda.upsertCommand("tickers", "Show the upcoming trending tickers on stocktwits").queue();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            log.info("Exception while starting up Bender Discord Bot " + e);
        }
    }
}

