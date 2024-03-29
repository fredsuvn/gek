package xyz.fsgek.common.convert.handlers;

import xyz.fsgek.annotations.Nullable;
import xyz.fsgek.common.reflect.GekReflect;
import xyz.fsgek.common.base.GekWrapper;
import xyz.fsgek.common.bean.GekBeanCopier;
import xyz.fsgek.common.bean.GekBeanResolver;
import xyz.fsgek.common.convert.GekConverter;

import java.lang.reflect.Type;
import java.time.*;
import java.time.zone.ZoneRules;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Convert handler implementation which is used to support the conversion for bean.
 * <p>
 * This handler is system default suffix handler (with {@link #BeanConvertHandler()}),
 * any object will be seen as "bean", and the conversion means create new object and copy properties.
 * <p>
 * It creates new {@link Map} for these target map types:
 * <ul>
 *     <li>{@link Map};</li>
 *     <li>{@link AbstractMap};</li>
 *     <li>{@link LinkedHashMap};</li>
 *     <li>{@link HashMap};</li>
 *     <li>{@link TreeMap};</li>
 *     <li>{@link ConcurrentMap};</li>
 *     <li>{@link ConcurrentHashMap};</li>
 *     <li>{@link Hashtable};</li>
 *     <li>{@link ConcurrentSkipListMap};</li>
 * </ul>
 * For other types, it creates with their empty constructor.
 * <p>
 * Note if the {@code obj} is null, return {@link GekWrapper#empty()}.
 *
 * @author fredsuvn
 */
public class BeanConvertHandler implements GekConverter.Handler {

    /**
     * An instance with {@link #BeanConvertHandler()}.
     */
    public static final BeanConvertHandler INSTANCE = new BeanConvertHandler();

    private static final Map<Class<?>, Supplier<Object>> GENERATOR_MAP = new ConcurrentHashMap<>();

    private static final Collection<Class<?>> UNSUPPORTED_TYPES = Arrays.asList(
        String.class, StringBuilder.class, StringBuffer.class,
        Boolean.class, boolean.class, Void.class, void.class,
        Byte.class, Short.class, Character.class, Integer.class, Long.class, Float.class, Double.class,
        byte.class, short.class, char.class, int.class, long.class, float.class, double.class,
        Date.class, Instant.class, LocalDateTime.class, LocalDate.class, LocalTime.class,
        OffsetDateTime.class, ZonedDateTime.class, Locale.class,
        ZoneId.class, ZoneOffset.class, ZoneRules.class, TimeZone.class, Duration.class,
        Iterable.class, Collection.class, List.class, AbstractList.class, ArrayList.class, LinkedList.class,
        CopyOnWriteArrayList.class, Set.class, LinkedHashSet.class,
        HashSet.class, TreeSet.class, ConcurrentSkipListSet.class
    );

    static {
        GENERATOR_MAP.put(Map.class, LinkedHashMap::new);
        GENERATOR_MAP.put(AbstractMap.class, LinkedHashMap::new);
        GENERATOR_MAP.put(LinkedHashMap.class, LinkedHashMap::new);
        GENERATOR_MAP.put(HashMap.class, HashMap::new);
        GENERATOR_MAP.put(TreeMap.class, TreeMap::new);
        GENERATOR_MAP.put(ConcurrentMap.class, ConcurrentHashMap::new);
        GENERATOR_MAP.put(ConcurrentHashMap.class, ConcurrentHashMap::new);
        GENERATOR_MAP.put(Hashtable.class, Hashtable::new);
        GENERATOR_MAP.put(ConcurrentSkipListMap.class, ConcurrentSkipListMap::new);
    }

    private final GekBeanResolver beanResolver;
    private final GekBeanCopier copier;

    /**
     * Constructs with {@link GekBeanResolver#defaultResolver()}.
     *
     * @see #BeanConvertHandler(GekBeanResolver)
     */
    public BeanConvertHandler() {
        this(GekBeanResolver.defaultResolver());
    }

    /**
     * Constructs with given bean resolver.
     *
     * @param beanResolver bean resolver
     */
    public BeanConvertHandler(GekBeanResolver beanResolver) {
        this.beanResolver = beanResolver;
        this.copier = GekBeanCopier.defaultCopier().toBuilder()
            .beanResolver(beanResolver)
            .build();
    }

    @Override
    public @Nullable Object convert(@Nullable Object source, Type sourceType, Type targetType, GekConverter converter) {
        if (source == null) {
            return GekWrapper.empty();
        }
        Class<?> targetRawType = GekReflect.getRawType(targetType);
        if (targetRawType == null || targetRawType.isArray() || UNSUPPORTED_TYPES.contains(targetRawType)) {
            return null;
        }
        Supplier<Object> generator = GENERATOR_MAP.get(targetRawType);
        Object dest;
        if (generator != null) {
            dest = generator.get();
        } else {
            dest = GekReflect.newInstance(targetRawType);
        }
        if (dest == null) {
            return null;
        }
        return getCopier().withConverter(converter).copyProperties(source, sourceType, dest, targetType);
    }

    /**
     * Returns bean resolver of this handler.
     *
     * @return bean resolver of this handler
     */
    public GekBeanResolver getBeanResolver() {
        return beanResolver;
    }

    /**
     * Returns copier of this handler.
     *
     * @return copier of this handler
     */
    public GekBeanCopier getCopier() {
        return copier;
    }
}
