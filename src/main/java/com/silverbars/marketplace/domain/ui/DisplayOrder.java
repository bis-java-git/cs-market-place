package com.silverbars.marketplace.domain.ui;

import com.silverbars.marketplace.domain.OrderType;

import java.math.BigDecimal;

public final class DisplayOrder {

    /**
     * Order Buy/Sell price
     */
    private final BigDecimal pricePerKg;

    /**
     * Number of silver bars to be bought or sold per kg.
     */
    private final BigDecimal quantity;

    /**
     * OrderType buy or sell
     */
    private final OrderType orderType;

    public DisplayOrder(
            BigDecimal pricePerKg,
            BigDecimal quantity,
            OrderType orderType
    ) {
        this.pricePerKg = pricePerKg;
        this.quantity = quantity;
        this.orderType = orderType;
    }

    public BigDecimal getPricePerKg() {
        return pricePerKg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DisplayOrder{");
        sb.append("pricePerKg=").append(pricePerKg);
        sb.append(", quantity=").append(quantity);
        sb.append(", orderType=").append(orderType);
        sb.append('}');
        return sb.toString();
    }
}
