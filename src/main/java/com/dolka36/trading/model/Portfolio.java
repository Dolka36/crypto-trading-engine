package com.dolka36.trading.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Portfolio {
    private final String userId;
    private final Map<String, BigDecimal> balances;

    public Portfolio(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("UserId не может быть null или пустым");
        }
        this.userId = userId;
        this.balances = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getBalance(String asset) {
        if (asset == null || asset.isBlank()) {
            return BigDecimal.ZERO;
        }
        return balances.getOrDefault(asset, BigDecimal.ZERO);
    }

    public void deposit(String asset, BigDecimal amount) {
        if (asset == null || asset.isBlank()) {
            throw new IllegalArgumentException("актив не может быть null или пустым");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("сумма пополнения должна быть больше 0");
        }
        balances.put(asset, getBalance(asset).add(amount));
    }

    public void withdraw(String asset, BigDecimal amount) {
        if (asset == null || asset.isBlank()) {
            throw new IllegalArgumentException("актив не может быть null или пустым");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("сумма списания должна быть больше 0");
        }

        if (getBalance(asset).compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств");
        }

        balances.put(asset, getBalance(asset).subtract(amount));

    }
}
