package org.promocat.promocat.data_entities.news_feed;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.dto.NewsFeedDTO;
import org.promocat.promocat.dto.pojo.NotificationDTO;
import org.promocat.promocat.exception.company.ApiCompanyNotFoundException;
import org.promocat.promocat.exception.news_feed.ApiNewsFeedNotFoundException;
import org.promocat.promocat.mapper.NewsFeedMapper;
import org.promocat.promocat.utils.FirebaseNotificationManager;
import org.promocat.promocat.utils.NotificationBuilderFactory;
import org.promocat.promocat.utils.NotificationLoader;
import org.promocat.promocat.utils.TopicGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Danil Lyskin at 21:13 31.10.2020
 */

@Slf4j
@Service
@Transactional
public class NewsFeedService {

    private final NewsFeedMapper newsFeedMapper;
    private final NewsFeedRepository newsFeedRepository;
    private final TopicGenerator topicGenerator;
    private final FirebaseNotificationManager firebaseNotificationManager;
    private final NotificationBuilderFactory notificationBuilderFactory;

    @Autowired
    public NewsFeedService(final NewsFeedMapper newsFeedMapper,
                           final NewsFeedRepository newsFeedRepository,
                           final TopicGenerator topicGenerator,
                           final FirebaseNotificationManager firebaseNotificationManager,
                           final NotificationBuilderFactory notificationBuilderFactory) {
        this.newsFeedMapper = newsFeedMapper;
        this.newsFeedRepository = newsFeedRepository;
        this.topicGenerator = topicGenerator;
        this.firebaseNotificationManager = firebaseNotificationManager;
        this.notificationBuilderFactory = notificationBuilderFactory;
    }

    /**
     * Сохранение новости в БД.
     *
     * @param dto объектное представление новости.
     * @return представление новости в БД. {@link NewsFeedDTO}
     */
    public NewsFeedDTO save(final NewsFeedDTO dto) {
        log.info("Trying to save news with name: {}", dto.getName());
        NotificationLoader.NotificationType notificationType = null;
        String topic;
        if (dto.getType() == AccountType.USER) {
            notificationType = NotificationLoader.NotificationType.USER_NEW_PUBLICATION;
            topic = topicGenerator.getNewsFeedTopicForUser();
        } else {
            notificationType = NotificationLoader.NotificationType.COMPANY_NEW_PUBLICATION;
            topic = topicGenerator.getNewsFeedTopicForCompany();
        }
        NotificationDTO notification = notificationBuilderFactory.getBuilder()
                .getNotification(notificationType)
                .set("publication_name", dto.getName())
                .build();
        firebaseNotificationManager.sendNotificationByTopic(
                notification,
                topic
        );
        return newsFeedMapper.toDto(newsFeedRepository.save(newsFeedMapper.toEntity(dto)));
    }

    /**
     * Поиск новости по {@code id}.
     *
     * @param id новости.
     * @return представление новости в БД. {@link NewsFeedDTO}
     * @throws ApiCompanyNotFoundException если такой новости не существует.
     */
    public NewsFeedDTO findById(final Long id) {
        Optional<NewsFeed> news = newsFeedRepository.findById(id);

        if (news.isPresent()) {
            log.info("Found news with id: {}", id);
            return newsFeedMapper.toDto(news.get());
        } else {
            log.warn("No news with id: {}", id);
            throw new ApiNewsFeedNotFoundException(String.format("No news with such id: %d in db.", id));
        }
    }

    /**
     * Удаление новости по {@code id}.
     *
     * @param id новости.
     */
    public void deleteById(final Long id) {
        log.info("Delete news with id: {}", id);
        newsFeedRepository.deleteById(id);
    }

    /**
     * Поиск всех новостей по {@code type}.
     *
     * @param type новости.
     * @return массив представлений новости в БД. {@link NewsFeedDTO}
     */
    public List<NewsFeedDTO> findByType(final String type) {
        List<NewsFeed> news = newsFeedRepository.findAllByType(AccountType.of(type));

        return news.stream().map(newsFeedMapper::toDto).collect(Collectors.toList());
    }
}
