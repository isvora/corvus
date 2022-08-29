package com.isvora.corvus.database;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "symbol")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Symbol {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "rank")
    private int rank;

    @Column(name = "value")
    private double score;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pricing", referencedColumnName = "id")
    private Pricing pricing;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock", referencedColumnName = "stock_id")
    private Stock stock;

    @Override
    public String toString() {
        return "Symbol{" +
                "id=" + id +
                ", rank=" + rank +
                ", score=" + score +
                ", pricing=" + pricing.toString() +
                ", stock=" + stock.toString() +
                '}';
    }

    public String discordFormat(String type) {
        var symbol = this.stock.getTicker();
        var priceChange = this.pricing.getPriceChange();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(type);
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
        stringBuilder.append("-".repeat(100));
        return stringBuilder.toString();
    }
}
