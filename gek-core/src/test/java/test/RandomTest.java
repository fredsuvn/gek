package test;

import org.testng.Assert;
import org.testng.annotations.Test;
import xyz.fsgek.common.base.FsLogger;
import xyz.fsgek.common.base.FsRandom;

public class RandomTest {

    @Test
    public void testRandom() {
        FsRandom<String> fsRandom = FsRandom.newBuilder()
            .score(20, "A")
            .score(20, "B")
            .score(60, () -> "C")
            .build();
        int countA = 0;
        int countB = 0;
        int countC = 0;
        for (int i = 0; i < 1000; i++) {
            Object result = fsRandom.next();
            if (result.equals("A")) {
                countA++;
            } else if (result.equals("B")) {
                countB++;
            } else if (result.equals("C")) {
                countC++;
            }
        }
        int total = countA + countB + countC;
        Assert.assertEquals(total, 1000);
        FsLogger.defaultLogger().info("countA: ", countA, " countB: ", countB, ", countC: ", countC, ", total: ", total);

        String randomStr = String.join("", fsRandom.nextList(100));
        FsLogger.defaultLogger().info("randomStr: ", randomStr);
        Assert.assertEquals(randomStr.length(), 100);
    }
}