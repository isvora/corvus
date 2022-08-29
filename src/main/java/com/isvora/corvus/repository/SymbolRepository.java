package com.isvora.corvus.repository;

import com.isvora.corvus.database.Stock;
import com.isvora.corvus.database.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SymbolRepository extends JpaRepository<Symbol, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * " +
            "FROM symbol " +
            "WHERE rank > 10 AND rank < 21")
    List<Symbol> findUpcomingTickers();

    Optional<Symbol> findByStock(Stock stock);
}
