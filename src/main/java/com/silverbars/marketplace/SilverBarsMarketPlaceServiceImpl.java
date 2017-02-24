package com.silverbars.marketplace;

import ch.qos.logback.classic.Logger;
import com.silverbars.marketplace.domain.Order;
import com.silverbars.marketplace.domain.OrderType;
import com.silverbars.marketplace.domain.ui.DisplayOrder;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Queue;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Implementation for silver bars market place service
 * Provides following functionality:
 * 1. Register an order
 * 2. Cancels an registered order
 * 3. Gets all live orders
 * 4. Get summary information for live orders
 */
public class SilverBarsMarketPlaceServiceImpl implements SilverBarsMarketPlaceService {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(SilverBarsMarketPlaceServiceImpl.class);

    //In memory store to for all the orders
    private final Queue<Order> orders = new ConcurrentLinkedQueue<>();

    /**
     * Adds new order to order list
     *
     * @param newOrder new incoming order
     */
    @Override
    public void registerAnOrder(final Order newOrder) {
        logger.info("Registering an order {} ", newOrder);
        orders.add(newOrder);
    }

    /**
     * Cancels registered order
     *
     * @param cancelOrder cancelled order
     */
    @Override
    public void cancelAnOrder(final Order cancelOrder) {
        logger.info("Cancelling an order {} ", cancelOrder);
        orders.remove(cancelOrder);
    }

    /**
     * Returns all live orders summary for buy or sell
     *
     * @return Orders summary information for buy or sell
     */
    @Override
    public List<DisplayOrder> getSummaryInformationForLiveOrders(final OrderType orderType) {
        logger.info("Getting summary information for live order [order type = {} ]", orderType);
        Map<BigDecimal, List<Order>> liveOrderMap = orders.parallelStream().filter(o -> o.getOrderType().equals(orderType)).collect(Collectors.groupingBy(Order::getQuantity));

        Set<BigDecimal> liveOrderList = orderType.equals(OrderType.BUY) ? liveOrderMap.keySet() : new TreeSet<>(liveOrderMap.keySet());
        logger.info("Getting summary information for live order [ orderType = {} ] [ total summary live orders= {} ]", orderType, liveOrderList.size());

        return liveOrderList.parallelStream().map(item -> new DisplayOrder(item, liveOrderMap.get(item).stream()
                .map(Order::getPricePerKg).reduce(BigDecimal.ZERO, BigDecimal::add),
                orderType)).collect(toList());
    }

    /**
     * Get all live orders
     *
     * @param orderType Buy or Sell
     * @return list of orders
     */
    @Override
    public List<Order> getAllLiveOrders(final OrderType orderType) {
        List<Order> liveOrderList = orders.parallelStream().filter(o -> o.getOrderType().equals(orderType)).collect(toList());
        logger.info("Getting all live orders [order type {} ] [Total live orders = {} ]", orderType, liveOrderList.size());
        return liveOrderList;
    }
}
