package com.isvora.corvus.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class StocktwitsConfiguration {

    @Value("${stocktwits.api}")
    private String apiUrl;

    @Value("${stocktwits.comments.api}")
    private String commentsApiUrl;
}
