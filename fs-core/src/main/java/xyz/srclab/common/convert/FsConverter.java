package xyz.srclab.common.convert;

import xyz.srclab.annotations.Nullable;
import xyz.srclab.common.base.FsUnsafe;
import xyz.srclab.common.collect.FsCollect;
import xyz.srclab.common.reflect.TypeRef;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Converter to convert source object from source type to target type.
 * <p>
 * A {@link FsConverter} consists of a list of {@link Handler}s.
 * More specifically, the converter is composed of a prefix handler (maybe null), a list of common handlers,
 * and a suffix handler (maybe null).
 * <p>
 * The conversion is performed in the order of the prefix handler (if not null), common handlers,
 * and suffix converter (if not null). If any handler returns a non-{@link #CONTINUE} and non-{@link #BREAK} value,
 * the conversion is successful and that return value will be returned.
 * <p>
 * If a handler returns {@link #CONTINUE}, it means that handler doesn't support current conversion and hands off to
 * next handler; If it returns {@link #BREAK}, means this converter doesn't support current conversion.
 *
 * @author fredsuvn
 * @see Handler
 */
public interface FsConverter {

    /**
     * Returns default converter, of which handlers are:
     * <ul>
     *     <li>Prefix: {@link xyz.srclab.common.convert.handlers.AssignableConvertHandler};</li>
     *     <li>Suffix: {@link xyz.srclab.common.convert.handlers.BeanConvertHandler};</li>
     *     <li>Common:
     *     <ul>
     *         <li>{@link xyz.srclab.common.convert.handlers.DateConvertHandler};</li>
     *         <li>{@link xyz.srclab.common.convert.handlers.BooleanConvertHandler};</li>
     *         <li>{@link xyz.srclab.common.convert.handlers.NumberConvertHandler};</li>
     *         <li>{@link xyz.srclab.common.convert.handlers.StringConvertHandler};</li>
     *         <li>{@link xyz.srclab.common.convert.handlers.CollectConvertHandler};</li>
     *     </ul>
     *     </li>
     * </ul>
     * Those construct from empty constructor and in the order listed.
     */
    static FsConverter defaultConverter() {
        return FsUnsafe.ForConvert.DEFAULT_CONVERTER;
    }

    /**
     * Represents current conversion is unsupported by this handler and will be handed off to next handler.
     */
    Object CONTINUE = new Object();

    /**
     * Represents current conversion is unsupported by this handler and will be broken out the conversion will be finished.
     */
    Object BREAK = new Object();

    /**
     * Represents current conversion is not supported by current converter.
     */
    Object UNSUPPORTED = new Object();

    /**
     * Returns a converters with given prefix, suffix, common handlers, and default conversion options.
     *
     * @param prefix prefix handler
     * @param suffix suffix handler
     * @param common common handlers
     */
    static FsConverter withHandlers(
        @Nullable Handler prefix,
        @Nullable Handler suffix,
        Iterable<Handler> common
    ) {
        return newConverter(prefix, suffix, common, Options.defaultOptions());
    }

    /**
     * Returns a converters with given common handlers and default conversion options.
     *
     * @param common common handlers
     */
    static FsConverter withHandlers(Iterable<Handler> common) {
        return withHandlers(null, null, common);
    }

    /**
     * Returns a converters with given prefix, suffix, common handlers and conversion options.
     *
     * @param prefix  prefix handler
     * @param suffix  suffix handler
     * @param common  common handlers
     * @param options conversion options
     */
    static FsConverter newConverter(
        @Nullable Handler prefix,
        @Nullable Handler suffix,
        Iterable<Handler> common,
        Options options
    ) {
        class FsConverterImpl implements FsConverter, Handler {

            private final List<Handler> commonHandlers;

            FsConverterImpl(List<Handler> commonHandlers) {
                this.commonHandlers = commonHandlers;
            }

            @Override
            public @Nullable Handler getPrefixHandler() {
                return prefix;
            }

            @Override
            public @Nullable Handler getSuffixHandler() {
                return suffix;
            }

            @Override
            public List<Handler> getCommonHandlers() {
                return commonHandlers;
            }

            @Override
            public Options getOptions() {
                return options;
            }

            @Override
            public Handler asHandler() {
                return this;
            }

            @Override
            public @Nullable Object convert(@Nullable Object source, Type sourceType, Type targetType, Options options1, FsConverter converter) {
                Object value = convert(source, sourceType, targetType, options1);
                if (value == UNSUPPORTED) {
                    return BREAK;
                }
                return value;
            }
        }
        return new FsConverterImpl(FsCollect.immutableList(common));
    }

    /**
     * Returns prefix handler.
     */
    @Nullable
    Handler getPrefixHandler();

    /**
     * Returns prefix handler.
     */
    @Nullable
    Handler getSuffixHandler();

    /**
     * Returns common handlers.
     */
    List<Handler> getCommonHandlers();

    /**
     * Returns conversion options.
     */
    Options getOptions();

    /**
     * Converts given object to target type.
     * If the conversion is unsupported, return null.
     * <p>
     * <b>
     * Note the return value itself may also be null.
     * </b>
     *
     * @param obj        given object
     * @param targetType target type
     */
    @Nullable
    default <T> T convert(@Nullable Object obj, Class<T> targetType) {
        return convert(obj, (Type) targetType);
    }

    /**
     * Converts given object to target type.
     * If the conversion is unsupported, return null.
     * <p>
     * <b>
     * Note the return value itself may also be null.
     * </b>
     *
     * @param obj        given object
     * @param targetType ref of target type
     */
    @Nullable
    default <T> T convert(@Nullable Object obj, TypeRef<T> targetType) {
        return convert(obj, targetType.getType());
    }

    /**
     * Converts given object to target type.
     * If the conversion is unsupported, return null.
     * <p>
     * <b>
     * Note the return value itself may also be null.
     * </b>
     *
     * @param obj        given object
     * @param targetType target type
     */
    @Nullable
    default <T> T convert(@Nullable Object obj, Type targetType) {
        Object value = convert(obj, obj.getClass(), targetType, getOptions());
        if (value == UNSUPPORTED) {
            return null;
        }
        return (T) value;
    }

    /**
     * Converts given object from specified type to target type.
     * If the conversion is unsupported, return null.
     * <p>
     * <b>
     * Note the return value itself may also be null.
     * </b>
     *
     * @param obj        given object
     * @param fromType   specified type
     * @param targetType target type
     */
    @Nullable
    default <T> T convertByType(@Nullable Object obj, Type fromType, Type targetType) {
        Object value = convert(obj, fromType, targetType, getOptions());
        if (value == UNSUPPORTED) {
            return null;
        }
        return (T) value;
    }

    /**
     * Converts source object from source type to target type.
     * If return value is {@link #UNSUPPORTED}, it indicates current conversion is unsupported.
     * Any other return value indicates current conversion is successful and the return value is valid, including null.
     *
     * @param source     source object
     * @param sourceType source type
     * @param targetType target type
     */
    @Nullable
    default Object convert(@Nullable Object source, Type sourceType, Type targetType, Options options) {
        Handler prefix = getPrefixHandler();
        if (prefix != null) {
            Object value = prefix.convert(source, sourceType, targetType, options, this);
            if (value == BREAK || value == UNSUPPORTED) {
                return UNSUPPORTED;
            }
            if (value != CONTINUE) {
                return value;
            }
        }
        for (Handler commonHandler : getCommonHandlers()) {
            Object value = commonHandler.convert(source, sourceType, targetType, options, this);
            if (value == BREAK || value == UNSUPPORTED) {
                return UNSUPPORTED;
            }
            if (value != CONTINUE) {
                return value;
            }
        }
        Handler suffix = getSuffixHandler();
        if (suffix != null) {
            Object value = suffix.convert(source, sourceType, targetType, options, this);
            if (value == BREAK || value == CONTINUE || value == UNSUPPORTED) {
                return UNSUPPORTED;
            }
            return value;
        }
        return UNSUPPORTED;
    }

    /**
     * Converts source object from source type to target type.
     * If current conversion is unsupported, an {@link UnsupportedConvertException} will be thrown
     *
     * @param source     source object
     * @param sourceType source type
     * @param targetType target type
     */
    @Nullable
    default Object convertOrThrow(@Nullable Object source, Type sourceType, Type targetType, Options options)
        throws UnsupportedConvertException {
        Object value = convert(source, sourceType, targetType, options);
        if (value == UNSUPPORTED) {
            throw new UnsupportedConvertException(sourceType, targetType);
        }
        return value;
    }

    /**
     * Returns a new converter with handlers consist of the handlers from the current converter,
     * but inserts given handler at first index of common handlers.
     *
     * @param handler given handler
     */
    default FsConverter withCommonHandler(Handler handler) {
        List<Handler> common = getCommonHandlers();
        List<Handler> newCommon = new ArrayList<>(common.size() + 1);
        newCommon.add(handler);
        newCommon.addAll(common);
        return withHandlers(getPrefixHandler(), getSuffixHandler(), newCommon);
    }

    /**
     * Returns this converter as handler type.
     */
    Handler asHandler();

    /**
     * Handler of {@link FsConvert}.
     *
     * @author fredsuvn
     * @see FsConverter
     */
    interface Handler {

        /**
         * Converts source object from source type to target type.
         * <p>
         * If this method returns {@link #CONTINUE}, it means that this handler doesn't support current conversion,
         * but it will hand off to next handler.
         * <p>
         * If this method returns {@link #BREAK}, it means that this handler doesn't support current conversion,
         * and the converter will break current conversion. This indicates that the converter does not support current
         * conversion.
         * <p>
         * Otherwise, any other return value is considered as the final conversion result.
         *
         * @param source     source object
         * @param sourceType source type
         * @param targetType target type
         * @param converter  converter in which this handler is located.
         */
        @Nullable
        Object convert(
            @Nullable Object source, Type sourceType, Type targetType, Options options, FsConverter converter);
    }

    /**
     * Options for convert.
     *
     * @author fredsuvn
     */
    interface Options {

        /**
         * Returns default options:
         * <ul>
         *     <li>
         *         Compatibility policy: {@link #NEED_ASSIGNABLE};
         *     </li>
         * </ul>
         */
        static FsConverter.Options defaultOptions() {
            return FsUnsafe.ForConvert.DEFAULT_OPTIONS;
        }

        /**
         * Returns options with given compatibility policy.
         *
         * @param policy given compatibility policy, see {@link #objectGenerationPolicy()}
         */
        static Options withCompatibilityPolicy(int policy) {
            return new Options() {
                @Override
                public int objectGenerationPolicy() {
                    return policy;
                }
            };
        }

        /**
         * Policy value for object generation:
         * If and only if target type is assignable from source type,
         * return source object, else return new instance of target type;
         */
        int NEED_ASSIGNABLE = 1;

        /**
         * Policy value for object generation:
         * If and only if target type is equal to source type, return source object,
         * else return new instance of target type;
         */
        int NEED_EQUAL = 2;

        /**
         * Policy value for object generation:
         * Always return new instance of target type;
         */
        int ALWAYS_NEW = 3;

        /**
         * Returns object generation policy:
         * <ul>
         *     <li>
         *         {@link #NEED_ASSIGNABLE}: If and only if target type is assignable from source type,
         *         return source object, else return new instance of target type;
         *     </li>
         *     <li>
         *         {@link #NEED_EQUAL}: If and only if target type is equal to source type, return source object,
         *         else return new instance of target type;
         *     </li>
         *     <li>
         *         {@link #ALWAYS_NEW}: Always return new instance of target type;
         *     </li>
         * </ul>
         */
        int objectGenerationPolicy();

        /**
         * Returns an Options of which {@link #objectGenerationPolicy()} is replaced by given policy.
         *
         * @param policy given policy
         */
        default Options replaceObjectGenerationPolicy(int policy) {
            if (objectGenerationPolicy() == policy) {
                return this;
            }
            return withCompatibilityPolicy(policy);
        }
    }
}
