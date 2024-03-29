package xyz.fsgek.common.io;

import xyz.fsgek.annotations.Nullable;
import xyz.fsgek.common.base.GekChars;
import xyz.fsgek.common.base.GekCheck;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Input/Output utilities.
 *
 * @author fresduvn
 */
public class GekIO {

    /**
     * Default IO buffer size: 1024 * 8 = 8192.
     */
    public static final int IO_BUFFER_SIZE = 1024 * 8;

    /**
     * Reads all bytes from given input stream.
     * Return null if no data read out and reach to the end of stream.
     *
     * @param inputStream given input stream
     * @return all bytes from given input stream
     */
    @Nullable
    public static byte[] readBytes(InputStream inputStream) {
        try {
            int available = inputStream.available();
            ByteArrayOutputStream dest;
            boolean hasRead = false;
            if (available > 0) {
                byte[] bytes = new byte[available];
                int c = inputStream.read(bytes);
                if (c == -1) {
                    return null;
                }
                if (c == available) {
                    int r = inputStream.read();
                    if (r == -1) {
                        return bytes;
                    } else {
                        dest = new ByteArrayOutputStream(available + 1);
                        dest.write(bytes);
                        dest.write(r);
                        hasRead = true;
                    }
                } else {
                    dest = new ByteArrayOutputStream(c);
                    dest.write(bytes, 0, c);
                    hasRead = true;
                }
            } else {
                dest = new ByteArrayOutputStream();
            }
            long readCount = readBytesTo(inputStream, dest);
            if (readCount == -1) {
                if (hasRead) {
                    return dest.toByteArray();
                }
                return null;
            }
            return dest.toByteArray();
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }

    /**
     * Reads specified limit number of bytes from given input stream.
     * Return null if no data read out and reach to the end of stream.
     * <p>
     * If the limit number &lt; 0, read all bytes;
     * els if limit number is 0, no read and return;
     * else this method will keep reading bytes until it reaches the limit or the end of the stream.
     *
     * @param inputStream given input stream
     * @param limit       specified limit number
     * @return specified limit number of bytes from given input stream
     */
    @Nullable
    public static byte[] readBytes(InputStream inputStream, int limit) {
        try {
            int available = inputStream.available();
            int hasRead = 0;
            ByteArrayOutputStream dest;
            if (available > 0) {
                if (available >= limit) {
                    byte[] bytes = new byte[limit];
                    int c = inputStream.read(bytes);
                    if (c == -1) {
                        return null;
                    }
                    if (c == limit) {
                        return bytes;
                    } else {
                        return Arrays.copyOf(bytes, c);
                    }
                } else {
                    byte[] bytes = new byte[available];
                    int c = inputStream.read(bytes);
                    if (c == -1) {
                        return null;
                    }
                    if (c == available) {
                        int r = inputStream.read();
                        if (r == -1) {
                            return bytes;
                        } else {
                            dest = new ByteArrayOutputStream(available + 1);
                            dest.write(bytes);
                            dest.write(r);
                            hasRead += (available + 1);
                        }
                    } else {
                        dest = new ByteArrayOutputStream(c);
                        dest.write(bytes, 0, c);
                        hasRead += c;
                    }
                }
            } else {
                dest = new ByteArrayOutputStream();
            }
            long readCount = limit == -1 ?
                readBytesTo(inputStream, dest, limit) : readBytesTo(inputStream, dest, limit - hasRead);
            if (readCount == -1) {
                if (hasRead > 0) {
                    return dest.toByteArray();
                }
                return null;
            }
            return dest.toByteArray();
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }

    /**
     * Reads all bytes from given input stream to given dest stream, returns actual read number.
     * Return -1 if no data read out and reach to the end of stream.
     *
     * @param inputStream given input stream
     * @param dest        given dest stream
     * @return actual read number
     */
    public static long readBytesTo(InputStream inputStream, OutputStream dest) {
        return readBytesTo(inputStream, dest, -1);
    }

    /**
     * Reads specified limit number of bytes from given input stream to given dest stream, returns actual read number.
     * Return -1 if no data read out and reach to the end of stream.
     * <p>
     * If the limit number &lt; 0, read all bytes;
     * els if limit number is 0, no read and return;
     * else this method will keep reading bytes until it reaches the limit or the end of the stream.
     *
     * @param inputStream given input stream
     * @param dest        given dest stream
     * @param limit       specified limit number
     * @return actual read number
     */
    public static long readBytesTo(InputStream inputStream, OutputStream dest, int limit) {
        return readBytesTo(inputStream, dest, limit, IO_BUFFER_SIZE);
    }

    /**
     * Reads specified limit number of bytes from given input stream to given dest stream, returns actual read number.
     * Return -1 if no data read out and reach to the end of stream.
     * <p>
     * If the limit number &lt; 0, read all bytes;
     * els if limit number is 0, no read and return;
     * else this method will keep reading bytes until it reaches the limit or the end of the stream.
     *
     * @param inputStream given input stream
     * @param dest        given dest stream
     * @param limit       specified limit number
     * @param bufferSize  buffer size for each reading and writing
     * @return actual read number
     */
    public static long readBytesTo(InputStream inputStream, OutputStream dest, int limit, int bufferSize) {
        if (limit == 0) {
            return 0;
        }
        try {
            long readNum = 0;
            int actualBufferSize = limit < 0 ? bufferSize : Math.min(limit, bufferSize);
            byte[] buffer = new byte[actualBufferSize];
            while (true) {
                int readLen = limit < 0 ? buffer.length : (int) Math.min(limit - readNum, buffer.length);
                int readSize = inputStream.read(buffer, 0, readLen);
                if (readSize < 0) {
                    if (readNum == 0) {
                        return -1;
                    }
                    break;
                }
                if (readSize > 0) {
                    dest.write(buffer, 0, readSize);
                    readNum += readSize;
                }
                if (limit < 0) {
                    continue;
                }
                long remaining = limit - readNum;
                if (remaining <= 0) {
                    break;
                }
            }
            return readNum;
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }

    /**
     * Reads available bytes from given input stream.
     * Return null if no data read out and reach to the end of stream.
     *
     * @param inputStream given input stream
     * @return available bytes from given input stream
     */
    @Nullable
    public static byte[] availableBytes(InputStream inputStream) {
        try {
            int available = inputStream.available();
            if (available > 0) {
                byte[] bytes = new byte[available];
                int c = inputStream.read(bytes);
                if (c == -1) {
                    return null;
                }
                if (c == available) {
                    return bytes;
                }
                return Arrays.copyOf(bytes, c);
            }
            if (available == 0) {
                byte[] b = new byte[1];
                int readSize = inputStream.read(b);
                if (readSize == -1) {
                    return null;
                }
                if (readSize == 0) {
                    return new byte[0];
                }
                return b;
            }
            ByteArrayOutputStream dest = new ByteArrayOutputStream();
            long readCount = availableBytesTo(inputStream, dest);
            if (readCount == -1) {
                return null;
            }
            return dest.toByteArray();
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }

    /**
     * Reads available bytes from given input stream to given dest stream,
     * returns actual read number.
     * Return -1 if no data read out and reach to the end of stream.
     *
     * @param inputStream given input stream
     * @param dest        given dest stream
     * @return actual read number
     */
    public static long availableBytesTo(InputStream inputStream, OutputStream dest) {
        try {
            int available = inputStream.available();
            if (available < 0) {
                return -1;
            }
            if (available == 0) {
                byte[] b = new byte[1];
                int readSize = inputStream.read(b);
                if (readSize == -1) {
                    return -1;
                }
                if (readSize == 1) {
                    dest.write(b);
                    return 1;
                }
                return 0;
            }
            return readBytesTo(inputStream, dest, available);
        } catch (IOException e) {
            throw new GekIOException(e);
        }
    }

    /**
     * Reads all chars as string from given reader.
     * Return null if no data read out and reach to the end of stream.
     *
     * @param reader given reader
     * @return all chars as string from given reader
     */
    @Nullable
    public static String readString(Reader reader) {
        try {
            StringBuilder dest = new StringBuilder();
            long readCount = readCharsTo(reader, dest);
            if (readCount == -1) {
                return null;
            }
            return dest.toString();
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }

    /**
     * Reads specified limit number of chars as string from given reader.
     * Return null if no data read out and reach to the end of stream.
     * <p>
     * If the limit number &lt; 0, read all bytes;
     * els if limit number is 0, no read and return;
     * else this method will keep reading chars until it reaches the limit or the end of the reader.
     *
     * @param reader given reader
     * @param limit  specified limit number
     * @return specified limit number of chars as string from given reader
     */
    @Nullable
    public static String readString(Reader reader, int limit) {
        try {
            StringBuilder dest = new StringBuilder();
            long readCount = readCharsTo(reader, dest, limit);
            if (readCount == -1) {
                return null;
            }
            return dest.toString();
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }

    /**
     * Reads all chars from given reader to given dest output, returns actual read number.
     * Return -1 if no data read out and reach to the end of stream.
     * <p>
     * If the limit number &lt; 0, read all chars;
     * els if limit number is 0, no read and return;
     * else this method will keep reading chars until it reaches the limit or the end of the reader.
     *
     * @param reader given reader
     * @param dest   given dest stream
     * @return actual read number
     */
    public static long readCharsTo(Reader reader, Appendable dest) {
        return readCharsTo(reader, dest, -1);
    }

    /**
     * Reads specified limit number of chars from given reader to given dest output, returns actual read number.
     * Return -1 if no data read out and reach to the end of stream.
     * <p>
     * If the limit number &lt; 0, read all chars;
     * els if limit number is 0, no read and return;
     * else this method will keep reading chars until it reaches the limit or the end of the reader.
     *
     * @param reader given reader
     * @param dest   given dest stream
     * @param limit  specified limit number
     * @return actual read number
     */
    public static long readCharsTo(Reader reader, Appendable dest, int limit) {
        return readCharsTo(reader, dest, limit, IO_BUFFER_SIZE);
    }

    /**
     * Reads specified limit number of chars from given reader to given dest output, returns actual read number.
     * Return -1 if no data read out and reach to the end of stream.
     * <p>
     * If the limit number &lt; 0, read all chars;
     * els if limit number is 0, no read and return;
     * else this method will keep reading chars until it reaches the limit or the end of the reader.
     *
     * @param reader     given reader
     * @param dest       given dest stream
     * @param limit      specified limit number
     * @param bufferSize buffer size for each reading and writing
     * @return actual read number
     */
    public static long readCharsTo(Reader reader, Appendable dest, int limit, int bufferSize) {
        if (limit == 0) {
            return 0;
        }
        try {
            long readNum = 0;
            int actualBufferSize = limit < 0 ? bufferSize : Math.min(limit, bufferSize);
            char[] buffer = new char[actualBufferSize];
            while (true) {
                int readLen = limit < 0 ? buffer.length : (int) Math.min(limit - readNum, buffer.length);
                int readSize = reader.read(buffer, 0, readLen);
                if (readSize < 0) {
                    if (readNum == 0) {
                        return -1;
                    }
                    break;
                }
                if (readSize > 0) {
                    append(dest, buffer, 0, readSize);
                    readNum += readSize;
                }
                if (limit < 0) {
                    continue;
                }
                long remaining = limit - readNum;
                if (remaining <= 0) {
                    break;
                }
            }
            return readNum;
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }

    private static void append(Appendable dest, char[] chars, int off, int len) throws IOException {
        if (dest instanceof StringBuilder) {
            ((StringBuilder) dest).append(chars, off, len);
        } else if (dest instanceof StringBuffer) {
            ((StringBuffer) dest).append(chars, off, len);
        } else if (dest instanceof Writer) {
            ((Writer) dest).write(chars, off, len);
        } else {
            dest.append(new String(chars, off, len));
        }
    }

    /**
     * Reads string encoded by all bytes from given input stream with {@link GekChars#defaultCharset()}.
     * Return null if no data read out and reach to the end of stream.
     *
     * @param inputStream given input stream
     * @return string encoded by all bytes from given input stream with {@link GekChars#defaultCharset()}
     */
    @Nullable
    public static String readString(InputStream inputStream) {
        return readString(inputStream, GekChars.defaultCharset());
    }

    /**
     * Reads string encoded by all bytes from given input stream.
     * Return null if no data read out and reach to the end of stream.
     *
     * @param inputStream given input stream
     * @param charset     charset of the string
     * @return string encoded by all bytes from given input stream
     */
    @Nullable
    public static String readString(InputStream inputStream, Charset charset) {
        byte[] bytes = readBytes(inputStream);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, charset);
    }

    /**
     * Reads available string encoded by all bytes from given input stream with
     * {@link GekChars#defaultCharset()}, returns null if reaches end of the stream.
     * Return null if no data read out and reach to the end of stream.
     *
     * @param inputStream given input stream
     * @return available string
     */
    @Nullable
    public static String avalaibleString(InputStream inputStream) {
        return avalaibleString(inputStream, GekChars.defaultCharset());
    }

    /**
     * Reads available string encoded by all bytes from given input stream,
     * Return null if no data read out and reach to the end of stream.
     *
     * @param inputStream given input stream
     * @param charset     charset of the string
     * @return available string
     */
    @Nullable
    public static String avalaibleString(InputStream inputStream, Charset charset) {
        byte[] bytes = availableBytes(inputStream);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, charset);
    }

    /**
     * Wraps given input stream as a reader with {@link InputStreamReader} and {@link GekChars#defaultCharset()}.
     *
     * @param inputStream given input stream
     * @return wrapped reader
     */
    public static Reader toReader(InputStream inputStream) {
        return toReader(inputStream, GekChars.defaultCharset());
    }

    /**
     * Wraps given input stream as a reader with {@link InputStreamReader}.
     *
     * @param inputStream given input stream
     * @param charset     charset of return reader
     * @return wrapped reader
     */
    public static Reader toReader(InputStream inputStream, Charset charset) {
        return new InputStreamReader(inputStream, charset);
    }

    /**
     * Wraps given buffer as a reader, supports mark/reset.
     *
     * @param buffer given buffer
     * @return wrapped reader
     */
    public static Reader toReader(CharBuffer buffer) {
        return new CharBufferReader(buffer);
    }

    /**
     * Wraps given reader as an input stream with {@link GekChars#defaultCharset()}, doesn't support mark/reset.
     *
     * @param reader given reader
     * @return wrapped stream
     */
    public static InputStream toInputStream(Reader reader) {
        return toInputStream(reader, GekChars.defaultCharset());
    }

    /**
     * Wraps given reader as an input stream, doesn't support mark/reset.
     *
     * @param reader  given reader
     * @param charset charset of given reader
     * @return wrapped stream
     */
    public static InputStream toInputStream(Reader reader, Charset charset) {
        return new ReaderInputStream(reader, charset);
    }

    /**
     * Wraps given array to {@link ByteArrayInputStream}.
     *
     * @param array given array
     * @return wrapped stream
     */
    public static ByteArrayInputStream toInputStream(byte[] array) {
        return new ByteArrayInputStream(array);
    }

    /**
     * Wraps given array to {@link ByteArrayInputStream}, the wrapped range of specified length start from given offset.
     *
     * @param array  given array
     * @param offset given offset
     * @param length specified length
     * @return wrapped stream
     */
    public static ByteArrayInputStream toInputStream(byte[] array, int offset, int length) {
        GekCheck.checkRangeInBounds(offset, offset + length, 0, array.length);
        return new ByteArrayInputStream(array, offset, length);
    }

    /**
     * Wraps given buffer as an input stream, supports mark/reset.
     *
     * @param buffer given buffer
     * @return wrapped stream
     */
    public static InputStream toInputStream(ByteBuffer buffer) {
        return new ByteBufferInputStream(buffer);
    }

    /**
     * Wraps given random access file as an input stream, supports mark/reset.
     * <p>
     * Note this method will seek position of random access file to given offset immediately.
     *
     * @param random given random access file
     * @return wrapped stream
     */
    public static InputStream toInputStream(RandomAccessFile random) {
        return toInputStream(random, 0, -1);
    }

    /**
     * Wraps given random access file as an input stream,
     * readable bytes from {@code offset} position to {@code (offset + length)},
     * supports mark/reset.
     * {@code length} can be set to -1 if to read to end of the file.
     * <p>
     * Note this method will seek position of random access file to given offset immediately.
     *
     * @param random given random access file
     * @param offset offset position to start read
     * @param length length of readable bytes, or -1 to read to end of file
     * @return wrapped stream
     */
    public static InputStream toInputStream(RandomAccessFile random, long offset, long length) {
        return new RandomInputStream(random, offset, length);
    }

    /**
     * Wraps given output stream as a writer with {@link OutputStreamWriter} and {@link GekChars#defaultCharset()}.
     *
     * @param outputStream given out stream
     * @return wrapped writer
     */
    public static Writer toWriter(OutputStream outputStream) {
        return toWriter(outputStream, GekChars.defaultCharset());
    }

    /**
     * Wraps given output stream as a writer with {@link OutputStreamWriter}.
     *
     * @param outputStream given out stream
     * @param charset      charset writer
     * @return wrapped writer
     */
    public static Writer toWriter(OutputStream outputStream, Charset charset) {
        return new OutputStreamWriter(outputStream, charset);
    }

    /**
     * Wraps given buffer as a writer.
     *
     * @param buffer given buffer
     * @return wrapped writer
     */
    public static Writer toWriter(CharBuffer buffer) {
        return new CharBufferWriter(buffer);
    }

    /**
     * Wraps given appendable as an output stream with {@link GekChars#defaultCharset()}.
     * <p>
     * Note {@link OutputStream#flush()} and {@link OutputStream#close()} are valid for given {@code appendable}
     * if it is instance of {@link Writer}.
     *
     * @param appendable given appendable
     * @return wrapped stream
     */
    public static OutputStream toOutputStream(Appendable appendable) {
        return toOutputStream(appendable, GekChars.defaultCharset());
    }

    /**
     * Wraps given appendable as an output stream.
     * <p>
     * Note {@link OutputStream#flush()} and {@link OutputStream#close()} are valid for given {@code appendable}
     * if it is instance of {@link Writer}.
     *
     * @param appendable given appendable
     * @param charset    charset of writer
     * @return wrapped stream
     */
    public static OutputStream toOutputStream(Appendable appendable, Charset charset) {
        return new AppendableOutputStream(appendable, charset);
    }

    /**
     * Wraps given array as an output stream.
     *
     * @param array given array
     * @return wrapped stream
     */
    public static OutputStream toOutputStream(byte[] array) {
        return new ByteArrayAsOutputStream(array, 0, array.length);
    }

    /**
     * Wraps given array as an output stream, the wrapped range of specified length start from given offset.
     *
     * @param array  given array
     * @param offset given offset
     * @param length specified length
     * @return wrapped stream
     */
    public static OutputStream toOutputStream(byte[] array, int offset, int length) {
        GekCheck.checkRangeInBounds(offset, offset + length, 0, array.length);
        return new ByteArrayAsOutputStream(array, offset, length);
    }

    /**
     * Wraps given buffer as an output stream.
     *
     * @param buffer given writer
     * @return wrapped stream
     */
    public static OutputStream toOutputStream(ByteBuffer buffer) {
        return new ByteBufferOutputStream(buffer);
    }

    /**
     * Wraps given random access file as an output stream.
     * The stream will lock the file with exclusive lock by {@link FileChannel#tryLock(long, long, boolean)}.
     * <p>
     * Note this method will seek position of random access file to given offset immediately.
     *
     * @param random given random access file
     * @return wrapped stream
     */
    public static OutputStream toOutputStream(RandomAccessFile random) {
        return toOutputStream(random, 0, -1);
    }

    /**
     * Wraps given random access file as an output stream,
     * written bytes from {@code offset} position to {@code (offset + length)}.
     * {@code length} can be set to -1 if to write unlimitedly.
     * The stream will lock the file with exclusive lock by {@link FileChannel#tryLock(long, long, boolean)}.
     * <p>
     * Note this method will seek position of random access file to given offset immediately.
     *
     * @param random given random access file
     * @param offset offset position to start write
     * @param length length of written bytes, or -1 to write unlimitedly
     * @return wrapped stream
     */
    public static OutputStream toOutputStream(RandomAccessFile random, long offset, long length) {
        return new RandomOutputStream(random, offset, length);
    }

    /**
     * Wraps given source stream to limit read length.
     * Note returned stream doesn't support mark/reset.
     *
     * @param source given source stream
     * @param limit  max read length, must &gt;= 0
     * @return wrapped stream
     */
    public static InputStream limited(InputStream source, long limit) {
        return new LimitedInputStream(source, limit);
    }

    /**
     * Wraps given source stream to limit written length.
     *
     * @param source given source stream
     * @param limit  max written length, must &gt;= 0
     * @return wrapped stream
     */
    public static OutputStream limited(OutputStream source, long limit) {
        return new LimitedOutputStream(source, limit);
    }

    /**
     * Using {@link RandomAccessFile} to read all bytes of given file of path.
     *
     * @param path given file of path
     * @return read bytes
     */
    public static byte[] readBytes(Path path) {
        return readBytes(path, 0, -1);
    }

    /**
     * Using {@link RandomAccessFile} to read given length bytes of given file of path from offset position,
     * the given length may be set to -1 to read to end of file.
     *
     * @param path   given file of path
     * @param offset offset position
     * @param length given length, maybe -1 to read to end of file
     * @return read bytes
     */
    public static byte[] readBytes(Path path, long offset, long length) {
        try (RandomAccessFile random = new RandomAccessFile(path.toFile(), "r")) {
            return GekIO.readBytes(GekIO.toInputStream(random, offset, length));
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }

    /**
     * Using {@link RandomAccessFile} to read all bytes of given file of path.
     * The read bytes will be encoded to String with {@link GekChars#defaultCharset()}.
     *
     * @param path given file of path
     * @return read string
     */
    public static String readString(Path path) {
        return readString(path, GekChars.defaultCharset());
    }

    /**
     * Using {@link RandomAccessFile} to read all bytes of given file of path.
     * The read bytes will be encoded to String with given charset.
     *
     * @param path    given file of path
     * @param charset given charset
     * @return read string
     */
    public static String readString(Path path, Charset charset) {
        return readString(path, 0, -1, charset);
    }

    /**
     * Using {@link RandomAccessFile} to read given length bytes of given file of path from offset position,
     * the given length may be set to -1 to read to end of file.
     * The read bytes will be encoded to String with {@link GekChars#defaultCharset()}.
     *
     * @param path   given file of path
     * @param offset offset position
     * @param length given length, maybe -1 to read to end of file
     * @return read string
     */
    public static String readString(Path path, long offset, long length) {
        return readString(path, offset, length, GekChars.defaultCharset());
    }

    /**
     * Using {@link RandomAccessFile} to read given length bytes of given file of path from offset position,
     * the given length may be set to -1 to read to end of file.
     * The read bytes will be encoded to String with given charset.
     *
     * @param path    given file of path
     * @param offset  offset position
     * @param length  given length, maybe -1 to read to end of file
     * @param charset given charset
     * @return read string
     */
    public static String readString(Path path, long offset, long length, Charset charset) {
        try (RandomAccessFile random = new RandomAccessFile(path.toFile(), "r")) {
            return GekIO.readString(GekIO.toInputStream(random, offset, length), charset);
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }

    /**
     * Using {@link RandomAccessFile} to write  bytes into given file of path.
     *
     * @param path given file of path
     * @param data given data
     */
    public static void writeBytes(Path path, InputStream data) {
        writeBytes(path, 0, -1, data);
    }

    /**
     * Using {@link RandomAccessFile} to write given length bytes into given file of path from offset position,
     * the given length may be set to -1 to write unlimitedly.
     *
     * @param path   given file of path
     * @param offset offset position
     * @param length given length, maybe -1 to write unlimitedly
     * @param data   given data
     */
    public static void writeBytes(Path path, long offset, long length, InputStream data) {
        try (RandomAccessFile random = new RandomAccessFile(path.toFile(), "rw")) {
            OutputStream dest = GekIO.toOutputStream(random, offset, length);
            GekIO.readBytesTo(data, dest);
            dest.flush();
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }

    /**
     * Using {@link RandomAccessFile} to write given data into given file.
     * The written bytes will be decoded from given data with {@link GekChars#defaultCharset()}.
     *
     * @param path given file of path
     * @param data given data
     */
    public static void writeString(Path path, CharSequence data) {
        writeString(path, data, GekChars.defaultCharset());
    }

    /**
     * Using {@link RandomAccessFile} to write given data into given file.
     * The written bytes will be decoded from given data with given charset.
     *
     * @param path    given file of path
     * @param data    given data
     * @param charset given charset
     */
    public static void writeString(Path path, CharSequence data, Charset charset) {
        writeString(path, 0, -1, data, charset);
    }

    /**
     * Using {@link RandomAccessFile} to write given data into given file of path from offset position,
     * the given length may be set to -1 to write unlimitedly.
     * The written bytes will be decoded from given data with {@link GekChars#defaultCharset()}.
     *
     * @param path   given file of path
     * @param offset offset position
     * @param length given length, maybe -1 to write unlimitedly
     * @param data   given data
     */
    public static void writeString(Path path, long offset, long length, CharSequence data) {
        writeString(path, offset, length, data, GekChars.defaultCharset());
    }

    /**
     * Using {@link RandomAccessFile} to write given data into given file of path from offset position,
     * the given length may be set to -1 to write unlimitedly.
     * The written bytes will be decoded from given data with given charset.
     *
     * @param path    given file of path
     * @param offset  offset position
     * @param length  given length, maybe -1 to write unlimitedly
     * @param data    given data
     * @param charset given charset
     */
    public static void writeString(Path path, long offset, long length, CharSequence data, Charset charset) {
        try (RandomAccessFile random = new RandomAccessFile(path.toFile(), "rw")) {
            Writer writer = GekIO.toWriter(GekIO.toOutputStream(random, offset, length), charset);
            writer.append(data);
            writer.flush();
        } catch (Exception e) {
            throw new GekIOException(e);
        }
    }
}
