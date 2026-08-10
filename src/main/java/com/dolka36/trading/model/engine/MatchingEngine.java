package com.dolka36.trading.model.engine;

import com.dolka36.trading.model.Order;
import com.dolka36.trading.model.Trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MatchingEngine {
    private long tradeIdCounter = 1;

    public List<Trade> match(OrderBook orderBook) {
        List<Trade> trades = new ArrayList<>();

        while (!orderBook.getBids().isEmpty() && !orderBook.getAsks().isEmpty()) {
            Order buy = orderBook.getBids().peek();
            Order sell = orderBook.getAsks().peek();

            if (buy.price().compareTo(sell.price()) < 0) {
                break;
            }

            orderBook.getBids().poll();
            orderBook.getAsks().poll();

            BigDecimal tradeQuantity = buy.quantity().min(sell.quantity());

            Trade trade = new Trade(
                    tradeIdCounter++,
                    buy.id(),
                    sell.id(),
                    orderBook.getAsset(),
                    sell.price(),
                    tradeQuantity,
                    System.currentTimeMillis()
            );
            trades.add(trade);

            if (buy.quantity().compareTo(tradeQuantity) > 0) {
                Order remainingBuy = new Order(
                        buy.id(), buy.userId(), buy.asset(), buy.type(),
                        buy.price(), buy.quantity().subtract(tradeQuantity), buy.timestamp()
                );
                orderBook.addOrder(remainingBuy);
            }

            if (sell.quantity().compareTo(tradeQuantity) > 0) {
                Order remainingSell = new Order(
                        sell.id(), sell.userId(), sell.asset(), sell.type(),
                        sell.price(), sell.quantity().subtract(tradeQuantity), sell.timestamp()
                );
                orderBook.addOrder(remainingSell);
            }
        }

        return trades;
    }
}