package com.dolka36.trading.model.engine;


import com.dolka36.trading.model.Order;
import com.dolka36.trading.model.OrderType;

import java.util.Comparator;
import java.util.PriorityQueue;

public class OrderBook {
    private final String asset;
    private final PriorityQueue<Order> bids;
    private final PriorityQueue<Order> asks;

    public OrderBook(String asset) {
        if (asset == null || asset.isBlank()) {
            throw new IllegalArgumentException("Актив не может быть равен null или пустым");
        }
        this.asset = asset;
        Comparator<Order> bidsComparator = Comparator
                .comparing(Order::price, Comparator.reverseOrder())
                .thenComparingLong(Order::timestamp);

        Comparator<Order> asksComparator = Comparator
                .comparing(Order::price)
                .thenComparingLong(Order::timestamp);

        this.bids = new PriorityQueue<>(bidsComparator);
        this.asks = new PriorityQueue<>(asksComparator);
    }

    public String getAsset(){
        return asset;
    }

    public void addOrder(Order order){
        if (order == null || !order.asset().equalsIgnoreCase(this.asset)){
            throw new IllegalArgumentException("Ордер не может быть null и актив должен совпадать");
        }
        if (order.type() == OrderType.BUY) {
            bids.add(order);
        } else {
            asks.add(order);
        }
    }

    public PriorityQueue<Order> getBids() {
        return bids;
    }

    public PriorityQueue<Order> getAsks() {
        return asks;
    }
}
