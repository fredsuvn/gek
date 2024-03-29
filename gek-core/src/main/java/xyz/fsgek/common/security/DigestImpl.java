package xyz.fsgek.common.security;

import xyz.fsgek.annotations.Nullable;
import xyz.fsgek.common.io.GekIO;
import xyz.fsgek.common.base.GekCheck;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.function.Supplier;

final class DigestImpl implements GekDigest {

    private final String algorithm;
    private final ThreadLocal<MessageDigest> local;

    DigestImpl(String algorithm, Supplier<MessageDigest> supplier) {
        this.algorithm = algorithm;
        this.local = ThreadLocal.withInitial(supplier);
    }

    @Override
    public @Nullable MessageDigest getMessageDigest() {
        return local.get();
    }

    @Override
    public int getDigestLength() {
        return local.get().getDigestLength();
    }

    @Override
    public CryptoProcess prepare(byte[] source, int offset, int length) {
        GekCheck.checkRangeInBounds(offset, offset + length, 0, source.length);
        return new ByteArrayCryptoProcess(source, offset, length);
    }

    @Override
    public CryptoProcess prepare(ByteBuffer source) {
        return new BufferCryptoProcess(source);
    }

    @Override
    public CryptoProcess prepare(InputStream source) {
        return new StreamCryptoProcess(source);
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    private final class ByteArrayCryptoProcess extends AbstractCryptoProcess {

        private final byte[] source;
        private final int offset;
        private final int length;

        private ByteArrayCryptoProcess(byte[] source, int offset, int length) {
            this.source = source;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public byte[] doFinal() {
            try {
                ByteBuffer src = ByteBuffer.wrap(source, this.offset, this.length);
                MessageDigest digest = local.get();
                return GekCrypto.digest(digest, src);
            } catch (GekSecurityException e) {
                throw e;
            } catch (Exception e) {
                throw new GekSecurityException(e);
            }
        }
    }

    private final class BufferCryptoProcess extends AbstractCryptoProcess {

        private final ByteBuffer source;

        private BufferCryptoProcess(ByteBuffer source) {
            this.source = source;
        }

        @Override
        public byte[] doFinal() {
            try {
                MessageDigest digest = local.get();
                return GekCrypto.digest(digest, source);
            } catch (GekSecurityException e) {
                throw e;
            } catch (Exception e) {
                throw new GekSecurityException(e);
            }
        }
    }

    private final class StreamCryptoProcess extends AbstractCryptoProcess {

        private final InputStream in;

        private StreamCryptoProcess(InputStream in) {
            this.in = in;
        }

        @Override
        public byte[] doFinal() {
            try {
                MessageDigest digest = local.get();
                return GekCrypto.digest(digest, in, bufferSize);
            } catch (GekSecurityException e) {
                throw e;
            } catch (Exception e) {
                throw new GekSecurityException(e);
            }
        }
    }

    private abstract class AbstractCryptoProcess implements CryptoProcess {

        protected int bufferSize;

        @Override
        public CryptoProcess key(Key key) {
            return this;
        }

        @Override
        public CryptoProcess algorithmParameterSpec(AlgorithmParameterSpec parameterSpec) {
            return this;
        }

        @Override
        public CryptoProcess algorithmParameters(AlgorithmParameters parameters) {
            return this;
        }

        @Override
        public CryptoProcess secureRandom(SecureRandom secureRandom) {
            return this;
        }

        @Override
        public CryptoProcess certificate(Certificate certificate) {
            return this;
        }

        @Override
        public CryptoProcess keySize(int keySize) {
            return this;
        }

        @Override
        public CryptoProcess blockSize(int blockSize) {
            return this;
        }

        @Override
        public CryptoProcess bufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }

        @Override
        public CryptoProcess digest() {
            return this;
        }

        @Override
        public int doFinal(byte[] dest, int offset) {
            try {
                if (dest.length - offset < getDigestLength()) {
                    throw new GekSecurityException("length of dest remaining is not enough.");
                }
                byte[] en = doFinal();
                System.arraycopy(en, 0, dest, offset, en.length);
                return en.length;
            } catch (GekSecurityException e) {
                throw e;
            } catch (Exception e) {
                throw new GekSecurityException(e);
            }
        }

        @Override
        public int doFinal(ByteBuffer dest) {
            try {
                if (dest.remaining() < getDigestLength()) {
                    throw new GekSecurityException("length of dest remaining is not enough.");
                }
                byte[] en = doFinal();
                dest.put(en);
                return en.length;
            } catch (GekSecurityException e) {
                throw e;
            } catch (Exception e) {
                throw new GekSecurityException(e);
            }
        }

        @Override
        public long doFinal(OutputStream dest) {
            try {
                byte[] en = doFinal();
                dest.write(en);
                return en.length;
            } catch (GekSecurityException e) {
                throw e;
            } catch (Exception e) {
                throw new GekSecurityException(e);
            }
        }

        @Override
        public InputStream doFinalStream() {
            try {
                byte[] en = doFinal();
                return GekIO.toInputStream(en);
            } catch (GekSecurityException e) {
                throw e;
            } catch (Exception e) {
                throw new GekSecurityException(e);
            }
        }
    }
}
