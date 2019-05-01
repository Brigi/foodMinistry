package org.food.ministry.actors.factory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;

import org.food.ministry.actors.factory.messages.UniqueIDErrorMessage;
import org.food.ministry.actors.factory.messages.UniqueIDMessage;
import org.food.ministry.actors.factory.messages.UniqueIDResultMessage;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor provides unique IDs for messages.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class IDGeneratorActor extends AbstractActor {

    /**
     * An endless stream of random long values
     */
    private ThreadLocalRandom randomValueGenerator = ThreadLocalRandom.current();

    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props() {
        return Props.create(IDGeneratorActor.class, IDGeneratorActor::new);
    }

    /**
     * Accepts a {@link UniqueIDMessage} and returns a {@link UniqueIDResultMessage}
     * with the requested ID in it
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(UniqueIDMessage.class, this::getNextID);

        return receiveBuilder.build();
    }

    /**
     * Retrieves the next free ID and sends it back to the requester via a
     * {@link UniqueIDResultMessage}
     * 
     * @param message The {@link UniqueIDMessage} of the requester
     */
    private void getNextID(UniqueIDMessage message) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA1");
        } catch(NoSuchAlgorithmException e) {
            getSender().tell(new UniqueIDErrorMessage("The algorithm for the ID generation isn't supported anymore!"), getSelf());
            return;
        }
        byte[] sha1Hash = mDigest.digest(longToByteArray(randomValueGenerator.nextLong()));
        getSender().tell(new UniqueIDResultMessage(byteArrayToLong(sha1Hash)), getSelf());
    }

    /**
     * Converts the given byte array to a long value. The given byte array must have
     * the size of 8.
     * 
     * @param bytes The value in bytes to be converted to long
     * @return The converted long value
     */
    private long byteArrayToLong(byte[] bytes) {
        long longValue = ((long) bytes[7] << 56) | ((long) bytes[6] & 0xff) << 48 | ((long) bytes[5] & 0xff) << 40 | ((long) bytes[4] & 0xff) << 32 | ((long) bytes[3] & 0xff) << 24
                | ((long) bytes[2] & 0xff) << 16 | ((long) bytes[1] & 0xff) << 8 | ((long) bytes[0] & 0xff);
        return longValue;
    }

    /**
     * Converts the given long value to bytes
     * 
     * @param longValue The value to convert to bytes
     * @return The converted bytes
     */
    private byte[] longToByteArray(long longValue) {
        byte[] bytes = new byte[] { (byte) longValue, (byte) (longValue >> 8), (byte) (longValue >> 16), (byte) (longValue >> 24), (byte) (longValue >> 32),
                (byte) (longValue >> 40), (byte) (longValue >> 48), (byte) (longValue >> 56) };
        return bytes;
    }
}
