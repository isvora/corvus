package com.isvora.corvus.mapper;

import com.isvora.corvus.database.Pricing;
import com.isvora.corvus.database.Stock;
import com.isvora.corvus.database.Symbol;
import com.isvora.corvus.model.StocktwitsResource;

import java.util.ArrayList;
import java.util.List;

public final class SymbolMapper {

    private SymbolMapper() {

    }

    public static List<Symbol> map(StocktwitsResource stocktwitsResource) {
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
}
