package xyz.fsgek.common.security;

import xyz.fsgek.annotations.Nullable;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.util.function.Supplier;

final class KeyFactoryImpl implements GekKeyFactory {

    private final String algorithm;
    private final ThreadLocal<KeyFactory> local;

    KeyFactoryImpl(String algorithm, Supplier<KeyFactory> supplier) {
        this.algorithm = algorithm;
        this.local = ThreadLocal.withInitial(supplier);
    }

    @Override
    public @Nullable KeyFactory getKeyFactory() {
        return local.get();
    }

    @Override
    public PublicKey generatePublic(KeySpec spec) {
        try {
            KeyFactory factory = local.get();
            return factory.generatePublic(spec);
        } catch (GekSecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new GekSecurityException(e);
        }
    }

    @Override
    public PrivateKey generatePrivate(KeySpec spec) {
        try {
            KeyFactory factory = local.get();
            return factory.generatePrivate(spec);
        } catch (GekSecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new GekSecurityException(e);
        }
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }
}
