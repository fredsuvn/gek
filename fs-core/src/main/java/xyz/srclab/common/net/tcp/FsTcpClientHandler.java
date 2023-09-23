package xyz.srclab.common.net.tcp;

import java.nio.ByteBuffer;

/**
 * TCP/IP network handler for client actions.
 *
 * @author fredsuvn
 */
public interface FsTcpClientHandler {

    /**
     * Callback when the channel was opened.
     *
     * @param channel the channel
     */
    default void onOpen(FsTcpChannel channel) {
    }

    /**
     * Callback when the channel was closed.
     *
     * @param channel the channel
     * @param buffer  remaining buffer of the channel, readonly and initial position is 0
     */
    default void onClose(FsTcpChannel channel, ByteBuffer buffer) {
    }

    /**
     * Callback when an exception occurs in the channel.
     *
     * @param channel   the channel
     * @param throwable the exception
     * @param buffer    remaining buffer of the channel, readonly and initial position is 0
     */
    default void onException(FsTcpChannel channel, Throwable throwable, ByteBuffer buffer) {
    }

    /**
     * Callback for each read loop of channel.
     * <p>
     * Client has a list of handlers, if there has new data read from remote endpoint,
     * {@link FsTcpChannelHandler#onMessage(FsTcpChannel, Object)} will be called for each handler.
     * <b>Only all onMessage method of handlers have been called</b>, then this method would be called.
     * <p>
     * If an exception is thrown, {@link #onException(FsTcpChannel, Throwable, ByteBuffer)} will be called.
     *
     * @param channel    the channel
     * @param hasNewData whether there has new data read in current loop
     * @param buffer     remaining buffer of the channel, readonly and initial position is 0
     */
    default void onLoop(FsTcpChannel channel, boolean hasNewData, ByteBuffer buffer) {
    }
}