package test;

import org.testng.Assert;
import org.testng.annotations.Test;
import xyz.fsgek.common.base.*;
import xyz.fsgek.common.io.GekIO;

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class GekTest {

    private static final String ECHO_CONTENT = "hello world!";

    @Test
    public void testThrow() {
        GekLogger.defaultLogger().info(GekTrace.stackTraceToString(
            new IllegalArgumentException(new IllegalStateException(new NullPointerException())))
        );
        GekLogger.defaultLogger().info(GekTrace.stackTraceToString(
            new IllegalArgumentException(new IllegalStateException(new NullPointerException())),
            " : ")
        );
    }

    @Test
    public void testFindCallerStackTrace() {
        T1.invoke1();
    }

    @Test
    public void testEqual() {
        Assert.assertTrue(Gek.equals(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        Assert.assertFalse(Gek.equals(new int[]{1, 2}, new int[]{1, 2, 3}));
        Assert.assertFalse(Gek.equalsWith(new int[]{1, 2, 3}, new int[]{1, 2, 3}, false, false));
        Assert.assertTrue(Gek.equals(
            new Object[]{new int[]{1, 2, 3}, new int[]{1, 2, 3}},
            new Object[]{new int[]{1, 2, 3}, new int[]{1, 2, 3}}
        ));
        Assert.assertFalse(Gek.equals(
            new Object[]{new int[]{1, 2, 3}, new int[]{1, 2}},
            new Object[]{new int[]{1, 2, 3}, new int[]{1, 2, 3}}
        ));
        Assert.assertFalse(Gek.equalsWith(
            new Object[]{new int[]{1, 2, 3}, new int[]{1, 2, 3}},
            new Object[]{new int[]{1, 2, 3}, new int[]{1, 2, 3}}, false, true
        ));
    }

    @Test
    public void testRes() throws IOException {
        URL f1 = Gek.findRes("/t2/f1.txt");
        Assert.assertEquals(GekIO.readString(f1.openStream(), GekChars.defaultCharset()), "f1.txt");
        Set<URL> set = Gek.findAllRes("/t2/f2.txt");
        for (URL url : set) {
            Assert.assertEquals(GekIO.readString(url.openStream(), GekChars.defaultCharset()), "f2.txt");
        }
    }

    @Test
    public void testProcess() throws InterruptedException {
        if (GekSystem.isLinux() || GekSystem.isMac() || GekSystem.isBsd()) {
            testEcho("echo " + ECHO_CONTENT);
        }
        if (GekSystem.isWindows()) {
            testEcho("cmd.exe /c echo " + ECHO_CONTENT);
        }
    }

    @Test
    public void testPing() throws InterruptedException {
        Process process = GekProcess.start("ping", "-n", "5", "127.0.0.1");
        Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();
        GekThread.start(() -> {
            while (true) {
                String output = GekIO.avalaibleString(process.getInputStream(), GekChars.nativeCharset());
                if (output == null) {
                    semaphore.release();
                    return;
                }
                if (GekString.isNotEmpty(output)) {
                    GekLogger.defaultLogger().info(output);
                }
                GekThread.sleep(1);
            }
        });
        process.waitFor();
        while (semaphore.hasQueuedThreads()) {
            GekThread.sleep(1000);
        }
        process.destroy();
    }

    private void testEcho(String command) throws InterruptedException {
        Process process = GekProcess.start(command);
        process.waitFor();
        String output = GekIO.avalaibleString(process.getInputStream(), GekChars.nativeCharset());
        GekLogger.defaultLogger().info(output);
        Assert.assertEquals(output, ECHO_CONTENT + GekSystem.getLineSeparator());
        process.destroy();
    }

    @Test
    public void testThread() throws InterruptedException {
        Thread thread = GekThread.start("hahaha", () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Assert.assertEquals(thread.getName(), "hahaha");
        Assert.assertFalse(thread.isDaemon());
        thread.join();
        Assert.assertFalse(thread.isAlive());
    }

    @Test
    public void testSystem() {
        GekLogger.defaultLogger().info(GekSystem.getJavaVersion());
        GekLogger.defaultLogger().info(GekSystem.javaMajorVersion());
        GekLogger.defaultLogger().info(GekChars.nativeCharset());
        GekLogger.defaultLogger().info(GekSystem.getOsName());
        GekLogger.defaultLogger().info(GekSystem.isWindows());
        GekLogger.defaultLogger().info(GekSystem.isLinux());
        GekLogger.defaultLogger().info(GekSystem.isBsd());
        GekLogger.defaultLogger().info(GekSystem.isMac());
        GekLogger.defaultLogger().info(GekSystem.isJdk9OrHigher());
    }

    @Test
    public void testEnum() {
        Assert.assertEquals(Te.A, Gek.findEnum(Te.class, 0));
        Assert.assertEquals(Te.B, Gek.findEnum(Te.class, "B", false));
        Assert.assertEquals(Te.C, Gek.findEnum(Te.class, "c", true));
        Assert.assertNull(Gek.findEnum(Te.class, 10));
        Assert.assertNull(Gek.findEnum(Te.class, "d", false));
        Assert.expectThrows(IllegalArgumentException.class, () -> Gek.findEnum(Te.class, -1));
        Assert.expectThrows(IllegalArgumentException.class, () -> Gek.findEnum(Gek.class, -1));
        Assert.expectThrows(IllegalArgumentException.class, () -> Gek.findEnum(Gek.class, "a", true));
    }

    public enum Te {
        A, B, C
    }

    private static final class T1 {
        public static void invoke1() {
            T2.invoke2();
        }
    }

    private static final class T2 {
        public static void invoke2() {
            T3.invoke3();
        }
    }

    private static final class T3 {
        public static void invoke3() {
            StackTraceElement element1 = GekTrace.findCallerStackTrace(T1.class.getName(), "invoke1");
            Assert.assertEquals(element1.getClassName(), GekTest.class.getName());
            Assert.assertEquals(element1.getMethodName(), "testFindCallerStackTrace");
            StackTraceElement element2 = GekTrace.findCallerStackTrace(T2.class.getName(), "invoke2");
            Assert.assertEquals(element2.getClassName(), T1.class.getName());
            Assert.assertEquals(element2.getMethodName(), "invoke1");
            StackTraceElement element3 = GekTrace.findCallerStackTrace(T3.class.getName(), "invoke3");
            Assert.assertEquals(element3.getClassName(), T2.class.getName());
            Assert.assertEquals(element3.getMethodName(), "invoke2");
        }
    }
}
