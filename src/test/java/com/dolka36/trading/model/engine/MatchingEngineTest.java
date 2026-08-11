package com.dolka36.trading.model.engine;

import com.dolka36.trading.model.Order;
import com.dolka36.trading.model.OrderType;
import com.dolka36.trading.model.Trade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchingEngineTest {

    @Test
    public void shouldMatchOrdersFullyWhenPricesAndQuantitiesMatch() {
        MatchingEngine engine = new MatchingEngine();
        OrderBook orderBook = new OrderBook("BTC");
        Order buyOrder = new Order(
                1L,
                "user1",
                "BTC",
                OrderType.BUY,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(1),
                System.currentTimeMillis()
        );
        Order sellOrder = new Order(
                2L,
                "user2",
                "BTC",
                OrderType.SELL,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(1),
                System.currentTimeMillis()
        );
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        List<Trade> trades = engine.match(orderBook);

        Assertions.assertEquals(1, trades.size());
        Assertions.assertTrue(orderBook.getBids().isEmpty());
        Assertions.assertTrue(orderBook.getAsks().isEmpty());
    }

    @Test
    public void shouldHandlePartialMatchingCorrectly(){
        MatchingEngine engine = new MatchingEngine();
        OrderBook orderBook = new OrderBook("BTC");
        Order buyOrder = new Order(
                1L,
                "user1",
                "BTC",
                OrderType.BUY,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(2),
                System.currentTimeMillis()
        );
        Order sellOrder = new Order(
                2L,
                "user2",
                "BTC",
                OrderType.SELL,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(1),
                System.currentTimeMillis()
        );

        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        List<Trade> trades = engine.match(orderBook);

        Assertions.assertEquals(1, trades.size());
        Assertions.assertTrue(orderBook.getAsks().isEmpty());
        Assertions.assertEquals(1, orderBook.getBids().size());
        Assertions.assertEquals(BigDecimal.valueOf(1), orderBook.getBids().peek().quantity());
    }

    @Test
    public void shouldNotMatchWhenPricesDoNotCross(){
        MatchingEngine engine = new MatchingEngine();
        OrderBook orderBook = new OrderBook("BTC");
        Order buyOrder = new Order(
                1L,
                "user1",
                "BTC",
                OrderType.BUY,
                BigDecimal.valueOf(90),
                BigDecimal.valueOf(1),
                System.currentTimeMillis()
        );
        Order sellOrder = new Order(
                2L,
                "user2",
                "BTC",
                OrderType.SELL,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(1),
                System.currentTimeMillis()
        );

        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        List<Trade> trades = engine.match(orderBook);

        Assertions.assertEquals(0, trades.size());
        Assertions.assertEquals(1, orderBook.getBids().size());
        Assertions.assertEquals(1, orderBook.getAsks().size());
    }

}