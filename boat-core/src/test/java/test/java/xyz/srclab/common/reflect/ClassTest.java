package test.java.xyz.srclab.common.reflect;

import org.testng.Assert;
import org.testng.annotations.Test;
import xyz.srclab.common.reflect.Reflects;
import xyz.srclab.common.test.TestLogger;

/**
 * @author sunqian
 */
public class ClassTest {

    private static final TestLogger logger = TestLogger.DEFAULT;

    @Test
    public void testToClass() {
        Class<NewClass> newClass = Reflects.toClass(
                "test.java.xyz.srclab.common.reflect.NewClass");
        Assert.assertEquals(newClass, NewClass.class);
    }

    @Test
    public void testToInstance() {
        NewClass newClass = Reflects.toInstance(
                "test.java.xyz.srclab.common.reflect.NewClass");
        Assert.assertEquals(newClass, new NewClass());
    }

    @Test
    public void testToWrapper() {
        Assert.assertEquals(Reflects.toWrapperClass(boolean.class), Boolean.class);
        Assert.assertEquals(Reflects.toWrapperClass(byte.class), Byte.class);
        Assert.assertEquals(Reflects.toWrapperClass(short.class), Short.class);
        Assert.assertEquals(Reflects.toWrapperClass(char.class), Character.class);
        Assert.assertEquals(Reflects.toWrapperClass(int.class), Integer.class);
        Assert.assertEquals(Reflects.toWrapperClass(long.class), Long.class);
        Assert.assertEquals(Reflects.toWrapperClass(float.class), Float.class);
        Assert.assertEquals(Reflects.toWrapperClass(double.class), Double.class);
        Assert.assertEquals(Reflects.toWrapperClass(void.class), Void.class);
    }
}