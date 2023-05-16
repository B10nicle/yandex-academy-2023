package ru.yandex.yandexlavka.error.exception;

/**
 * @author Oleg Khilko
 */

public class CourierHasZeroCompletedOrdersException extends RuntimeException {
    public CourierHasZeroCompletedOrdersException(String message) {
        super(message);
    }
}
