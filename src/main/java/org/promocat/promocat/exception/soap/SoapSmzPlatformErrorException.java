package org.promocat.promocat.exception.soap;

import lombok.Getter;
import org.promocat.promocat.utils.soap.operations.SmzPlatformError;

public class SoapSmzPlatformErrorException extends SoapException {

    @Getter
    private SmzPlatformError error;

    public SoapSmzPlatformErrorException(SmzPlatformError message) {
        super(message.getMessage());
        this.error = message;
    }
}
