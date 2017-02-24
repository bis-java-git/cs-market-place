package com.silverbars.marketplace;

import com.silverbars.marketplace.domain.Order;
import com.silverbars.marketplace.domain.OrderType;
import com.silverbars.marketplace.domain.ui.DisplayOrder;

import java.util.List;

/**
 * Interface for silver bars market place service
 */
public interface SilverBarsMarketPlaceService {

    /**
     * Adds new order to order list
     *
     * @param newOrder new incoming order
     */
    void registerAnOrder(final Order newOrder);

    /**
     * Cancels registered order
     *
     * @param cancelOrder registered order
     */
    void cancelAnOrder(final Order cancelOrder);

    /**
     * Returns all live orders summary for buy or sell
     *
     * @return Orders summary information for buy or sell
     */
    List<DisplayOrder> getSummaryInformationForLiveOrders(OrderType orderType);

    /**
     * Get all live orders
     *
     * @param orderType Buy or Sell
     * @return list of orders
     */
    List<Order> getAllLiveOrders(OrderType orderType);
}
