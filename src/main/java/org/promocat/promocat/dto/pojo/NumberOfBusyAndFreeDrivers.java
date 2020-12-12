package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModel;

/**
 * Created by Danil Lyskin at 20:42 12.12.2020
 */

@ApiModel(
        value = "Count free and busy users",
        description = "DTO with count free and busy users"
)
public class NumberOfBusyAndFreeDrivers {
    /**
     * Количество свободных пользователей со статусом Full.
     */
    public Long free;

    /**
     * Количество пользователей, которые в данный момент учавствуют в акциях.
     */
    public Long busy;

    public NumberOfBusyAndFreeDrivers(Long free, Long busy) {
        this.free = free;
        this.busy = busy;
    }
}
