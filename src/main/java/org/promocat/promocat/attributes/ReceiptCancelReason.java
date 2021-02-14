package org.promocat.promocat.attributes;

import lombok.Getter;

/**
 * Причина отзыва чека.
 */
public enum ReceiptCancelReason {

    /**
     * Чек сформирован ошибочно.
     */
    GENERATED_INCORRECTLY("REGISTRATION_MISTAKE"),
    /**
     * Возврат средств.
     */
    REFUND("REFUND");

    @Getter
    String ruString;

    ReceiptCancelReason(String s) {
        this.ruString = s;
    }

    @Override
    public String toString() {
        return ruString;
    }
}
