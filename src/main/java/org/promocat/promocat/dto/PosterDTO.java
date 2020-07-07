package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Blob;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:26 27.06.2020
 */
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosterDTO extends AbstractDTO {
    private String fileName;
    private String dataType;
    private Blob poster;
}
