package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.news_feed.NewsFeed;
import org.promocat.promocat.dto.NewsFeedDTO;
import org.springframework.stereotype.Component;

/**
 * Created by Danil Lyskin at 21:19 31.10.2020
 */
@Component
public class NewsFeedMapper extends AbstractMapper<NewsFeed, NewsFeedDTO> {
    public NewsFeedMapper() {
        super(NewsFeed.class, NewsFeedDTO.class);
    }
}
