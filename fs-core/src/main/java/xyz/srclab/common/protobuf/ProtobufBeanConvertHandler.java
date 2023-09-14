package xyz.srclab.common.protobuf;

import com.google.protobuf.Message;
import xyz.srclab.annotations.Nullable;
import xyz.srclab.common.bean.FsBeanCopier;
import xyz.srclab.common.convert.FsConvertException;
import xyz.srclab.common.convert.FsConverter;
import xyz.srclab.common.convert.handlers.BeanConvertHandler;
import xyz.srclab.common.reflect.FsType;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Convert handler implementation for <a href="https://github.com/protocolbuffers/protobuf">Protocol Buffers</a>.
 * This class extends {@link BeanConvertHandler} to supports convert to protobuf types.
 *
 * @author fredsuvn
 */
public class ProtobufBeanConvertHandler extends BeanConvertHandler {

    /**
     * An instance with {@link #ProtobufBeanConvertHandler()}.
     */
    public static final ProtobufBeanConvertHandler INSTANCE = new ProtobufBeanConvertHandler();

    /**
     * Constructs with {@link FsProtobuf#protobufBeanCopier()}.
     */
    public ProtobufBeanConvertHandler() {
        this(FsProtobuf.protobufBeanCopier());
    }

    /**
     * Constructs with given bean copier.
     *
     * @param copier given bean copier
     */
    public ProtobufBeanConvertHandler(FsBeanCopier copier) {
        super(copier);
    }

    @Override
    public @Nullable Object convert(@Nullable Object source, Type sourceType, Type targetType, FsConverter converter) {
        if (source == null) {
            return null;
        }
        if (!(targetType instanceof Class<?>)) {
            return super.convert(source, sourceType, targetType, converter);
        }
        Class<?> rawType = FsType.getRawType(targetType);
        //Check whether it is a protobuf object
        boolean isProtobuf = false;
        boolean isBuilder = false;
        if (Message.class.isAssignableFrom(rawType)) {
            isProtobuf = true;
        }
        if (Message.Builder.class.isAssignableFrom(rawType)) {
            isProtobuf = true;
            isBuilder = true;
        }
        if (!isProtobuf) {
            return super.convert(source, sourceType, targetType, converter);
        }
        try {
            Object builder = getProtobufBuilder(rawType, isBuilder);
            getCopier().copyProperties(source, sourceType, builder, builder.getClass());
            return build(builder, isBuilder);
        } catch (FsConvertException e) {
            throw e;
        } catch (Exception e) {
            throw new FsConvertException(e);
        }
    }

    @Nullable
    private Object getProtobufBuilder(Class<?> type, boolean isBuilder) throws Exception {
        if (isBuilder) {
            Method build = type.getMethod("build");
            Class<?> srcType = build.getReturnType();
            Method newBuilder = srcType.getMethod("newBuilder");
            return newBuilder.invoke(null);
        } else {
            Method newBuilder = type.getMethod("newBuilder");
            return newBuilder.invoke(null);
        }
    }

    private Object build(Object builder, boolean isBuilder) throws Exception {
        if (isBuilder) {
            return builder;
        }
        Method build = builder.getClass().getMethod("build");
        return build.invoke(builder);
    }
}