package xyz.fsgek.common.base.obj;

import xyz.fsgek.annotations.Nullable;
import xyz.fsgek.common.reflect.GekReflect;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * Specified object type of {@link WildcardType} for {@link GekObj}.
 *
 * @author fredsuvn
 */
public interface WildcardObj<T> extends GekObj<T> {

    /**
     * Returns type of hold object as {@link WildcardType}.
     *
     * @return type of hold object as {@link WildcardType}
     */
    @Override
    WildcardType getType();

    /**
     * Returns upper bound by {@link GekReflect#getUpperBound(WildcardType)}.
     *
     * @return upper bound type or null
     */
    @Nullable
    Type getUpperBound();

    /**
     * Returns upper bound by {@link GekReflect#getLowerBound(WildcardType)}.
     *
     * @return lower bound type or null
     */
    @Nullable
    Type getLowerBound();
}
