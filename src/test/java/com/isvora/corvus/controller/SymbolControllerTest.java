package com.isvora.corvus.controller;

import com.isvora.corvus.model.*;
import com.isvora.corvus.service.StockTwitsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SymbolController.class)
public class SymbolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockTwitsService stockTwitsService;

    @Test
    public void testUpcomingTickers() throws Exception {
        when(stockTwitsService.getStockTwitsResponse()).thenReturn(
                StocktwitsResource.builder().response(
                        ResponseResource.builder()
                                .ranks(List.of(
                                       RankResource.builder()
                                               .rank(1)
                                               .value(20)
                                               .pricing(
                                                       PricingResource.builder()
                                                               .price(10)
                                                               .priceChange(4)
                                                               .build()
                                               )
                                               .stock(StockResource.builder()
                                                       .name("Tesla")
                                                       .symbol("TSLA")
                                                       .stockId("123")
                                                       .build())
                                               .build()
                                )).build()
                ).build()
        );

        this.mockMvc.perform(get("/upcoming-tickers")).andExpect(status().isOk())
                .andExpect(content().string(containsString("TSLA")));
    }
}
