package com.isvora.corvus.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class RankResource {

    private int rank;

    private double value;

    private StockResource stock;

    private PricingResource pricing;

    public String discordFormat() {
        var symbol = this.stock.getSymbol();
        var priceChange = this.pricing.getPriceChange();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.rank % 10 == 0 ? 10 : this.rank % 10);
        stringBuilder.append(". $");
        stringBuilder.append(symbol);
        stringBuilder.append(" <https://stocktwits.com/symbol/");
        stringBuilder.append(symbol);
        stringBuilder.append("> Rank: ");
        stringBuilder.append(this.rank);
        stringBuilder.append(", Price: ");
        stringBuilder.append(this.pricing.getPrice());
        stringBuilder.append("$, Price Change: ");
        stringBuilder.append(priceChange);
        stringBuilder.append("% ");
        if (priceChange >= 0) {
            stringBuilder.append("\uD83D\uDCC8 \n");
        } else {
            stringBuilder.append("\uD83D\uDCC9 \n");
        }
        return stringBuilder.toString();
    }

}
