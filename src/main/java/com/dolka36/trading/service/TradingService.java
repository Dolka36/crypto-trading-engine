package com.dolka36.trading.service;

import com.dolka36.trading.model.Order;
import com.dolka36.trading.model.Portfolio;
import com.dolka36.trading.model.Trade;
import com.dolka36.trading.model.engine.MatchingEngine;
import com.dolka36.trading.model.engine.OrderBook;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradingService {
    private final Map<String, Portfolio> portfolios = new HashMap<>();
    private final Map<Long, String> orderToUserMap = new HashMap<>();
    private final OrderBook orderBook;
    private final MatchingEngine matchingEngine;
    private final String quoteAsset;

    public TradingService(OrderBook orderBook, MatchingEngine matchingEngine, String quoteAsset) {
        if (orderBook == null || matchingEngine == null || quoteAsset == null || quoteAsset.isBlank()) {
            throw new IllegalArgumentException("Параметры сервиса не могут быть null или пустыми");
        }
        this.orderBook = orderBook;
        this.matchingEngine = matchingEngine;
        this.quoteAsset = quoteAsset;
    }


    public void registerPortfolio(Portfolio portfolio) {
        if (portfolio == null || portfolio.getUserId() == null) {
            throw new IllegalArgumentException("Портфель не может быть null");
        }
        portfolios.put(portfolio.getUserId(), portfolio);
    }

    public Portfolio getPortfolio(String userId) {
        return portfolios.get(userId);
    }


    public List<Trade> placeOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Ордер не может быть null");
        }
        if (!portfolios.containsKey(order.userId())) {
            throw new IllegalArgumentException("Пользователь с ID " + order.userId() + " не зарегистрирован");
        }

        orderToUserMap.put(order.id(), order.userId());

        orderBook.addOrder(order);

        List<Trade> trades = matchingEngine.match(orderBook);

        for (Trade trade : trades) {
            processTradeBalances(trade);
        }

        return trades;
    }

    private void processTradeBalances(Trade trade) {
        String buyerUserId = orderToUserMap.get(trade.buyOrderId());
        String sellerUserId = orderToUserMap.get(trade.sellOrderId());

        Portfolio buyerPortfolio = portfolios.get(buyerUserId);
        Portfolio sellerPortfolio = portfolios.get(sellerUserId);

        BigDecimal totalCost = trade.price().multiply(trade.quantity());
        String baseAsset = trade.asset();

        buyerPortfolio.withdraw(quoteAsset, totalCost);
        buyerPortfolio.deposit(baseAsset, trade.quantity());

        sellerPortfolio.withdraw(baseAsset, trade.quantity());
        sellerPortfolio.deposit(quoteAsset, totalCost);
    }
}