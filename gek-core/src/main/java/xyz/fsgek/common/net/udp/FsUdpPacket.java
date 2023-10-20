package xyz.fsgek.common.net.udp;

import xyz.fsgek.common.io.FsBuffer;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Represents UDP datagram packet, usually based on {@link DatagramPacket}.
 *
 * @author fredsuvn
 */
public interface FsUdpPacket {

    /**
     * Returns UDP packet of which content comes from given datagram packet.
     *
     * @param packet given packet
     * @return UDP packet
     */
    static FsUdpPacket from(DatagramPacket packet) {
        byte[] data = Arrays.copyOfRange(
            packet.getData(), packet.getOffset(), packet.getOffset() + packet.getLength());
        ByteBuffer buffer = ByteBuffer.wrap(data);
        FsUdpHeader header = FsUdpHeader.from(packet);
        return new FsUdpPacket() {

            @Override
            public FsUdpHeader getHeader() {
                return header;
            }

            @Override
            public ByteBuffer getData() {
                return buffer.asReadOnlyBuffer();
            }
        };
    }

    /**
     * Returns UDP header of given buffer and address.
     * The given buffer will be read out by this method.
     *
     * @param buffer  given buffer
     * @param address given address
     * @return UDP packet
     */
    static FsUdpPacket of(ByteBuffer buffer, InetSocketAddress address) {
        ByteBuffer data = ByteBuffer.wrap(FsBuffer.getBytes(buffer));
        return new FsUdpPacket() {

            @Override
            public FsUdpHeader getHeader() {
                return FsUdpHeader.of(address);
            }

            @Override
            public ByteBuffer getData() {
                return data.asReadOnlyBuffer();
            }
        };
    }

    /**
     * Returns header info of this datagram packet.
     *
     * @return header info of this datagram packet
     */
    FsUdpHeader getHeader();

    /**
     * Returns readonly data of this datagram packet.
     *
     * @return readonly data of this datagram packet
     */
    ByteBuffer getData();
}