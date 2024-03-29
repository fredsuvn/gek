package xyz.fsgek.common.io;

import xyz.fsgek.annotations.ThreadSafe;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * Interface to operate file, thread-safe.
 *
 * @author fresduvn
 */
@ThreadSafe
public interface GekFile {

    /**
     * Creates a {@link GekFile} from given path.
     * <p>
     * This method uses {@link RandomAccessFile} to implement {@link GekFile} interface,
     * and will use {@link FileChannel#tryLock(long, long, boolean)} to lock written part when writing file.
     *
     * @param path given path
     * @return a {@link GekFile} from given path
     */
    static GekFile from(Path path) {
        return new FileImpl(path);
    }

    /**
     * Returns path of file.
     *
     * @return path of file
     */
    Path getPath();

    /**
     * Returns file object.
     *
     * @return file object
     */
    default File getFile() {
        return getPath().toFile();
    }

    /**
     * Returns whether current file is opened.
     *
     * @return whether current file is opened
     */
    boolean isOpened();

    /**
     * Opens current file with given mode,
     * the mode including "r", "rw", "rws", or "rwd", is same with {@link RandomAccessFile}.
     * <p>
     * This method will restart the stream from {@link #bindInputStream()} and {@link #bindOutputStream()}.
     *
     * @param mode given mode
     */
    void open(String mode);

    /**
     * Closes current file.
     * <p>
     * This method will close the stream from {@link #bindInputStream()} and {@link #bindOutputStream()}.
     */
    void close();

    /**
     * Returns position of current file pointer.
     *
     * @return position of current file pointer
     */
    long position();

    /**
     * Sets position of current file pointer. If the new position over the length of file, the file will be extended.
     * <p>
     * This method will reset position for the stream from {@link #bindInputStream()} and {@link #bindOutputStream()}.
     *
     * @param pos position of file pointer
     */
    void position(long pos);

    /**
     * Returns file length.
     *
     * @return file length
     */
    long length();

    /**
     * Sets new file length, truncated or extended.
     *
     * @param newLength new length
     */
    void setFileLength(long newLength);

    /**
     * Returns channel of file.
     *
     * @return channel of file
     */
    FileChannel getChannel();

    /**
     * Returns descriptor of file.
     *
     * @return descriptor of file
     */
    FileDescriptor getDescriptor();

    /**
     * Force all system buffers to synchronize with the underlying device, equivalent to {@link FileDescriptor#sync()}.
     */
    default void sync() {
        try {
            getDescriptor().sync();
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }

    /**
     * Returns an input stream for current file, the read position is {@link #position()}.
     * Read operations of stream will move the position of file,
     * and if the position is reset by {@link #position(long)}, the stream's read position will also be reset.
     * <p>
     * Returned stream doesn't support {@link InputStream#mark(int)} and {@link InputStream#reset()},
     * and {@link InputStream#close()} is also invalid. Using {@link #close()  GekFile.close} to close both binding
     * input/output stream.
     *
     * @return an input stream for current file
     */
    InputStream bindInputStream();

    /**
     * Returns an output stream for current file, the written position is {@link #position()}.
     * Write operations of stream will move the position of file,
     * and if the position is reset by {@link #position(long)}, the stream's written position will also be reset.
     * If the written data over the length of file, the file will be auto extended.
     * <p>
     * {@link InputStream#close()} is invalid, using {@link #close()  GekFile.close} to close both binding
     * input/output stream.
     *
     * @return an output stream for current file
     */
    OutputStream bindOutputStream();
}
