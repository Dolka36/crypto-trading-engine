package com.dolka36.trading.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    public void shouldCreateOrderSuccessfully(){
        Assertions.assertDoesNotThrow(() -> {
            Order order = new Order(
                    1,
                    "1",
                    "USD",
                    OrderType.BUY,
                    BigDecimal.valueOf(100),
                    BigDecimal.valueOf(10),
                    1
            );
        });
    }

    @Test
    public void shouldThrowExceptionWhenPriceIsNegative(){
        assertThrows(IllegalArgumentException.class, () -> {
            Order order = new Order(
                    1,
                    "1",
                    "USD",
                    OrderType.BUY,
                    BigDecimal.valueOf(-100), // отрицательная цена
                    BigDecimal.valueOf(10),
                    1
            );
        });
    }
}