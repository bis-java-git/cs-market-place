package com.silverbars.marketplace.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * Order domain class for open orders
 */
public final class Order {

    /**
     * User Id
     */
    private final String userId;

    /**
     * Order Buy/Sell price
     */
    private final BigDecimal pricePerKg;

    /**
     * Quantity of silver bars to be bought or sold per kg.
     */
    private final BigDecimal quantity;

    /**
     * OrderType buy or sell
     */
    private final OrderType orderType;

    /**
     * Unique id for silver bars order
     */
    private final String uniqueIdForAnOrder;

    /**
     * Generates timestamp for an order
     */
    private final Timestamp timeStamp;

    public Order(String userId,
                 BigDecimal pricePerKg,
                 BigDecimal quantity,
                 OrderType orderType
    ) {
        this.userId = userId;
        this.pricePerKg = pricePerKg;
        this.quantity = quantity;
        this.orderType = orderType;
        uniqueIdForAnOrder = UUID.randomUUID().toString();
        timeStamp=new Timestamp(System.currentTimeMillis());
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getPricePerKg() {
        return pricePerKg;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public String getUniqueIdForAnOrder() {
        return uniqueIdForAnOrder;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getUserId(), order.getUserId()) &&
                Objects.equals(getPricePerKg(), order.getPricePerKg()) &&
                Objects.equals(getQuantity(), order.getQuantity()) &&
                getOrderType() == order.getOrderType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getPricePerKg(), getQuantity(), getOrderType(), getUniqueIdForAnOrder(), getTimeStamp());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", pricePerKg=").append(pricePerKg);
        sb.append(", quantity=").append(quantity);
        sb.append(", orderType=").append(orderType);
        sb.append('}');
        return sb.toString();
    }
}
