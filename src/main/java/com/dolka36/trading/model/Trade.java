package com.dolka36.trading.model;

import java.math.BigDecimal;

public record Trade(
    long tradeId, long buyOrderId, long sellOrderId, String asset, BigDecimal price, BigDecimal quantity, long timestamp
) {
    public Trade{
        if (tradeId <= 0){
            throw new IllegalArgumentException("ID сделки должен быть больше 0");
        }
        if (buyOrderId <= 0){
            throw new IllegalArgumentException("ID ордера на покупку должен быть больше 0");
        }
        if (sellOrderId <= 0){
            throw new IllegalArgumentException("ID ордера на продажу должен быть больше 0");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("цена сделки должна быть больше 0");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("объём совершенной сделки должен быть больше 0");
        }
        if (asset == null || asset.isBlank()){
            throw new IllegalArgumentException("актив не может быть пустым");
        }
    }
}
