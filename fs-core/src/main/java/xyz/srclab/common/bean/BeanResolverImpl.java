package xyz.srclab.common.bean;

import xyz.srclab.annotations.Nullable;
import xyz.srclab.common.base.Fs;
import xyz.srclab.common.bean.handlers.JavaBeanResolveHandler;
import xyz.srclab.common.cache.FsCache;
import xyz.srclab.common.collect.FsCollect;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

final class BeanResolverImpl implements FsBeanResolver, FsBeanResolver.Handler {

    static final BeanResolverImpl INSTANCE =
        new BeanResolverImpl(Collections.singletonList(JavaBeanResolveHandler.INSTANCE), FsCache.softCache());

    private final List<Handler> handlers;
    private final @Nullable FsCache<Type, FsBean> cache;

    BeanResolverImpl(Iterable<Handler> handlers, @Nullable FsCache<Type, FsBean> cache) {
        this.handlers = FsCollect.immutableList(handlers);
        this.cache = cache;
    }

    @Override
    public FsBean resolve(Type type) {
        if (cache == null) {
            return resolve0(type);
        }
        return cache.get(type, this::resolve0);
    }

    @Override
    public List<Handler> getHandlers() {
        return handlers;
    }

    @Override
    public FsBeanResolver withHandler(Handler handler) {
        List<Handler> newHandlers = new ArrayList<>(handlers.size() + 1);
        return new BeanResolverImpl(newHandlers, cache);
    }

    @Override
    public Handler asHandler() {
        return this;
    }

    @Override
    public @Nullable Object resolve(BeanBuilder builder) {
        for (Handler handler : handlers) {
            Object result = handler.resolve(builder);
            if (!Objects.equals(result, Fs.CONTINUE)) {
                return result;
            }
        }
        return Fs.CONTINUE;
    }

    private FsBean resolve0(Type type) {
        FsBeanBuilderImpl builder = new FsBeanBuilderImpl(type);
        for (Handler handler : handlers) {
            Object result = handler.resolve(builder);
            if (Fs.CONTINUE != result) {
                break;
            }
        }
        builder.build();
        return builder;
    }

    static final class FsBeanBuilderImpl implements BeanBuilder {

        private final Type type;
        private volatile Map<String, FsBeanProperty> properties = new LinkedHashMap<>();
        private volatile boolean built = false;
        private int hash = 0;
        private String toString = null;

        FsBeanBuilderImpl(Type type) {
            this.type = type;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public Map<String, FsBeanProperty> getProperties() {
            return properties;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            FsBeanBuilderImpl that = (FsBeanBuilderImpl) o;
            return Objects.equals(type, that.type) && Objects.equals(properties, that.properties);
        }

        @Override
        public int hashCode() {
            if (!built) {
                return Objects.hash(type, properties);
            }
            if (hash == 0) {
                int finalHash = Objects.hash(type, properties);
                if (finalHash == 0) {
                    finalHash = 1;
                }
                hash = finalHash;
                return finalHash;
            }
            return hash;
        }

        @Override
        public String toString() {
            if (!built) {
                return computeToString(false);
            }
            if (toString == null) {
                String finalToString = computeToString(true);
                toString = finalToString;
                return finalToString;
            }
            return toString;
        }

        private String computeToString(boolean built) {
            return (built ? "bean" : "beanBuilder") + "(" + properties.entrySet().stream()
                .map(it -> it.getKey() + ": " + it.getValue()).collect(Collectors.joining(", ")) +
                ")";
        }

        private void build() {
            Map<String, FsBeanProperty> builtProperties = this.properties;
            properties = FsCollect.immutableMap(builtProperties);
            built = true;
        }
    }
}