package org.promocat.promocat.utils.soap.util;

import org.promocat.promocat.exception.util.ApiServerErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.promocat.promocat.utils.soap.attributes.ConnectionPermissions.CANCEL_INCOME;
import static org.promocat.promocat.utils.soap.attributes.ConnectionPermissions.INCOME_LIST;
import static org.promocat.promocat.utils.soap.attributes.ConnectionPermissions.INCOME_REGISTRATION;
import static org.promocat.promocat.utils.soap.attributes.ConnectionPermissions.PAYMENT_INFORMATION;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 10:53 29.07.2020
 */
public final class TaxUtils {

    /**
     * Список разрешений, которые запрашиваются у налогоплательщика.
     */
    public static final List<String> PERMISSIONS = List.of(
            INCOME_REGISTRATION,
            PAYMENT_INFORMATION,
            CANCEL_INCOME,
            INCOME_LIST
    );

    /**
     * Имя отображаемое в Мой налог.
     */
    public static final String PROMOCAT_NAME = "PromoCat";

    /**
     * ИНН PromoCat.
     */
    public static final String PROMOCAT_INN = "7802704195";

    /**
     * Описание сервиса для Мой налог
     */
    public static final String PROMOCAT_DESCRIPTION = "PromoCat — это новая революционная платформа, которая позволяет" +
            " владельцам автомобилей практически не тратить деньги на топливо или существенно на нём экономить. " +
            "Всё просто. Вам необходимо приехать на любую АЗС нашего партнёра, где вам на заднее стекло автомобиля " +
            "наклеят перфорированный рекламный постер. В рамках действия рекламной акции вы сможете получать деньги за " +
            "каждый километр пути, которые PromoCat будет переводить вам на банковский счёт.";

    /**
     * Текст отображаемый в Мой налог. (Мини описание)
     */
    public static final String PROMOCAT_TEXT = "PromoCat — размещайте рекламу на своём авто и" +
            " экономьте до 100% на топливе";

    /**
     * Описание услуги.
     */
    public static final String TAX_SERVICE_DESCRIPTION = "Продвижение товаров и услуг";

    /**
     * Ссылка на сайт PromoCat.
     */
    public static final String PROMOCAT_TRANSITION_LINK = "https://promocatcompany.com/ru/sz";

    /**
     * Телефон PromoCat
     */
    public static final String PROMOCAT_PHONE = "+79062587099";

    /**
     * Логотип PromoCat в base64.
     */
    public static final String PROMOCAT_LOGO;

    static {
        try {
            PROMOCAT_LOGO = Files.readString(
                    Path.of("src/main/resources/data/admin/examples/logo_example_tax_base64"));
        } catch (IOException e) {
            throw new ApiServerErrorException("logo error");
        }
    }

    /**
     * Изменение формата телефона
     *
     * @param phone телефон в формате +7(XXX)XXX-XX-XX
     * @return телефон в формамте 7XXXXXXXXXX
     */
    public static String reformatPhone(final String phone) {
        return phone.replace("+", "")
                .replace("(", "")
                .replace(")", "")
                .replace("-", "");
    }
}
