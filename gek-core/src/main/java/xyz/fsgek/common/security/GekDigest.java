package xyz.fsgek.common.security;

import xyz.fsgek.annotations.Nullable;
import xyz.fsgek.annotations.ThreadSafe;

import java.security.MessageDigest;
import java.security.Provider;

/**
 * Denotes cipher for message digestion, maybe has a back {@link MessageDigest}.
 * <p>
 * It is recommended to use method-chaining:
 * <pre>
 *     byte[] digestBytes = digest.prepare(bytes).bufferSize(size).digest().doFinal();
 * </pre>
 *
 * @author fredsuvn
 * @see MessageDigest
 */
@ThreadSafe
public interface GekDigest extends Prepareable {

    /**
     * Returns new instance of specified algorithm.
     * Returned instance has a back thread-local {@link MessageDigest}
     * which supplied with {@link MessageDigest#getInstance(String)}.
     *
     * @param algorithm specified algorithm
     * @return new instance of specified algorithm
     */
    static GekDigest getInstance(String algorithm) {
        return new DigestImpl(algorithm, () -> {
            try {
                return MessageDigest.getInstance(algorithm);
            } catch (Exception e) {
                throw new GekSecurityException(e);
            }
        });
    }

    /**
     * Returns new instance of specified algorithm and provider.
     * Returned instance has a back thread-local {@link MessageDigest}
     * which supplied with {@link MessageDigest#getInstance(String, String)}.
     *
     * @param algorithm specified algorithm
     * @param provider  specified provider
     * @return new instance of specified algorithm
     */
    static GekDigest getInstance(String algorithm, String provider) {
        return new DigestImpl(algorithm, () -> {
            try {
                return MessageDigest.getInstance(algorithm, provider);
            } catch (Exception e) {
                throw new GekSecurityException(e);
            }
        });
    }

    /**
     * Returns new instance of specified algorithm and provider.
     * Returned instance has a back thread-local
     * which supplied with {@link MessageDigest#getInstance(String, Provider)}.
     *
     * @param algorithm specified algorithm
     * @param provider  specified provider
     * @return new instance of specified algorithm
     */
    static GekDigest getInstance(String algorithm, Provider provider) {
        return new DigestImpl(algorithm, () -> {
            try {
                return MessageDigest.getInstance(algorithm, provider);
            } catch (Exception e) {
                throw new GekSecurityException(e);
            }
        });
    }

    /**
     * Returns back {@link MessageDigest} if it has, or null if it doesn't have one.
     * The back {@link MessageDigest} maybe thread-local, that is, returned value may be not only one instance.
     *
     * @return back {@link MessageDigest} if it has, or null if it doesn't have one
     */
    @Nullable
    MessageDigest getMessageDigest();

    /**
     * Returns digest length.
     *
     * @return digest length
     */
    int getDigestLength();
}
