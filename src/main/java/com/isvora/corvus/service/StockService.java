package com.isvora.corvus.service;

import com.isvora.corvus.database.Stock;
import com.isvora.corvus.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public Optional<Stock> findStockByTicker(String ticker) {
        return stockRepository.findByTicker(ticker);
    }
}
