package test.xyz.srclab.common.reflect

import org.testng.annotations.Test
import xyz.srclab.common.reflect.MethodHelper
import xyz.srclab.test.doAssertEquals
import xyz.srclab.test.doExpectThrowable
import java.lang.reflect.Modifier

object MethodHelperTest {

    @Test
    fun testGetMethod() {
        val method = MethodHelper.getMethod(Any::class.java, "toString")
        doAssertEquals(method, Any::class.java.getMethod("toString"))

        val aAllMethods = A::class.java.declaredMethods
        doAssertEquals(aAllMethods[0], A::class.java.getDeclaredMethod("toString"))
        val anyAllMethods = Any::class.java.declaredMethods
        val allMethods = mutableListOf(*aAllMethods)
        allMethods.addAll(anyAllMethods)
        doAssertEquals(MethodHelper.getAllMethods(A::class.java), allMethods)
        allMethods.remove(Any::class.java.getMethod("toString"))
        allMethods.remove(A::class.java.getMethod("staticFun"))
        doAssertEquals(
            MethodHelper.getOverrideableMethods(A::class.java),
            A::class.java.methods.filter { m -> !Modifier.isStatic(m.modifiers) && !Modifier.isFinal(m.modifiers) })

        doAssertEquals(
            MethodHelper.getPublicStaticMethods(A::class.java),
            listOf(A::class.java.getMethod("staticFun"))
        )
        val publicNonStaticMethods = mutableListOf(*A::class.java.methods)
        publicNonStaticMethods.remove(A::class.java.getMethod("staticFun"))
        doAssertEquals(
            MethodHelper.getPublicNonStaticMethods(A::class.java),
            publicNonStaticMethods
        )

        doExpectThrowable(IllegalArgumentException::class.java) {
            MethodHelper.getMethod(A::class.java, "sss")
        }
    }

    class A {

        companion object {
            @JvmStatic
            fun staticFun() {

            }
        }

        override fun toString(): String {
            return "A: " + super.toString()
        }
    }
}