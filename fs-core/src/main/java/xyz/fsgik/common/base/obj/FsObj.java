package xyz.fsgik.common.base.obj;

import xyz.fsgik.common.reflect.FsType;
import xyz.fsgik.common.reflect.TypeRef;

import java.lang.reflect.*;
import java.util.Collections;

/**
 * This class wraps an object and its type.
 * This class is usually used to specify clear type for an object.
 *
 * @author fredsuvn
 */
public interface FsObj<T> {

    /**
     * Wraps with given object and its type.
     * <p>
     * This method will return instance of:
     * <ul>
     *     <li>{@link ClassObj};</li>
     *     <li>{@link ParameterizedObj};</li>
     *     <li>{@link WildcardObj};</li>
     *     <li>{@link TypeVariableObj};</li>
     *     <li>{@link GenericArrayObj};</li>
     * </ul>
     *
     * @param object given object
     * @param type   type of given object
     */
    static <T> FsObj<T> wrap(T object, Type type) {
        if (type instanceof Class) {
            return new Impls.ClassImpl<>(object, (Class<?>) type);
        }
        if (type instanceof ParameterizedType) {
            return new Impls.ParameterizedImpl<>(object, (ParameterizedType) type);
        }
        if (type instanceof WildcardType) {
            return new Impls.WildcardImpl<>(object, (WildcardType) type);
        }
        if (type instanceof TypeVariable) {
            return new Impls.TypeVariableImpl<>(object, (TypeVariable<?>) type);
        }
        if (type instanceof GenericArrayType) {
            return new Impls.GenericArrayImpl<>(object, (GenericArrayType) type);
        }
        throw new UnsupportedOperationException(
            "Type must be one of Class, ParameterizedType, WildcardType, TypeVariable or GenericArrayType.");
    }

    /**
     * Wraps with given object and its type ref. This method will call {@link #wrap(Object, Type)}.
     *
     * @param object  given object
     * @param typeRef type ref of given object
     */
    static <T> FsObj<T> wrap(T object, TypeRef<T> typeRef) {
        return wrap(object, typeRef.getType());
    }

    /**
     * Returns current object.
     */
    T getObject();

    /**
     * Return type of current object.
     */
    Type getType();

    /**
     * To {@link ClassObj}.
     * <p>
     * Note {@link ParameterizedObj} can convert to {@link Class} without its generic types.
     */
    default ClassObj<T> toClassObj() {
        if (this instanceof ClassObj) {
            return (ClassObj<T>) this;
        }
        if (this instanceof ParameterizedObj) {
            return new Impls.ClassImpl<>(getObject(), FsType.getRawType(getType()));
        }
        return new Impls.ClassImpl<>(getObject(), (Class<?>) getType());
    }

    /**
     * To {@link ParameterizedObj}.
     * <p>
     * Note {@link ClassObj} can convert to {@link ParameterizedObj} with a generic type of {@link Object} type.
     */
    default ParameterizedObj<T> toParameterizedObj() {
        if (this instanceof ParameterizedObj) {
            return (ParameterizedObj<T>) this;
        }
        if (this instanceof ClassObj) {
            return new Impls.ParameterizedImpl<>(getObject(),
                FsType.parameterizedType(getType(), Collections.singletonList(Object.class)));
        }
        return new Impls.ParameterizedImpl<>(getObject(), (ParameterizedType) getType());
    }

    /**
     * To {@link WildcardObj}.
     */
    default WildcardObj<T> toWildcardObj() {
        if (this instanceof WildcardObj) {
            return (WildcardObj<T>) this;
        }
        return new Impls.WildcardImpl<>(getObject(), (WildcardType) getType());
    }

    /**
     * To {@link TypeVariableObj}.
     */
    default TypeVariableObj<T> toTypeVariableObj() {
        if (this instanceof TypeVariableObj) {
            return (TypeVariableObj<T>) this;
        }
        return new Impls.TypeVariableImpl<>(getObject(), (TypeVariable<?>) getType());
    }

    /**
     * To {@link GenericArrayObj}.
     */
    default GenericArrayObj<T> toGenericArrayObj() {
        if (this instanceof GenericArrayObj) {
            return (GenericArrayObj<T>) this;
        }
        return new Impls.GenericArrayImpl<>(getObject(), (GenericArrayType) getType());
    }
}