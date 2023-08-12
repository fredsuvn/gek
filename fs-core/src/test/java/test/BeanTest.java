package test;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.testng.Assert;
import org.testng.annotations.Test;
import xyz.srclab.common.base.Fs;
import xyz.srclab.common.base.FsLogger;
import xyz.srclab.common.bean.FsBean;
import xyz.srclab.common.bean.FsBeanResolver;
import xyz.srclab.common.bean.FsProperty;
import xyz.srclab.common.bean.handlers.DefaultBeanResolveHandler;
import xyz.srclab.common.reflect.TypeRef;

import java.lang.annotation.*;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanTest {

    @Test
    public void testTypeBean() throws Exception {
        Type ccType = new TypeRef<Cc<Double>>() {
        }.getType();
        FsBean ccBean = Fs.resolveBean(ccType);
        FsLogger.system().info("ccBean: ", ccBean);
        FsProperty cc = ccBean.getProperty("cc");
        FsProperty c1 = ccBean.getProperty("c1");
        FsProperty c2 = ccBean.getProperty("c2");
        FsProperty i1 = ccBean.getProperty("i1");
        FsProperty i2 = ccBean.getProperty("i2");
        Assert.assertEquals(cc.getType(), Double.class);
        Assert.assertEquals(c2.getType(), Long.class);
        Assert.assertEquals(i1.getType(), String.class);
        Assert.assertEquals(i2.getType(), Integer.class);
        Assert.assertNull(c1);
        Assert.assertEquals(
            c2.getFieldAnnotations().stream().map(Annotation::toString).collect(Collectors.toList()),
            Arrays.asList(C2.class.getDeclaredField("c2").getAnnotations()[0].toString())
        );
        Assert.assertEquals(
            c2.getGetterAnnotations().stream().map(Annotation::toString).collect(Collectors.toList()),
            Arrays.asList(C2.class.getMethod("getC2").getAnnotations()[0].toString())
        );
        Assert.assertEquals(
            c2.getSetterAnnotations().stream().map(Annotation::toString).collect(Collectors.toList()),
            Arrays.asList(C2.class.getMethod("setC2", Object.class).getAnnotations()[0].toString())
        );
        Assert.assertEquals(
            c2.getAnnotations().stream().map(Annotation::toString).collect(Collectors.toList()),
            Arrays.asList(
                C2.class.getMethod("getC2").getAnnotations()[0].toString(),
                C2.class.getMethod("setC2", Object.class).getAnnotations()[0].toString(),
                C2.class.getDeclaredField("c2").getAnnotations()[0].toString()
            ));
    }

    @Test
    public void testClassBean() throws Exception {
        Type ccType = Cc.class;
        FsBean ccBean = Fs.resolveBean(ccType);
        FsLogger.system().info("ccBean: ", ccBean);
        FsProperty cc = ccBean.getProperty("cc");
        FsProperty c1 = ccBean.getProperty("c1");
        FsProperty c2 = ccBean.getProperty("c2");
        FsProperty i1 = ccBean.getProperty("i1");
        FsProperty i2 = ccBean.getProperty("i2");
        Assert.assertEquals(cc.getType().toString(), "T");
        Assert.assertEquals(c2.getType(), Long.class);
        Assert.assertEquals(i1.getType(), String.class);
        Assert.assertEquals(i2.getType(), Integer.class);
        Assert.assertNull(c1);
        Assert.assertEquals(
            c2.getFieldAnnotations().stream().map(Annotation::toString).collect(Collectors.toList()),
            Arrays.asList(C2.class.getDeclaredField("c2").getAnnotations()[0].toString())
        );
        Assert.assertEquals(
            c2.getGetterAnnotations().stream().map(Annotation::toString).collect(Collectors.toList()),
            Arrays.asList(C2.class.getMethod("getC2").getAnnotations()[0].toString())
        );
        Assert.assertEquals(
            c2.getSetterAnnotations().stream().map(Annotation::toString).collect(Collectors.toList()),
            Arrays.asList(C2.class.getMethod("setC2", Object.class).getAnnotations()[0].toString())
        );
        Assert.assertEquals(
            c2.getAnnotations().stream().map(Annotation::toString).collect(Collectors.toList()),
            Arrays.asList(
                C2.class.getMethod("getC2").getAnnotations()[0].toString(),
                C2.class.getMethod("setC2", Object.class).getAnnotations()[0].toString(),
                C2.class.getDeclaredField("c2").getAnnotations()[0].toString()
            ));
    }

    @Test
    public void testMapBean() {
        Type mapType = new TypeRef<Map<String, Long>>() {
        }.getType();
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("1", 10086L);
        map.put("2", 10010L);
        map.put("3", 10000L);
        FsBean mapBean = Fs.wrapBean(map, mapType);
        FsLogger.system().info("mapBean: ", mapBean);
        FsProperty p1 = mapBean.getProperty("1");
        FsProperty p2 = mapBean.getProperty("2");
        FsProperty p3 = mapBean.getProperty("3");
        FsProperty p4 = mapBean.getProperty("4");
        Assert.assertEquals(p1.getType(), Long.class);
        Assert.assertEquals(p2.getType(), Long.class);
        Assert.assertEquals(p3.getType(), Long.class);
        Assert.assertNull(p4);
        Map<String, FsProperty> properties = mapBean.getProperties();
        Assert.assertSame(properties, mapBean.getProperties());
        map.put("4", 12345L);
        Assert.assertNotEquals(properties, mapBean.getProperties());
        Assert.assertNull(p4);
        FsProperty p42 = mapBean.getProperty("4");
        Assert.assertEquals(p42.getType(), Long.class);
        Assert.assertSame(p1, mapBean.getProperties().get("1"));
        Assert.assertSame(p1, mapBean.getProperty("1"));
        map.remove("2");
        Assert.assertNull(mapBean.getProperty("2"));
        FsLogger.system().info("mapBean: ", mapBean);

        FsBean mapObjBean = Fs.wrapBean(map);
        FsProperty p1Obj = mapObjBean.getProperty("1");
        Assert.assertEquals(p1Obj.getType(), Object.class);
        Assert.assertEquals(
            p1.get(map),
            p1Obj.get(map)
        );

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            Fs.wrapBean(map, new TypeRef<Map<Object, Long>>() {
            }.getType());
        });
    }

    @Test
    public void testBeanResolver() {
        Type ccType = new TypeRef<Cc<Double>>() {
        }.getType();
        FsBean ccBean1 = Fs.resolveBean(ccType);
        FsBean ccBean2 = Fs.resolveBean(ccType);
        Assert.assertSame(ccBean1, ccBean2);
        FsBeanResolver resolver = FsBeanResolver.newResolver(
            Collections.singletonList(new DefaultBeanResolveHandler()),
            false
        );
        FsBean ccBean3 = resolver.resolve(ccType);
        Assert.assertNotSame(ccBean1, ccBean3);
        Assert.assertEquals(ccBean1, ccBean3);
        FsBean ccBean4 = resolver.resolve(ccType);
        Assert.assertNotSame(ccBean4, ccBean3);
        Assert.assertEquals(ccBean4, ccBean3);
    }

    @Test
    public void testCopyProperties() {
        Cc<Long> cc1 = new Cc<>();
        cc1.setI1("i1");
        cc1.setI2(2);
        cc1.setC2(22L);
        cc1.setCc(33L);
        Cc<Long> cc2 = Fs.copyProperties(cc1, new Cc<>());
        Assert.assertEquals(cc2, cc1);
        cc1.setI1(null);
        cc2.setI1("888");
        Fs.copyProperties(cc1, cc2);
        Assert.assertEquals(cc2, cc1);
        Assert.assertEquals(cc2.getI1(), cc1.getI1());
        Assert.assertNull(cc2.getI1());
        cc2.setI1("888");
        Fs.copyProperties(cc1, cc2, false);
        Assert.assertEquals("888", cc2.getI1());
        cc1.setI1("aaaa");
        cc2 = Fs.copyProperties(cc1, new Cc<>(), "c2");
        Assert.assertEquals(cc1.getI1(), cc2.getI1());
        Assert.assertEquals(cc1.getI2(), cc2.getI2());
        Assert.assertEquals(cc1.getCc(), cc2.getCc());
        Assert.assertNull(cc2.getC2());
        Assert.assertEquals(cc1.getC2().longValue(), 22);
        cc1.setI1(null);
        cc2 = Fs.copyProperties(cc1, new Cc<>(), false, "c2");
        cc2.setI1("qqqq");
        Fs.copyProperties(cc1, new Cc<>(), false, "c2");
        Assert.assertEquals("qqqq", cc2.getI1());
        Assert.assertEquals(cc1.getI2(), cc2.getI2());
        Assert.assertEquals(cc1.getCc(), cc2.getCc());
        Assert.assertNull(cc2.getC2());
        Assert.assertEquals(cc1.getC2().longValue(), 22);
        Assert.expectThrows(ClassCastException.class, () -> {
            Cc<Long> ccl = new Cc<>();
            Fs.copyProperties(cc1, new TypeRef<Cc<Double>>() {
                }.getType(),
                ccl, new TypeRef<Cc<String>>() {
                }.getType());
            Long l = ccl.getCc();
            System.out.println(l);
        });
        Cc<String> ccs = Fs.copyProperties(cc1, new TypeRef<Cc<Double>>() {
            }.getType(),
            new Cc<>(), new TypeRef<Cc<String>>() {
            }.getType());
        Assert.assertEquals(ccs.getCc(), cc1.getCc().toString());
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Cc<T> extends C2<Long> implements I1, I2<Integer> {

        private String i1;
        private T cc;
        private Integer i2;

        @Override
        public String getI1() {
            return i1;
        }

        @Override
        public void setI1(String i1) {
            this.i1 = i1;
        }
    }

    @EqualsAndHashCode
    public static class C2<T> {

        @Ann("c2")
        private T c2;

        @Ann("getC2")
        public T getC2() {
            return c2;
        }

        @Ann("setC2")
        public void setC2(T c2) {
            this.c2 = c2;
        }
    }

    @Data
    public static class C1 {
        private String c1;
    }

    public interface I2<T> {

        T getI2();

        void setI2(T t);
    }

    public interface I1 {

        String getI1();

        void setI1(String i1);
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface Ann {
        String value();
    }
}
