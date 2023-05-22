package xyz.srclab.common.io;

import xyz.srclab.common.base.FsCheck;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

final class ByteBufferInputStream extends InputStream {

    private final ByteBuffer buffer;

    ByteBufferInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read(byte[] array, int off, int len) throws IOException {
        FsCheck.checkRangeInBounds(0, array.length, off, off + len);
        if (len == 0) {
            return 0;
        }
        try {
            int actualLength = Math.min(buffer.remaining(), len);
            if (actualLength <= 0) {
                return -1;
            }
            buffer.get(array, off, actualLength);
            return actualLength;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read() throws IOException {
        try {
            if (buffer.remaining() <= 0) {
                return -1;
            }
            return buffer.get() & 0xFF;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws IOException {
    }
}