package test;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TestUtil {

    private static final char[] CHARS = ("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM" +
        "!@#$%^&*()_+{}:\"?><|~!@#$%^&*()-=[];',./`").toCharArray();
    private static final char[] CHARS_CN = ("啥奖励返回连接暗示暗示福建烤老鼠理发卡思考复刻撒回复iwuwueiwio打算法法师净空法师看接口").toCharArray();

    public static String buildRandomString(int enSize, int cnSize) {
        StringBuilder sb = new StringBuilder(enSize + cnSize);
        Random random = new Random();
        for (int i = 0; i < enSize; i++) {
            sb.append(CHARS[Math.abs(random.nextInt()) % CHARS.length]);
        }
        for (int i = 0; i < cnSize; i++) {
            sb.append(CHARS_CN[Math.abs(random.nextInt()) % CHARS_CN.length]);
        }
        return sb.toString();
    }

    public static byte[] buildRandomBytes(int size) {
        long now = System.currentTimeMillis();
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) ((now * (i + 888) / 999) % 64);
        }
        return bytes;
    }

    public static void count(String key, Map<String, AtomicInteger> map) {
        AtomicInteger c = map.computeIfAbsent(key, k -> new AtomicInteger(0));
        c.incrementAndGet();
    }
}
