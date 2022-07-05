package test.kotlin.xyz.srclab.common.base

import org.testng.Assert
import org.testng.annotations.Test
import xyz.srclab.common.base.defaultSerialVersion
import xyz.srclab.common.base.readObject
import xyz.srclab.common.base.writeObject
import java.io.File
import java.io.Serializable

class SerialTest {

    @Test
    fun testSerialize() {
        val a = A()
        a.a = "123"
        val temp = File.createTempFile("ttt", ".txt")
        a.writeObject(temp, true)
        val ar = temp.readObject<A>(true)
        Assert.assertEquals(ar.a, a.a)
        temp.delete()
    }

    open class A : Serializable {

        lateinit var a: String

        companion object {
            private val serialVersionUID: Long = defaultSerialVersion()
        }
    }
}