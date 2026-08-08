package com.dolka36.trading.model;

import java.math.BigDecimal;

public record Order(
        long id, String userId, String asset, OrderType type, BigDecimal price, BigDecimal quantity, long timestamp
) {
    public Order {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Цена должна быть больше 0");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше 0");
        }
        if (id <= 0){
            throw new IllegalArgumentException("Id заявки должен быть больше 0");
        }
        if (userId == null || userId.isBlank()){
            throw new IllegalArgumentException("Id пользователя не может быть пустым");
        }
        if (asset == null || asset.isBlank()){
            throw new IllegalArgumentException("Название актива не может быть пустым");
        }
    }
}