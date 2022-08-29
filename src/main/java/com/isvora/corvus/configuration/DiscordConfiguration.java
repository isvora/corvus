package com.isvora.corvus.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class DiscordConfiguration {

    @Value("${discord.token}")
    private String discordToken;

    @Value("${discord.channel}")
    private String discordChannel;
}
