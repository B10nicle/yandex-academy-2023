package ru.yandex.yandexlavka.error.exception;

/**
 * @author Oleg Khilko
 */

public class PageDoesNotExistException extends RuntimeException {
    public PageDoesNotExistException(String message) {
        super(message);
    }
}
