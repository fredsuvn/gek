package xyz.srclab.common.collection;

import com.google.common.collect.ImmutableList;
import xyz.srclab.annotation.Immutable;

import java.util.*;
import java.util.function.Function;

public class ListHelper {

    @SafeVarargs
    @Immutable
    public static <E> List<E> concat(Iterable<? extends E>... iterables) {
        return concat(Arrays.asList(iterables));
    }

    @Immutable
    public static <E> List<E> concat(Iterable<Iterable<? extends E>> iterables) {
        List<E> result = new LinkedList<>();
        for (Iterable<? extends E> iterable : iterables) {
            result.addAll(IterableHelper.asList(iterable));
        }
        return immutable(result);
    }

    @Immutable
    public static <E> List<E> immutable(Iterable<? extends E> elements) {
        return ImmutableList.copyOf(elements);
    }

    @SafeVarargs
    @Immutable
    public static <E> List<E> immutable(E... elements) {
        return ImmutableList.copyOf(elements);
    }

    @Immutable
    public static <NE, OE> List<NE> map(OE[] array, Function<OE, NE> mapper) {
        List<NE> result = new ArrayList<>(array.length);
        for (OE o : array) {
            result.add(mapper.apply(o));
        }
        return immutable(result);
    }

    @Immutable
    public static <NE, OE> List<NE> map(Iterable<? extends OE> iterable, Function<OE, NE> mapper) {
        List<NE> result = new LinkedList<>();
        for (OE o : iterable) {
            result.add(mapper.apply(o));
        }
        return immutable(result);
    }

    public static boolean deepEquals(List<?> list1, List<?> list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        Iterator<?> iterator1 = list1.iterator();
        Iterator<?> iterator2 = list2.iterator();
        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                return false;
            }
            Object o1 = iterator1.next();
            Object o2 = iterator2.next();
            if (o1 instanceof List && o2 instanceof List) {
                if (deepEquals((List<?>) o1, (List<?>) o2)) {
                    continue;
                } else {
                    return false;
                }
            }
            if (o1 instanceof List || o2 instanceof List) {
                return false;
            }
            if (!Objects.deepEquals(o1, o2)) {
                return false;
            }
        }
        return true;
    }
}