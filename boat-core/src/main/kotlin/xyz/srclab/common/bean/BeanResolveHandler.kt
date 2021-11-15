package xyz.srclab.common.bean

import xyz.srclab.annotations.Written
import xyz.srclab.common.base.BJumpState
import xyz.srclab.common.base.BNamingCase
import xyz.srclab.common.invoke.BInstInvoker
import xyz.srclab.common.invoke.toInstInvoker
import xyz.srclab.common.reflect.eraseTypeParameters
import xyz.srclab.common.reflect.rawClass
import xyz.srclab.common.reflect.searchFieldOrNull
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Type

/**
 * Handler to resolve specified bean type.
 * default, a [BeanResolver] contains a chain of [BeanResolveHandler]s.
 *
 * @see AbstractBeanResolveHandler
 * @see BeanStyleBeanResolveHandler
 * @see RecordStyleBeanResolveHandler
 */
interface BeanResolveHandler {

    /**
     * Resolves into given [builder].
     */
    fun resolve(context: BeanResolveContext, @Written builder: BeanTypeBuilder): BJumpState

    companion object {

        @JvmField
        val BEAN_STYLE = BeanStyleBeanResolveHandler

        @JvmField
        val RECORD_STYLE = RecordStyleBeanResolveHandler

        @JvmField
        val DEFAULTS: List<BeanResolveHandler> = listOf(
            BEAN_STYLE
        )
    }
}

/**
 * Convenient [BeanResolveHandler], just override [resolveAccessors] method.
 */
abstract class AbstractBeanResolveHandler : BeanResolveHandler {

    /**
     * Overrides this method to provide getters and setters of target bean type.
     */
    protected abstract fun resolveAccessors(
        context: BeanResolveContext,
        @Written builder: BeanTypeBuilder,
        @Written getters: MutableMap<String, GetterInfo>,
        @Written setters: MutableMap<String, SetterInfo>,
    ): BJumpState

    override fun resolve(context: BeanResolveContext, @Written builder: BeanTypeBuilder): BJumpState {

        val getters: MutableMap<String, GetterInfo> = LinkedHashMap()
        val setters: MutableMap<String, SetterInfo> = LinkedHashMap()

        val result = resolveAccessors(context, builder, getters, setters)

        val beanType = builder.build()
        for (getterEntry in getters) {
            val propertyName = getterEntry.key
            val getter = getterEntry.value
            val setter = setters[propertyName]
            if (setter === null) {
                val propertyType = PropertyType.newPropertyType(
                    beanType,
                    propertyName,
                    getter.type,
                    getter.getter,
                    null,
                    getter.field,
                    getter.getterMethod,
                    null
                )
                builder.addProperty(propertyType)
            } else if (getter.type == setter.type) {
                val propertyType = PropertyType.newPropertyType(
                    beanType,
                    propertyName,
                    getter.type,
                    getter.getter,
                    setter.setter,
                    getter.field,
                    getter.getterMethod,
                    setter.setterMethod
                )
                builder.addProperty(propertyType)
                setters.remove(propertyName)
            }
        }

        for (setterEntry in setters) {
            val propertyName = setterEntry.key
            val setter = setterEntry.value
            val propertyType = PropertyType.newPropertyType(
                beanType,
                propertyName,
                setter.type,
                null,
                setter.setter,
                setter.field,
                null,
                setter.setterMethod
            )
            builder.addProperty(propertyType)
        }

        return result
    }

    data class GetterInfo(
        val name: String,
        val type: Type,
        val getter: BInstInvoker?,
        val field: Field?,
        val getterMethod: Method?,
    )

    data class SetterInfo(
        val name: String,
        val type: Type,
        val setter: BInstInvoker?,
        val field: Field?,
        val setterMethod: Method?,
    )
}


/**
 * Bean style of [AbstractBeanResolveHandler]:
 *
 * * getter: getXxx()
 * * setter: setXxx(Xxx)
 *
 * Note this handler doesn't add property if property's name has been existed in builder's properties.
 */
object BeanStyleBeanResolveHandler : AbstractBeanResolveHandler() {

    override fun resolveAccessors(
        context: BeanResolveContext,
        @Written builder: BeanTypeBuilder,
        getters: MutableMap<String, GetterInfo>,
        setters: MutableMap<String, SetterInfo>,
    ): BJumpState {
        val beanClass = builder.type.rawClass
        val methods = context.methods
        for (method in methods) {
            if (method.isBridge || method.isSynthetic) {
                continue
            }
            val name = method.name
            if (name.length <= 3) {
                continue
            }
            if (name.startsWith("get") && method.parameterCount == 0) {
                val propertyName =
                    BNamingCase.UPPER_CAMEL.convert(name.substring(3, name.length), BNamingCase.LOWER_CAMEL)
                if (builder.hasProperty(propertyName)) {
                    continue
                }
                val type = method.genericReturnType.eraseTypeParameters(context.typeArguments)
                val field = beanClass.searchFieldOrNull(propertyName, true)
                getters[propertyName] = GetterInfo(propertyName, type, method.toInstInvoker(), field, method)
                continue
            }
            if (name.startsWith("set") && method.parameterCount == 1) {
                val propertyName =
                    BNamingCase.UPPER_CAMEL.convert(name.substring(3, name.length), BNamingCase.LOWER_CAMEL)
                if (builder.hasProperty(propertyName)) {
                    continue
                }
                val type = method.genericParameterTypes[0].eraseTypeParameters(context.typeArguments)
                val field = beanClass.searchFieldOrNull(propertyName, true)
                setters[propertyName] = SetterInfo(propertyName, type, method.toInstInvoker(), field, method)
                continue
            }
        }
        return BJumpState.CONTINUE
    }
}

/**
 * Record style of [AbstractBeanResolveHandler]:
 *
 * * getter: xxx()
 * * setter: xxx(xxx)
 *
 * Note this handler doesn't add property if property's name has been existed in builder's properties.
 */
object RecordStyleBeanResolveHandler : AbstractBeanResolveHandler() {

    override fun resolveAccessors(
        context: BeanResolveContext,
        @Written builder: BeanTypeBuilder,
        getters: MutableMap<String, GetterInfo>,
        setters: MutableMap<String, SetterInfo>,
    ): BJumpState {
        val beanClass = builder.type.rawClass
        val methods = context.methods
        for (method in methods) {
            if (method.isBridge || method.isSynthetic || method.declaringClass == Any::class.java) {
                continue
            }
            val propertyName = method.name
            if (builder.hasProperty(propertyName)) {
                continue
            }
            if (method.parameterCount == 0) {
                val type = method.genericReturnType.eraseTypeParameters(context.typeArguments)
                val field = beanClass.searchFieldOrNull(propertyName, true)
                getters[propertyName] = GetterInfo(propertyName, type, method.toInstInvoker(), field, method)
                continue
            }
            if (method.parameterCount == 1) {
                val type = method.genericParameterTypes[0].eraseTypeParameters(context.typeArguments)
                val field = beanClass.searchFieldOrNull(propertyName, true)
                setters[propertyName] = SetterInfo(propertyName, type, method.toInstInvoker(), field, method)
                continue
            }
        }
        return BJumpState.CONTINUE
    }
}