package com.isvora.corvus.service;

import com.isvora.corvus.database.Stock;
import com.isvora.corvus.database.Symbol;
import com.isvora.corvus.repository.SymbolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SymbolService {

    private final SymbolRepository symbolRepository;

    public List<Symbol> getUpcomingTrendingTickers() {
        return symbolRepository.findUpcomingTickers();
    }

    public Symbol save(Symbol symbol) {
        return symbolRepository.save(symbol);
    }

    public List<Symbol> saveAll(List<Symbol> symbols) {
        return symbolRepository.saveAll(symbols);
    }

    public Optional<Symbol> findSymbolByStock(Stock stock) {
        return symbolRepository.findByStock(stock);
    }

    public void deleteSymbol(Symbol symbol) {
        symbolRepository.delete(symbol);
    }
}
