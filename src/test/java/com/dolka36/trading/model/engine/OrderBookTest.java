package com.dolka36.trading.model.engine;

import com.dolka36.trading.model.Order;
import com.dolka36.trading.model.OrderType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderBookTest {


    @Test
    public void shouldPrioritizeHighestPriceForBids(){
        OrderBook orderBook = new OrderBook("BTC");
        Order order = new Order(
                1,
                "1",
                "BTC",
                OrderType.BUY,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(10),
                1
        );
        Order order2 = new Order(
                2,
                "2",
                "BTC",
                OrderType.BUY,
                BigDecimal.valueOf(150),
                BigDecimal.valueOf(20),
                2
        );

        orderBook.addOrder(order);
        orderBook.addOrder(order2);

        Assertions.assertEquals(BigDecimal.valueOf(150), orderBook.getBids().peek().price());
    }

    @Test
    public void shouldPrioritizeLowestPriceForAsks(){
        OrderBook orderBook = new OrderBook("BTC");
        Order order = new Order(
                1,
                "1",
                "BTC",
                OrderType.SELL,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(10),
                1
        );
        Order order2 = new Order(
                2,
                "2",
                "BTC",
                OrderType.SELL,
                BigDecimal.valueOf(150),
                BigDecimal.valueOf(20),
                2
        );
        orderBook.addOrder(order);
        orderBook.addOrder(order2);

        Assertions.assertEquals(BigDecimal.valueOf(100), orderBook.getAsks().peek().price());
    }

    @Test
    public void shouldThrowExceptionWhenAssetMismatched(){
        OrderBook orderBook = new OrderBook("BTC");
        Order order = new Order(
                1,
                "1",
                "BTC",
                OrderType.SELL,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(10),
                1
        );
        Order order2 = new Order(
                1,
                "1",
                "ETH",
                OrderType.SELL,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(10),
                1
        );

        assertThrows(IllegalArgumentException.class, () -> orderBook.addOrder(order2));
    }
}