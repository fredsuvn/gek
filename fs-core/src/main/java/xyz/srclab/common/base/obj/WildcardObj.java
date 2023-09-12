package xyz.srclab.common.base.obj;

import xyz.srclab.annotations.Nullable;
import xyz.srclab.common.reflect.FsType;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * Specified object type of {@link WildcardType} for {@link FsObj}.
 *
 * @author fredsuvn
 */
public interface WildcardObj<T> extends FsObj<T> {

    /**
     * Returns type of current object as {@link WildcardType}.
     */
    @Override
    WildcardType getType();

    /**
     * Returns upper bound by {@link FsType#getUpperBound(WildcardType)}.
     */
    @Nullable
    Type getUpperBound();

    /**
     * Returns upper bound by {@link FsType#getLowerBound(WildcardType)}.
     */
    @Nullable
    Type getLowerBound();
}