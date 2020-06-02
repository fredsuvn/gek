package xyz.srclab.common.bytecode;

import org.apache.commons.collections4.CollectionUtils;
import xyz.srclab.annotation.Immutable;
import xyz.srclab.annotation.Nullable;
import xyz.srclab.common.collection.IterableKit;
import xyz.srclab.common.collection.ListHelper;
import xyz.srclab.common.string.StringHelper;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @author sunqian
 */
@Immutable
public class BMethod implements BDescribable {

    private final String name;
    private final BType returnType;
    private final @Immutable List<BType> parameterTypes;
    private final @Immutable List<BTypeVariable> typeVariables;

    private @Nullable String descriptor;
    private @Nullable String signature;

    public BMethod(
            String name,
            @Nullable BType returnType,
            @Nullable Iterable<BType> parameterTypes,
            @Nullable Iterable<BTypeVariable> typeVariables
    ) {
        this.name = name;
        this.returnType = returnType == null ? ByteCodeHelper.PRIMITIVE_VOID : returnType;
        this.parameterTypes = parameterTypes == null ? Collections.emptyList() :
                ListHelper.immutable(IterableKit.asList(parameterTypes));
        this.typeVariables = typeVariables == null ? Collections.emptyList() :
                ListHelper.immutable(IterableKit.asList(typeVariables));
    }

    public BMethod(Method method) {
        this(
                method.getName(),
                new BRefType(method.getReturnType()),
                ListHelper.map(method.getParameterTypes(), BRefType::new),
                ListHelper.map(method.getTypeParameters(), BTypeVariable::new)
        );
    }

    public String getName() {
        return name;
    }

    public BType getReturnType() {
        return returnType;
    }

    public BType getParameterType(int index) {
        return parameterTypes.get(index);
    }

    public List<BType> getParameterTypes() {
        return parameterTypes;
    }

    public BTypeVariable getBTypeVariable(int index) {
        return typeVariables.get(index);
    }

    public List<BTypeVariable> getTypeVariables() {
        return typeVariables;
    }

    @Override
    public String getDescriptor() {
        if (descriptor == null) {
            descriptor = getDescriptor0();
        }
        return descriptor;
    }

    private String getDescriptor0() {
        if (CollectionUtils.isEmpty(parameterTypes)) {
            return "()" + returnType.getDescriptor();
        }
        String parameterTypesDescriptor = StringHelper.join("", parameterTypes, BDescribable::getDescriptor);
        return "(" + parameterTypesDescriptor + ")" + returnType.getDescriptor();
    }

    @Override
    public String getSignature() {
        if (signature == null) {
            signature = getSignature0();
        }
        return signature;
    }

    private String getSignature0() {
        String typeVariablesDeclaration = CollectionUtils.isEmpty(typeVariables) ? "" :
                ("<" + StringHelper.join("", typeVariables, BTypeVariable::getDeclaration) + ">");
        String parameterTypesSignature = CollectionUtils.isEmpty(parameterTypes) ? "" :
                StringHelper.join("", parameterTypes, BDescribable::getSignature);
        return typeVariablesDeclaration + "(" + parameterTypesSignature + ")" + returnType.getSignature();
    }
}
