package xyz.fsgek.annotations;

import java.lang.annotation.*;

/**
 * Declares type of the annotated element is allowed to be the specified type.
 *
 * @author fredsuvn
 * @see AcceptedTypes
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.PARAMETER,
    ElementType.LOCAL_VARIABLE,
    ElementType.TYPE_PARAMETER,
    ElementType.TYPE_USE,
})
@Repeatable(AcceptedTypes.class)
public @interface AcceptedType {

    /**
     * Accepted type.
     *
     * @return accepted type
     */
    Class<?>[] value();
}