package com.isvora.corvus.controller;


import com.isvora.corvus.database.Symbol;
import com.isvora.corvus.mapper.SymbolMapper;
import com.isvora.corvus.service.StockTwitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SymbolController {

    private final StockTwitsService stockTwitsService;

    @GetMapping("/upcoming-tickers")
    public ResponseEntity<List<Symbol>> getUpcomingTickers() {
        var stocktwitsResource = stockTwitsService.getStockTwitsResponse();
        return new ResponseEntity<>(SymbolMapper.map(stocktwitsResource), HttpStatus.OK);
    }
}
