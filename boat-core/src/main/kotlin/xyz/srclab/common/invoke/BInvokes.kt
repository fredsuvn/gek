@file:JvmName("BInvokes")

package xyz.srclab.common.invoke

import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.concurrent.Callable
import java.util.function.Function

@JvmOverloads
fun Method.toInstInvoker(force: Boolean = false, reflect: Boolean = true): BInstInvoker {
    return if (reflect)
        ReflectedInstInvoker(this, force)
    else {
        UnreflectedInstInvoker(this, force)
    }
}

@JvmOverloads
fun Method.toFuncInvoker(force: Boolean = false, reflect: Boolean = true): BFuncInvoker {
    return if (reflect)
        ReflectedFuncInvoker(this, force)
    else {
        UnreflectedFuncInvoker(this, force)
    }
}

@JvmOverloads
fun Constructor<*>.toFuncInvoker(force: Boolean = false, reflect: Boolean = true): BFuncInvoker {
    return if (reflect)
        ReflectedConstructorInvoker(this, force)
    else {
        UnreflectedConstructorInvoker(this, force)
    }
}

fun Runnable.toFuncInvoker(): BFuncInvoker {
    return object : BFuncInvoker {
        override fun invoke(vararg args: Any?): Any? {
            this@toFuncInvoker.run()
            return null
        }
    }
}

fun Callable<*>.toFuncInvoker(): BFuncInvoker {
    return object : BFuncInvoker {
        override fun invoke(vararg args: Any?): Any? {
            return this@toFuncInvoker.call()
        }
    }
}

fun Function<Array<out Any?>, *>.toFuncInvoker(): BFuncInvoker {
    return object : BFuncInvoker {
        override fun invoke(vararg args: Any?): Any? {
            return this@toFuncInvoker.apply(args)
        }
    }
}

@JvmName("toFuncInvoker")
fun funcInvoker(func: (Array<out Any?>) -> Any?): BFuncInvoker {
    return object : BFuncInvoker {
        override fun invoke(vararg args: Any?): Any? {
            return func(args)
        }
    }
}

private class ReflectedInstInvoker(
    private val method: Method,
    force: Boolean,
) : BInstInvoker {

    init {
        if (force) {
            method.isAccessible = true
        }
    }

    override fun invoke(inst: Any, vararg args: Any?): Any? {
        return method.invoke(inst, *args)
    }
}

private class UnreflectedInstInvoker(
    method: Method,
    force: Boolean,
) : BInstInvoker {

    private val handle: MethodHandle

    init {
        if (force) {
            method.isAccessible = true
        }
        handle = MethodHandles.lookup().unreflect(method)
    }

    override fun invoke(inst: Any, vararg args: Any?): Any? {
        val arguments = arrayOfNulls<Any?>(args.size + 1)
        arguments[0] = inst
        System.arraycopy(args, 0, arguments, 1, args.size)
        return handle.invokeWithArguments(*arguments)
    }
}

private class ReflectedFuncInvoker(
    private val method: Method,
    force: Boolean,
) : BFuncInvoker {

    init {
        if (force) {
            method.isAccessible = true
        }
    }

    override fun invoke(vararg args: Any?): Any? {
        return method.invoke(null, *args)
    }
}

private class ReflectedConstructorInvoker(
    private val constructor: Constructor<*>,
    force: Boolean,
) : BFuncInvoker {

    init {
        if (force) {
            constructor.isAccessible = true
        }
    }

    override fun invoke(vararg args: Any?): Any? {
        return constructor.newInstance(*args)
    }
}

private class UnreflectedFuncInvoker(
    method: Method,
    force: Boolean,
) : BFuncInvoker {

    private val handle: MethodHandle

    init {
        if (force) {
            method.isAccessible = true
        }
        handle = MethodHandles.lookup().unreflect(method)
    }

    override fun invoke(vararg args: Any?): Any? {
        return handle.invokeWithArguments(*args)
    }
}

private class UnreflectedConstructorInvoker(
    constructor: Constructor<*>,
    force: Boolean,
) : BFuncInvoker {

    private val handle: MethodHandle

    init {
        if (force) {
            constructor.isAccessible = true
        }
        handle = MethodHandles.lookup().unreflectConstructor(constructor)
    }

    override fun invoke(vararg args: Any?): Any? {
        return handle.invokeWithArguments(*args)
    }
}