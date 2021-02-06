package org.promocat.promocat.data_entities.notification_npd;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.NotifNPDDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.notification_npd.ApiNotifNotFoundException;
import org.promocat.promocat.exception.user.ApiUserNotFoundException;
import org.promocat.promocat.mapper.NotifNPDMapper;
import org.promocat.promocat.mapper.UserMapper;
import org.promocat.promocat.utils.soap.SoapClient;
import org.promocat.promocat.utils.soap.operations.notifications.PostNotificationsAckRequest;
import org.promocat.promocat.utils.soap.operations.pojo.PostNotificationsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by Danil Lyskin at 10:47 02.02.2021
 */

@Service
@Slf4j
public class NotifNPDService {

    private final NotifNPDMapper notifNPDMapper;
    private final NotifNPDRepository notifNPDRepository;
    private final SoapClient soapClient;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public NotifNPDService(final NotifNPDMapper notifNPDMapper,
                           final NotifNPDRepository notifNPDRepository,
                           final SoapClient soapClient,
                           final UserRepository userRepository,
                           final UserMapper userMapper) {

        this.notifNPDMapper = notifNPDMapper;
        this.notifNPDRepository = notifNPDRepository;
        this.soapClient = soapClient;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Сохраняет уведомление в БД.
     *
     * @param dto объектное представление уведомления, полученное с фронта.
     * @return Представление уведомления, сохраненное в БД. {@link NotifNPDDTO}
     */
    public NotifNPDDTO save(final NotifNPDDTO dto) {
        if (!notifNPDRepository.existsByNotifId(dto.getNotifId())) {
            log.info("Saving notif with id: {}", dto.getNotifId());
            return notifNPDMapper.toDto(notifNPDRepository.save(notifNPDMapper.toEntity(dto)));
        }
        return dto;
    }

    /**
     * Находит уведомление в БД по id.
     *
     * @param id id уведомления
     * @return объект класса NotifNPDDTO, содержащий все необходимые данные об уведомление.
     * @throws ApiUserNotFoundException если не найдено уведомление с таким id.
     */
    public NotifNPDDTO findById(final Long id) {
        Optional<NotifNPD> notif = notifNPDRepository.findById(id);
        if (notif.isPresent()) {
            log.info("Found notif with id: {}", id);
            return notifNPDMapper.toDto(notif.get());
        } else {
            log.warn("No such notif with id: {}", id);
            throw new ApiNotifNotFoundException(String.format("No notif with such id: %d in db.", id));
        }
    }

    /**
     * Отправляет в налоговую информацию, что уведомление было прочитано.
     * @param dto объектное представление уведомления, полученное с фронта.
     */
    public void sendOpenNotif(NotifNPDDTO dto) {
        Optional<User> op = userRepository.findById(dto.getUserId());

        if (op.isPresent()) {
            UserDTO user = userMapper.toDto(op.get());
            PostNotificationsRequest post = new PostNotificationsRequest(String.valueOf(user.getInn()),
                                                dto.getNotifId());

            PostNotificationsAckRequest postNotificationsAckRequest = new PostNotificationsAckRequest();
            postNotificationsAckRequest.setNotificationList(List.of(post));
            soapClient.send(postNotificationsAckRequest);
        } else {
            throw new ApiUserNotFoundException(String.format("User with id %d not found", dto.getUserId()));
        }
    }
}