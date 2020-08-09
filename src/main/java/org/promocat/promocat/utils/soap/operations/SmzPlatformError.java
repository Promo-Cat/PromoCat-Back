package org.promocat.promocat.utils.soap.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.constraints.XmlInnerObject;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SmzPlatformError extends AbstractOperation {

    @Data
    @NoArgsConstructor
    public static class Args {
        @XmlField("Key")
        private String key;

        @XmlField("Value")
        private String value;
    }


    @XmlField("Code")
    private String code;

    @XmlField("Message")
    private String message;

    @XmlField("Args")
    @XmlInnerObject(Args.class)
    private List<Args> args;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
