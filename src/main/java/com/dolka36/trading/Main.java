package com.dolka36.trading;

import com.dolka36.trading.model.Order;
import com.dolka36.trading.model.OrderType;
import com.dolka36.trading.model.Portfolio;
import com.dolka36.trading.model.Trade;
import com.dolka36.trading.model.engine.MatchingEngine;
import com.dolka36.trading.model.engine.OrderBook;
import com.dolka36.trading.service.TradingService;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 🚀 ЗАПУСК БИРЖЕВОГО ДВИЖКА ===\n");

        // 1. Инициализация компонентов биржи
        OrderBook btcOrderBook = new OrderBook("BTC");
        MatchingEngine matchingEngine = new MatchingEngine();
        TradingService tradingService = new TradingService(btcOrderBook, matchingEngine, "USD");

        // 2. Создание портфелей пользователей
        Portfolio alicePortfolio = new Portfolio("user_alice");
        alicePortfolio.deposit("USD", BigDecimal.valueOf(50000)); // Алиса стартует с $50,000

        Portfolio bobPortfolio = new Portfolio("user_bob");
        bobPortfolio.deposit("BTC", BigDecimal.valueOf(2));      // Боб стартует с 2 BTC

        tradingService.registerPortfolio(alicePortfolio);
        tradingService.registerPortfolio(bobPortfolio);

        printBalances("Начальные балансы", alicePortfolio, bobPortfolio);

        // 3. Боб размещает ордер на продажу 1 BTC за $45,000
        Order sellOrder = new Order(
                1L,
                "user_bob",
                "BTC",
                OrderType.SELL,
                BigDecimal.valueOf(45000),
                BigDecimal.valueOf(1),
                System.currentTimeMillis()
        );
        System.out.println("📤 Боб выставляет ордер: SELL 1 BTC @ $45,000");
        tradingService.placeOrder(sellOrder);

        // 4. Алиса размещает ордер на покупку 1 BTC за $45,000
        Order buyOrder = new Order(
                2L,
                "user_alice",
                "BTC",
                OrderType.BUY,
                BigDecimal.valueOf(45000),
                BigDecimal.valueOf(1),
                System.currentTimeMillis()
        );
        System.out.println("📥 Алиса выставляет ордер: BUY 1 BTC @ $45,000\n");
        List<Trade> trades = tradingService.placeOrder(buyOrder);

        // 5. Вывод результатов сделок
        System.out.println("=== ⚡ СОВЕРШЕННЫЕ СДЕЛКИ ===");
        for (Trade trade : trades) {
            System.out.println("✅ Сделка #" + trade.tradeId() + ": " + trade.quantity() + " " + trade.asset() +
                    " по цене $" + trade.price());
        }
        System.out.println();

        printBalances("Итоговые балансы после торгов", alicePortfolio, bobPortfolio);
    }

    private static void printBalances(String header, Portfolio alice, Portfolio bob) {
        System.out.println("--- " + header + " ---");
        System.out.println("Алиса -> USD: $" + alice.getBalance("USD") + " | BTC: " + alice.getBalance("BTC"));
        System.out.println("Боб   -> USD: $" + bob.getBalance("USD") + " | BTC: " + bob.getBalance("BTC"));
        System.out.println("-----------------------------------\n");
    }
}