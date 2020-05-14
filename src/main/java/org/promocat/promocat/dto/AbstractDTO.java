package org.promocat.promocat.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Data
public abstract class AbstractDTO implements Serializable {

    private Long id;

}
