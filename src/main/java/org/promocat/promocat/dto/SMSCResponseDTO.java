package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSCResponseDTO {
    public String id;
    public String cnt;
    public String code;
}
