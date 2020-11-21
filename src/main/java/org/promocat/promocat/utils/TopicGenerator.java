package org.promocat.promocat.utils;

import org.promocat.promocat.dto.StockDTO;
import org.springframework.stereotype.Component;

@Component
public class TopicGenerator {

    private static final String NEW_STOCK_USER_TOPIC = "NEW_STOCK_USER";
    private static final String NEWS_FEED_POST_TOPIC = "NEWS_FEED";
    private static final String STOCK_USER_TOPIC_PREFIX = "STOCK_USER_";
    private static final String STOCK_COMPANY_TOPIC_PREFIX = "STOCK_STATUS_";
    private static final String NEW_STOCK_ADMIN_TOPIC = "NEW_STOCK_ADMIN";

    public String getNewStockTopicForUser() {
        return NEW_STOCK_USER_TOPIC;
    }

    public String getNewsFeedTopic() {
        return NEWS_FEED_POST_TOPIC;
    }

    public String getStockTopicForUser(StockDTO stock) {
        return STOCK_USER_TOPIC_PREFIX + stock.getId();
    }

    public String getStockTopicForCompany(StockDTO stock) {
        return STOCK_COMPANY_TOPIC_PREFIX + stock.getId();
    }

    public String getNewStockTopicForAdmin() {
        return NEW_STOCK_ADMIN_TOPIC;
    }

}
