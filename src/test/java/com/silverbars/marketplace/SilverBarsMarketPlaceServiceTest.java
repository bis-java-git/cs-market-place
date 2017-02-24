package com.silverbars.marketplace;

import ch.qos.logback.classic.Logger;
import com.silverbars.marketplace.domain.Order;
import com.silverbars.marketplace.domain.OrderType;
import com.silverbars.marketplace.domain.ui.DisplayOrder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

public class SilverBarsMarketPlaceServiceTest {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(SilverBarsMarketPlaceServiceTest.class);

    private final SilverBarsMarketPlaceService silverBarsMarketPlaceService = new SilverBarsMarketPlaceServiceImpl();

    private long totalLiveBuyOrders;

    private long totalLiveSellOrders;

    private List<Order> liveOrderList = Arrays.asList(
            new Order("1", new BigDecimal("3.5"), new BigDecimal("306"), OrderType.SELL),
            new Order("5", new BigDecimal("3.5"), new BigDecimal("306"), OrderType.BUY),
            new Order("5", new BigDecimal("3.5"), new BigDecimal("306"), OrderType.BUY),
            new Order("3", new BigDecimal("1.5"), new BigDecimal("307"), OrderType.SELL),
            new Order("3", new BigDecimal("1.5"), new BigDecimal("307"), OrderType.SELL),
            new Order("6", new BigDecimal("1.5"), new BigDecimal("307"), OrderType.BUY),
            new Order("4", new BigDecimal("2.0"), new BigDecimal("306"), OrderType.SELL),
            new Order("2", new BigDecimal("1.2"), new BigDecimal("310"), OrderType.SELL),
            new Order("7", new BigDecimal("2.0"), new BigDecimal("306"), OrderType.BUY),
            new Order("8", new BigDecimal("1.2"), new BigDecimal("310"), OrderType.BUY),
            new Order("1", new BigDecimal("3.5"), new BigDecimal("306"), OrderType.SELL));

    @Before
    public void setup() {
        liveOrderList.forEach(silverBarsMarketPlaceService::registerAnOrder);
        totalLiveBuyOrders = liveOrderList.stream().filter(o -> o.getOrderType().equals(OrderType.BUY)).count();
        totalLiveSellOrders = liveOrderList.stream().filter(o -> o.getOrderType().equals(OrderType.SELL)).count();
    }

    private Order findAnOrder(OrderType orderType) {
        Optional<Order> orderOptional = silverBarsMarketPlaceService.getAllLiveOrders(orderType).stream().filter(o -> o.getOrderType().equals(orderType)).findAny();
        return orderOptional.orElse(null);
    }

    private void cancelAllOrders() {
        silverBarsMarketPlaceService.getAllLiveOrders(OrderType.SELL).forEach(silverBarsMarketPlaceService::cancelAnOrder);
        silverBarsMarketPlaceService.getAllLiveOrders(OrderType.BUY).forEach(silverBarsMarketPlaceService::cancelAnOrder);
    }

    @Test
    public void shouldHaveLiveSellOrders() {
        assertEquals("Total live sell orders expected:", totalLiveSellOrders, silverBarsMarketPlaceService.getAllLiveOrders(OrderType.SELL).size());
    }

    @Test
    public void shouldHaveLiveBuyOrders() {
        assertEquals("Total live buy orders expected:", totalLiveBuyOrders, silverBarsMarketPlaceService.getAllLiveOrders(OrderType.BUY).size());
    }

    @Test
    public void shouldDisplayLiveOrders() {
        List<DisplayOrder> summaryInformationForLiveSellOrders = silverBarsMarketPlaceService.getSummaryInformationForLiveOrders(OrderType.SELL);
        assertEquals("Total live sell orders summary expected:", 3, summaryInformationForLiveSellOrders.size());
        logger.info("Live sell orders summary: {}", summaryInformationForLiveSellOrders);
        List<DisplayOrder> summaryInformationForLiveBuyOrders = silverBarsMarketPlaceService.getSummaryInformationForLiveOrders(OrderType.BUY);
        assertEquals("Total live buy orders summary expected:", 3, summaryInformationForLiveBuyOrders.size());
        logger.info("Live buy orders summary: {}", summaryInformationForLiveBuyOrders);
    }

    @Test
    public void shouldDisplayLiveBuyOrdersInDescendingOrder() {
        List<DisplayOrder> summaryInformationForLiveBuyOrders = silverBarsMarketPlaceService.getSummaryInformationForLiveOrders(OrderType.BUY);
        for (Integer totalLiveOrderSummaryCount = 0; totalLiveOrderSummaryCount < summaryInformationForLiveBuyOrders.size() - 2; totalLiveOrderSummaryCount++) {
            assertTrue("Live buy orders summary should be in descending order", summaryInformationForLiveBuyOrders.get(totalLiveOrderSummaryCount).getPricePerKg().compareTo(summaryInformationForLiveBuyOrders.get(totalLiveOrderSummaryCount + 1).getPricePerKg()) > 0);
        }
    }

    @Test
    public void shouldDisplayLiveBuyOrdersInDescendingOrderAfterCancellingAnOrder() {
        Order order = findAnOrder(OrderType.BUY);
        silverBarsMarketPlaceService.cancelAnOrder(order);
        assertEquals("Total live buy orders summary expected:", totalLiveBuyOrders - 1, silverBarsMarketPlaceService.getAllLiveOrders(OrderType.BUY).size());

        List<DisplayOrder> summaryInformationForLiveBuyOrders = silverBarsMarketPlaceService.getSummaryInformationForLiveOrders(OrderType.BUY);
        assertTrue("Total live buy orders expected greater or equal to 2 "
                , summaryInformationForLiveBuyOrders.size() >= 2);

        for (Integer totalLiveOrderSummaryCount = 0; totalLiveOrderSummaryCount < summaryInformationForLiveBuyOrders.size() - 2; totalLiveOrderSummaryCount++) {
            assertTrue("Live buy orders summary should be in descending order", summaryInformationForLiveBuyOrders.get(totalLiveOrderSummaryCount).getPricePerKg().compareTo(summaryInformationForLiveBuyOrders.get(totalLiveOrderSummaryCount + 1).getPricePerKg()) > 0);
        }
    }

    @Test
    public void shouldDisplayLiveSellOrdersInAscendingOrder() {
        List<DisplayOrder> summaryInformationForLiveSellOrders = silverBarsMarketPlaceService.getSummaryInformationForLiveOrders(OrderType.SELL);
        assertTrue("Total live sell orders summary should be greater or equals to 2 ", summaryInformationForLiveSellOrders.size() >= 2);
        for (Integer totalSellLiveOrderSummaryCount = 0; totalSellLiveOrderSummaryCount < summaryInformationForLiveSellOrders.size() - 2; totalSellLiveOrderSummaryCount++) {
            assertTrue("Live sell orders summary  should be in descending order", summaryInformationForLiveSellOrders.get(totalSellLiveOrderSummaryCount).getPricePerKg().compareTo(summaryInformationForLiveSellOrders.get(totalSellLiveOrderSummaryCount + 1).getPricePerKg()) < 0);
        }
    }

    @Test
    public void shouldDisplayLiveSellOrdersInAscendingOrderAfterCancellingAnOrder() {
        Order order = findAnOrder(OrderType.SELL);
        silverBarsMarketPlaceService.cancelAnOrder(order);
        assertEquals("Total live sell orders expected:", totalLiveSellOrders - 1, silverBarsMarketPlaceService.getAllLiveOrders(OrderType.SELL).size());

        List<DisplayOrder> summaryInformationForLiveSellOrders = silverBarsMarketPlaceService.getSummaryInformationForLiveOrders(OrderType.SELL);
        assertTrue("Total live sell orders expected greater than or equal to 2", summaryInformationForLiveSellOrders.size() >= 2);

        for (Integer totalLiveSellOrderSummaryCount = 0; totalLiveSellOrderSummaryCount < summaryInformationForLiveSellOrders.size() - 2; totalLiveSellOrderSummaryCount++) {
            assertTrue("Live sell summary order should be in descending order", summaryInformationForLiveSellOrders.get(totalLiveSellOrderSummaryCount).getPricePerKg().compareTo(summaryInformationForLiveSellOrders.get(totalLiveSellOrderSummaryCount + 1).getPricePerKg()) < 0);
        }
    }

    @Test
    public void registerAnOrderTest() {
        silverBarsMarketPlaceService.registerAnOrder(new Order("1", new BigDecimal("3.5"), new BigDecimal("306"), OrderType.SELL));
        assertEquals("Total live sell orders expected:", totalLiveSellOrders + 1, silverBarsMarketPlaceService.getAllLiveOrders(OrderType.SELL).size());

        silverBarsMarketPlaceService.registerAnOrder(new Order("1", new BigDecimal("3.5"), new BigDecimal("306"), OrderType.BUY));
        assertEquals("Total live buy orders expected:", totalLiveBuyOrders + 1, silverBarsMarketPlaceService.getAllLiveOrders(OrderType.BUY).size());

    }

    @Test
    public void cancelAnOrderTest() {
        Order order = findAnOrder(OrderType.SELL);
        silverBarsMarketPlaceService.cancelAnOrder(order);
        assertEquals("Total live sell orders expected:", totalLiveSellOrders - 1, silverBarsMarketPlaceService.getAllLiveOrders(OrderType.SELL).size());

        order = findAnOrder(OrderType.BUY);
        silverBarsMarketPlaceService.cancelAnOrder(order);
        assertEquals("Total live buy orders expected:", totalLiveBuyOrders - 1, silverBarsMarketPlaceService.getAllLiveOrders(OrderType.BUY).size());
    }

    @Test
    public void shouldReturnEmptySummaryInformationForLiveOrdersListWhenNoLiveSellOrders() {
        cancelAllOrders();
        assertThat("Should be empty for live summary sell orders", silverBarsMarketPlaceService.getSummaryInformationForLiveOrders(OrderType.SELL).isEmpty(), is(true));
    }

    @Test
    public void shouldReturnEmptySummaryInformationForLiveOrdersListWhenNoLiveBuyOrders() {
        cancelAllOrders();
        assertThat("Should be empty for live summary buy orders", silverBarsMarketPlaceService.getSummaryInformationForLiveOrders(OrderType.BUY).isEmpty(), is(true));
    }

    @Test
    public void shouldReturnEmptyLiveOrderListWhenNoSellLiveOrders() {
        cancelAllOrders();
        assertThat("Should be empty for live sell orders", silverBarsMarketPlaceService.getAllLiveOrders(OrderType.SELL).isEmpty(), is(true));
    }

    @Test
    public void shouldReturnEmptyLiveOrderListWhenNoBuyLiveOrders() {
        cancelAllOrders();
        assertThat("Should be empty for live buy orders", silverBarsMarketPlaceService.getAllLiveOrders(OrderType.BUY).isEmpty(), is(true));
    }
}
