package org.promocat.promocat.utils.soap.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.promocat.promocat.constraints.XmlField;

import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:20 24.07.2020
 */
@Data
@AllArgsConstructor
public class PostUnbindPartnerResponse {

    @XmlField("UnregistrationTime")
    private ZonedDateTime unregistrationTime;
}
