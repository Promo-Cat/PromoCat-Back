package org.promocat.promocat.utils.soap.operations;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public abstract class AbstractOperation {

    public abstract Class<? extends AbstractOperation> getResponseClass();

}
