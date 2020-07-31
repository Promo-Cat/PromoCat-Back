package org.promocat.promocat.exception.soap;

public class SoapResponseClassException extends SoapException {

    public SoapResponseClassException(String message) {
        super(String.format("Xml tag %s doesn`t found in response xml", message));
    }
}
