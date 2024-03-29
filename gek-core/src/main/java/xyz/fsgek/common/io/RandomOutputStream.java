package xyz.fsgek.common.io;

import xyz.fsgek.common.base.GekCheck;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

final class RandomOutputStream extends OutputStream {

    private final RandomAccessFile random;
    private final long limit;
    private long pos;

    RandomOutputStream(RandomAccessFile random, long offset, long length) {
        try {
            if (length != -1) {
                GekCheck.checkArgument(offset >= 0 && length >= 0, "offset and length must >= 0.");
                this.limit = offset + length;
            } else {
                this.limit = length;
            }
            this.random = random;
            this.pos = offset;
            this.random.seek(pos);
        } catch (IOException e) {
            throw new GekIOException(e);
        }
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        try {
            GekCheck.checkRangeInBounds(off, off + len, 0, b.length);
            if (len == 0) {
                return;
            }
            if (limit != -1) {
                GekCheck.checkInBounds(pos + len - 1, pos, limit);
            }
            FileLock lock = random.getChannel().tryLock(pos, len, false);
            if (lock == null || !lock.isValid()) {
                throw new IOException("Failed to lock file at " + pos + ", len: " + len + ".");
            }
            try {
                random.write(b, off, len);
            } finally {
                lock.close();
            }
            pos += len;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public synchronized void write(int b) throws IOException {
        try {
            if (limit != -1) {
                GekCheck.checkInBounds(pos, pos, limit);
            }
            FileLock lock = random.getChannel().tryLock(pos, 1, false);
            if (lock == null || !lock.isValid()) {
                throw new IOException("Failed to lock file at " + pos + ", len: 1.");
            }
            try {
                random.write(b);
            } finally {
                lock.close();
            }
            pos += 1;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public synchronized void flush() throws IOException {
        random.getFD().sync();
    }

    @Override
    public synchronized void close() throws IOException {
        random.close();
    }
}