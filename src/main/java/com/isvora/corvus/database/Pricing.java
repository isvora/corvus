package com.isvora.corvus.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "pricing")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "percentage_price_change")
    private double priceChange;

    @Column(name = "price")
    private double price;

    @OneToOne(mappedBy = "pricing")
    @JsonIgnore
    private Symbol symbol;

    @Override
    public String toString() {
        return "Pricing{" +
                "id=" + id +
                ", priceChange=" + priceChange +
                ", price=" + price +
                '}';
    }
}
