package top.anets.file.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * @author ftm
 * @date 2023-12-21 16:53
 */
public final class CollHelper {
    private CollHelper() {
    }

    public static Map<String, Set<String>> putAll(Map... items) {
        if (ArrayUtil.isEmpty(items)) {
            return Collections.emptyMap();
        } else {
            Map<String, Set<String>> map = new HashMap();
            Map[] var2 = items;
            int var3 = items.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Map<String, Set<String>> item = var2[var4];
                item.forEach((k, v) -> {
                    if (map.containsKey(k)) {
                        Set<String> list = (Set)map.get(k);
                        if (list == null) {
                            list = new HashSet();
                        }

                        ((Set)list).addAll(v);
                    } else {
                        map.put(k, new HashSet(v));
                    }

                });
            }

            return map;
        }
    }

    public static Map<String, String> getMap(BaseEnum[] list) {
        return uniqueIndex(Arrays.asList(list), BaseEnum::getCode, BaseEnum::getDesc);
    }

    public static <K, V, M> ImmutableMap<K, M> uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction, Function<? super V, M> valueFunction) {
        Iterator<V> iterator = values.iterator();
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        ImmutableMap.Builder builder = ImmutableMap.builder();

        while(iterator.hasNext()) {
            V value = iterator.next();
            builder.put(keyFunction.apply(value), valueFunction.apply(value));
        }

        try {
            return builder.build();
        } catch (IllegalArgumentException var6) {
            throw new IllegalArgumentException(var6.getMessage() + ".若要在键下索引多个值，请使用: Multimaps.index.", var6);
        }
    }

    public static <K, V, M> Multimap<K, M> iterableToMultiMap(Iterable<V> values, Function<? super V, K> keyFunction, Function<? super V, M> valueFunction) {
        Iterator<V> iterator = values.iterator();
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        ArrayListMultimap builder = ArrayListMultimap.create();

        while(iterator.hasNext()) {
            V value = iterator.next();
            builder.put(keyFunction.apply(value), valueFunction.apply(value));
        }

        try {
            return builder;
        } catch (IllegalArgumentException var6) {
            throw new IllegalArgumentException(var6.getMessage() + ".若要在键下索引多个值，请使用: Multimaps.index.", var6);
        }
    }

    public static <K, V> Map<V, K> inverse(Map<K, V> map) {
        if (MapUtil.isEmpty(map)) {
            return Collections.emptyMap();
        } else {
            HashBiMap<K, V> biMap = HashBiMap.create();
            map.forEach(biMap::forcePut);
            return biMap.inverse();
        }
    }

    public static int initialCapacity(int size, float loadFactor) {
        return (int)((float)size / loadFactor + 1.0F);
    }

    public static int initialCapacity(int size) {
        return initialCapacity(size, 0.75F);
    }

    public static <T> List<String> split(Collection<T> list, Function<? super T, ?> function, CharSequence separator) {
        return (List)(CollUtil.isEmpty(list) ? new ArrayList() : (List)list.parallelStream().map(function).map((item) -> {
            return StrUtil.splitToArray(String.valueOf(item), separator);
        }).flatMap(Arrays::stream).filter(ObjectUtil::isNotEmpty).distinct().collect(Collectors.toList()));
    }

    public static <T> List<String> split(Collection<String> list, CharSequence separator) {
        return (List)(CollUtil.isEmpty(list) ? new ArrayList() : (List)list.parallelStream().map((item) -> {
            return StrUtil.splitToArray(item, separator);
        }).flatMap(Arrays::stream).filter(ObjectUtil::isNotEmpty).distinct().collect(Collectors.toList()));
    }

    public static <E> List<E> asList(E... elements) {
        if (elements != null && elements.length != 0) {
            int capacity = computeListCapacity(elements.length);
            ArrayList<E> list = new ArrayList(capacity);
            Collections.addAll(list, elements);
            return list;
        } else {
            return Collections.emptyList();
        }
    }

    public static <E> Set<E> asSet(E... elements) {
        if (elements != null && elements.length != 0) {
            LinkedHashSet<E> set = new LinkedHashSet(elements.length * 4 / 3 + 1);
            Collections.addAll(set, elements);
            return set;
        } else {
            return Collections.emptySet();
        }
    }

    public static int computeListCapacity(int arraySize) {
        return (int)Math.min(5L + (long)arraySize + (long)(arraySize / 10), 2147483647L);
    }

    public static <T> List<T> addAll(List... values) {
        return (List) Stream.of(values).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static <T> List<T> addAllUnique(List... values) {
        return (List)Stream.of(values).flatMap(Collection::stream).distinct().collect(Collectors.toList());
    }

    public static <T> Set<T> addAll(Set... values) {
        return (Set)Stream.of(values).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public static <T> Set<T> addAllListToSet(List... values) {
        return (Set)Stream.of(values).flatMap(Collection::stream).collect(Collectors.toSet());
    }
}

