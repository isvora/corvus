package com.isvora.corvus.runner;

import com.isvora.corvus.database.Pricing;
import com.isvora.corvus.database.Stock;
import com.isvora.corvus.database.Symbol;
import com.isvora.corvus.model.StocktwitsResource;
import com.isvora.corvus.service.DiscordService;
import com.isvora.corvus.service.StockService;
import com.isvora.corvus.service.StockTwitsService;
import com.isvora.corvus.service.SymbolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class Corvus implements CommandLineRunner {

    private final StockTwitsService stockTwitsService;
    private final StockService stockService;
    private final SymbolService symbolService;
    private final DiscordService discordService;

    @Override
    public void run(String... args) {
        discordService.buildBot();
    }

    /**
     * Every 30 seconds scan for upcoming trending tickers to get information about them.
     * Delete the symbols from the database that they are replacing.
     * Send a discord message
     */
    @Scheduled(cron = "*/30 * * * * *")
    public void scanUpcomingTickers() {
        log.info("Scanning upcoming tickers");
        var upcomingTickers = new LinkedHashMap<Symbol, String>();
        var stocktwitsResponse = stockTwitsService.getStockTwitsResponse();
        var symbols = buildSymbolsList(stocktwitsResponse);
        var upcomingSymbols = symbols.subList(10, 20);
        upcomingSymbols.forEach(symbol -> {
            var stock = stockService.findStockByTicker(symbol.getStock().getTicker());
            if (stock.isPresent()) {
                var optionalSymbol = symbolService.findSymbolByStock(stock.get());
                optionalSymbol.ifPresent(value -> compareSymbols(value, symbol, upcomingTickers));
            } else {
                upcomingTickers.put(symbol, ":new: ");
                symbolService.save(symbol);
            }
        });

        if(!upcomingTickers.isEmpty()) {
            sendDiscordMessage(upcomingTickers);
        }
    }

    private void compareSymbols(Symbol oldSymbol, Symbol newSymbol, Map<Symbol, String> upcomingTickers) {
        if (newSymbol.getRank() < oldSymbol.getRank()) {
            upcomingTickers.put(newSymbol, ":arrow_up: ");
        } else if (newSymbol.getRank() > oldSymbol.getRank()) {
            upcomingTickers.put(newSymbol, ":arrow_down: ");
        } else {
            return;
        }
        symbolService.deleteSymbol(oldSymbol);
        symbolService.save(newSymbol);
    }

    private List<Symbol> buildSymbolsList(StocktwitsResource stocktwitsResource) {
        List<Symbol> symbols = new ArrayList<>();

        stocktwitsResource.getResponse().getRanks().forEach(rank -> {
                symbols.add(Symbol.builder()
                        .rank(rank.getRank())
                        .score(rank.getValue())
                        .pricing(Pricing.builder()
                                .price(rank.getPricing().getPrice())
                                .priceChange(rank.getPricing().getPriceChange())
                                .build())
                        .stock(Stock.builder()
                                .id(rank.getStock().getStockId())
                                .name(rank.getStock().getName())
                                .ticker(rank.getStock().getSymbol())
                                .build())
                        .build());
        });

        return symbols;
    }

    private void sendDiscordMessage(Map<Symbol, String> upcomingTickers) {
        var stringBuilder = new StringBuilder();
        upcomingTickers.forEach((symbol, s) -> {
            stringBuilder.append(symbol.discordFormat(s));
        });
        discordService.postUpcomingTickers(stringBuilder.toString());
    }

}
