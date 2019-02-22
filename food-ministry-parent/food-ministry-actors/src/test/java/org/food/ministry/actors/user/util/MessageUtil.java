package org.food.ministry.actors.user.util;

import org.food.ministry.actors.messages.AResultMessage;
import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.messages.IMessage;
import org.food.ministry.actors.util.Constants;
import org.junit.Assert;

public abstract class MessageUtil {

    public static final String CORRUPTED_DATA_SOURCE_MESSAGE = "Underlying data source not responding correctly";
    
    private MessageUtil() {
        /* static class only */
    }
    
    /**
     * Utility function to identify a subtype of the given {@link IMessage} objects. If no message is of the
     * searched type null is returned.
     * @param classOfMessage The class to search for
     * @param messages All messages to be checked
     * @return Return the first instance found or null if none was found
     */
    public static <T> T getMessageByClass(Class<T> classOfMessage, IMessage... messages) {
        for(IMessage message: messages) {
            if(message.getClass().equals(classOfMessage)) {
                return (T) message;
            }
        }
        return null;
    }
    
    /**
     * Checks that the result message has no error and compares it to the content of the delegate message
     * @param expectedResponseMessageId The expected id of the result message
     * @param resultMessage The result message
     * @param delegateMessage The delegate message
     */
    public static void checkNoErrorMessage(long expectedResponseMessageId, AResultMessage resultMessage, DelegateMessage delegateMessage) {
        Assert.assertTrue(resultMessage.isSuccessful());
        Assert.assertEquals(Constants.NO_ERROR_MESSAGE, resultMessage.getErrorMessage());
        checkForSameOrigin(expectedResponseMessageId, resultMessage, delegateMessage);
    }
    
    /**
     * Checks that the result message has an error with the given error message and compares it to the content of the delegate message
     * @param expectedResponseMessageId The expected id of the result message
     * @param expectedErrorMessage The expected error message
     * @param resultMessage The result message
     * @param delegateMessage The delegate message
     */
    public static void checkForErrorMessage(long expectedResponseMessageId, String expectedErrorMessage, AResultMessage resultMessage, DelegateMessage delegateMessage) {
        Assert.assertFalse(resultMessage.isSuccessful());
        Assert.assertEquals(expectedErrorMessage, resultMessage.getErrorMessage());
        checkForSameOrigin(expectedResponseMessageId, resultMessage, delegateMessage);
    }
    
    private static void checkForSameOrigin(long expectedResponseMessageId, AResultMessage resultMessage, DelegateMessage delegateMessage) {
        Assert.assertEquals(expectedResponseMessageId, resultMessage.getOriginId());
        Assert.assertEquals(resultMessage.getOriginId(), delegateMessage.getOriginId());
    }
}
