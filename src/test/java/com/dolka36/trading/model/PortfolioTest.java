package com.dolka36.trading.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {

    @Test
    public void shouldDepositSuccessfully() {
        Portfolio portfolio1 = new Portfolio("123");

        portfolio1.deposit("USD", BigDecimal.valueOf(100));

        Assertions.assertEquals(BigDecimal.valueOf(100), portfolio1.getBalance("USD"));
    }

    @Test
    public void shouldWithdrawSuccessfully() {
        Portfolio portfolio1 = new Portfolio("123");

        portfolio1.deposit("USD", BigDecimal.valueOf(100));
        portfolio1.withdraw("USD", BigDecimal.valueOf(40));

        Assertions.assertEquals(BigDecimal.valueOf(60), portfolio1.getBalance("USD"));
    }

    @Test
    public void shouldThrowExceptionWhenInsufficientFunds() {
        Portfolio portfolio1 = new Portfolio("123");

        portfolio1.deposit("USD", BigDecimal.valueOf(100));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> portfolio1.withdraw("USD", BigDecimal.valueOf(140)));
    }


    @Test
    public void shouldReturnZeroForNonExistentAsset() {
        Portfolio portfolio1 = new Portfolio("123");

        Assertions.assertEquals(BigDecimal.ZERO, portfolio1.getBalance("BTC"));
    }
}