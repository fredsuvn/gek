package xyz.fsgek.common.security;

import xyz.fsgek.annotations.Nullable;
import xyz.fsgek.common.io.GekIO;
import xyz.fsgek.common.base.GekCheck;

import javax.crypto.Mac;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.function.Supplier;

final class MacImpl implements GekMac {

    private final String algorithm;
    private final ThreadLocal<Mac> local;

    MacImpl(String algorithm, Supplier<Mac> supplier) {
        this.algorithm = algorithm;
        this.local = ThreadLocal.withInitial(supplier);
    }

    @Override
    public @Nullable Mac getMac() {
        return local.get();
    }

    @Override
    public int getMacLength() {
        return local.get().getMacLength();
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
                Mac mac = local.get();
                return GekCrypto.generateMac(mac, key, src, params);
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
                Mac mac = local.get();
                return GekCrypto.generateMac(mac, key, source, params);
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
                Mac mac = local.get();
                return GekCrypto.generateMac(mac, key, in, bufferSize, params);
            } catch (GekSecurityException e) {
                throw e;
            } catch (Exception e) {
                throw new GekSecurityException(e);
            }
        }
    }

    private abstract class AbstractCryptoProcess implements CryptoProcess {

        protected AlgorithmParams params;
        protected Key key;
        protected int bufferSize;

        @Override
        public CryptoProcess key(Key key) {
            this.key = key;
            return this;
        }

        @Override
        public CryptoProcess algorithmParameterSpec(AlgorithmParameterSpec parameterSpec) {
            getParams().setAlgorithmParameterSpec(parameterSpec);
            return this;
        }

        @Override
        public CryptoProcess algorithmParameters(AlgorithmParameters parameters) {
            getParams().setAlgorithmParameters(parameters);
            return this;
        }

        @Override
        public CryptoProcess secureRandom(SecureRandom secureRandom) {
            getParams().setSecureRandom(secureRandom);
            return this;
        }

        @Override
        public CryptoProcess certificate(Certificate certificate) {
            getParams().setCertificate(certificate);
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
        public CryptoProcess mac() {
            return this;
        }

        protected AlgorithmParams getParams() {
            if (params != null) {
                return params;
            }
            AlgorithmParams newParams = new AlgorithmParams();
            params = newParams;
            return newParams;
        }

        @Override
        public int doFinal(byte[] dest, int offset) {
            try {
                if (dest.length - offset < getMacLength()) {
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
                if (dest.remaining() < getMacLength()) {
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
