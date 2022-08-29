package com.isvora.corvus.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "stock")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @Id
    @Column(name = "stock_id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "symbol")
    private String ticker;

    @OneToOne(mappedBy = "stock")
    @JsonIgnore
    private Symbol symbol;

    @Override
    public String toString() {
        return "Stock{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ticker='" + ticker + '\'' +
                '}';
    }
}
