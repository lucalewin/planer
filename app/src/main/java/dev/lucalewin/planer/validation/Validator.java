package dev.lucalewin.planer.validation;

/**
 * @author Luca Lewin
 * @since Planer v1.4.1
 * @param <T>
 */
public abstract class Validator<T> {

    /**
     *
     * @param t the input to be validated
     * @return true if the input is valid, otherwise false
     */
    public abstract boolean isValid(T t);

}
