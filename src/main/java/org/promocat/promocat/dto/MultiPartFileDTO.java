package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Blob;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:26 27.06.2020
 */
// TODO docs
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiPartFileDTO extends AbstractDTO {
    private String fileName;
    private String dataType;
    private Blob blob;
}
