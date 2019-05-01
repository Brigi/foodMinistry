package org.food.ministry.actors.util;

/**
 * This class bundles all constants used in several classes.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public final class Constants {

    private Constants() {
        /* No constructor needed as this a static class only */}

    /**
     * Message for indicating that there was no error during processing
     */
    public static final String NO_ERROR_MESSAGE = "No Error.";

    /**
     * Message for indicating that the password or email address doesn't match
     * during login process
     */
    public static final String EMAIL_ADDRESS_ALREADY_EXISTS_MESSAGE = "The provided Email address is already in use. Please choose an unused one.";

    /**
     * Message for indicating that the password or email address doesn't match
     * during login process
     */
    public static final String WRONG_CREDENTIALS_MESSAGE = "Email address and/or password is incorrect.";
}
