package xyz.srclab.common.net.udp;

import xyz.srclab.common.net.FsNetServerException;

import java.nio.ByteBuffer;

/**
 * UDP network handler in server endpoint.
 *
 * @author fredsuvn
 */
public interface FsUdpServerHandler {

    /**
     * Callback when an exception occurs on the server.
     *
     * @param exception the exception for server
     */
    default void onException(FsNetServerException exception) {
    }

    /**
     * Callback when an exception occurs for the udp packet.
     *
     * @param header    header of the packet
     * @param throwable the exception
     * @param buffer    remaining buffer of the packet, readonly and initial position is 0
     */
    default void onException(FsUdpHeader header, Throwable throwable, ByteBuffer buffer) {
    }
}
