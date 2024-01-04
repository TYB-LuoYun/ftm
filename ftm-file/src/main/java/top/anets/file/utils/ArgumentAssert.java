package top.anets.file.utils;

/**
 * @author ftm
 * @date 2023-12-21 16:36
 */

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
public class ArgumentAssert  {
    public ArgumentAssert() {
    }

    public static <X extends Throwable> void isTrue(boolean expression, Supplier<? extends X> supplier) throws X {
        if (!expression) {
            throw supplier.get();
        }
    }

    public static void isTrue(boolean expression, String errorMsgTemplate, Object... params) throws ArgumentException {
        isTrue(expression, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static void isTrue(boolean expression) throws ArgumentException {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static <X extends Throwable> void isFalse(boolean expression, Supplier<X> errorSupplier) throws X {
        if (expression) {
            throw errorSupplier.get();
        }
    }

    public static void isFalse(boolean expression, String errorMsgTemplate, Object... params) throws ArgumentException {
        isFalse(expression, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static void isFalse(boolean expression) throws ArgumentException {
        isFalse(expression, "[Assertion failed] - this expression must be false");
    }

    public static <X extends Throwable> void isNull(Object object, Supplier<X> errorSupplier) throws X {
        if (null != object) {
            throw errorSupplier.get();
        }
    }

    public static void isNull(Object object, String errorMsgTemplate, Object... params) throws ArgumentException {
        isNull(object, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static void isNull(Object object) throws ArgumentException {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static <T, X extends Throwable> T notNull(T object, Supplier<X> errorSupplier) throws X {
        if (null == object) {
            throw errorSupplier.get();
        } else {
            return object;
        }
    }

    public static <T> T notNull(T object, String errorMsgTemplate, Object... params) throws ArgumentException {
        return notNull(object, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static <T> T notNull(T object) throws ArgumentException {
        return notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static <T extends CharSequence, X extends Throwable> T notEmpty(T text, Supplier<X> errorSupplier) throws X {
        if (StrUtil.isEmpty(text)) {
            throw errorSupplier.get();
        } else {
            return text;
        }
    }

    public static <T extends CharSequence> T notEmpty(T text, String errorMsgTemplate, Object... params) throws ArgumentException {
        return notEmpty(text, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static <T extends CharSequence> T notEmpty(T text) throws ArgumentException {
        return notEmpty(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    public static <T extends CharSequence, X extends Throwable> T notBlank(T text, Supplier<X> errorMsgSupplier) throws X {
        if (StrUtil.isBlank(text)) {
            throw  errorMsgSupplier.get();
        } else {
            return text;
        }
    }

    public static <T extends CharSequence> T notBlank(T text, String errorMsgTemplate, Object... params) throws ArgumentException {
        return notBlank(text, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static <T extends CharSequence> T notBlank(T text) throws ArgumentException {
        return notBlank(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
    }

    public static <T extends CharSequence, X extends Throwable> T notContain(CharSequence textToSearch, T substring, Supplier<X> errorSupplier) throws X {
        if (StrUtil.contains(textToSearch, substring)) {
            throw errorSupplier.get();
        } else {
            return substring;
        }
    }

    public static String notContain(String textToSearch, String substring, String errorMsgTemplate, Object... params) throws ArgumentException {
        return (String)notContain((CharSequence)textToSearch, (CharSequence)substring, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static String notContain(String textToSearch, String substring) throws ArgumentException {
        return notContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [{}]", substring);
    }

    public static <T> T contain(Collection<T> collection, T item) throws ArgumentException {
        return contain(collection, item, "[断言失败] - 此集合中参数不得包含该值 [{}]", item);
    }

    public static <T> T contain(Collection<T> collection, T item, String errorMsgTemplate, Object... params) throws ArgumentException {
        return contain(collection, item, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static <T, X extends Throwable> T contain(Collection<T> collection, T item, Supplier<X> errorSupplier) throws X {
        if (!CollUtil.contains(collection, item)) {
            throw errorSupplier.get();
        } else {
            return item;
        }
    }

    public static <T> T notContain(Collection<T> collection, T item) throws ArgumentException {
        return notContain(collection, item, "[断言失败] - 此集合中参数不得包含该值 [{}]", item);
    }

    public static <T> T notContain(Collection<T> collection, T item, String errorMsgTemplate, Object... params) throws ArgumentException {
        return notContain(collection, item, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static <T, X extends Throwable> T notContain(Collection<T> collection, T item, Supplier<X> errorSupplier) throws X {
        if (CollUtil.contains(collection, item)) {
            throw errorSupplier.get();
        } else {
            return item;
        }
    }

    public static <T, X extends Throwable> T[] notEmpty(T[] array, Supplier<X> errorSupplier) throws X {
        if (ArrayUtil.isEmpty(array)) {
            throw errorSupplier.get();
        } else {
            return array;
        }
    }

    public static <T> T[] notEmpty(T[] array, String errorMsgTemplate, Object... params) throws ArgumentException {
        return notEmpty(array, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static <T> T[] notEmpty(T[] array) throws ArgumentException {
        return notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
    }

    public static <T, X extends Throwable> T[] noNullElements(T[] array, Supplier<X> errorSupplier) throws X {
        if (ArrayUtil.hasNull(array)) {
            throw errorSupplier.get();
        } else {
            return array;
        }
    }

    public static <T> T[] noNullElements(T[] array, String errorMsgTemplate, Object... params) throws ArgumentException {
        return noNullElements(array, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static <T> T[] noNullElements(T[] array) throws ArgumentException {
        return noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
    }

    public static <E, T extends Iterable<E>, X extends Throwable> T notEmpty(T collection, Supplier<X> errorSupplier) throws X {
        if (CollUtil.isEmpty(collection)) {
            throw errorSupplier.get();
        } else {
            return collection;
        }
    }

    public static <E, T extends Iterable<E>> T notEmpty(T collection, String errorMsgTemplate, Object... params) throws ArgumentException {
        return notEmpty(collection, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static <E, T extends Iterable<E>> T notEmpty(T collection) throws ArgumentException {
        return notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    public static <K, V, T extends Map<K, V>, X extends Throwable> T notEmpty(T map, Supplier<X> errorSupplier) throws X {
        if (MapUtil.isEmpty(map)) {
            throw errorSupplier.get();
        } else {
            return map;
        }
    }

    public static <K, V, T extends Map<K, V>> T notEmpty(T map, String errorMsgTemplate, Object... params) throws ArgumentException {
        return notEmpty(map, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static <K, V, T extends Map<K, V>> T notEmpty(T map) throws ArgumentException {
        return notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }

    public static <T> T isInstanceOf(Class<?> type, T obj) {
        return isInstanceOf(type, obj, "Object [{}] is not instanceof [{}]", obj, type);
    }

    public static <T> T isInstanceOf(Class<?> type, T obj, String errorMsgTemplate, Object... params) throws ArgumentException {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        } else {
            return obj;
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType) throws ArgumentException {
        isAssignable(superType, subType, "{} is not assignable to {})", subType, superType);
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String errorMsgTemplate, Object... params) throws ArgumentException {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        }
    }

    public static void state(boolean expression, Supplier<String> errorMsgSupplier) throws IllegalStateException {
        if (!expression) {
            throw new IllegalStateException((String)errorMsgSupplier.get());
        }
    }

    public static void state(boolean expression, String errorMsgTemplate, Object... params) throws IllegalStateException {
        if (!expression) {
            throw new IllegalStateException(StrUtil.format(errorMsgTemplate, params));
        }
    }

    public static void state(boolean expression) throws IllegalStateException {
        state(expression, "[Assertion failed] - this state invariant must be true");
    }

    public static int checkIndex(int index, int size) throws ArgumentException, IndexOutOfBoundsException {
        return checkIndex(index, size, "[Assertion failed]");
    }

    public static int checkIndex(int index, int size, String errorMsgTemplate, Object... params) throws ArgumentException, IndexOutOfBoundsException {
        if (index >= 0 && index < size) {
            return index;
        } else {
            throw new IndexOutOfBoundsException(badIndexMsg(index, size, errorMsgTemplate, params));
        }
    }

    public static int checkBetween(int value, int min, int max) {
        if (value >= min && value <= max) {
            return value;
        } else {
            throw new ArgumentException(StrUtil.format("Length must be between {} and {}.", new Object[]{min, max}));
        }
    }

    public static long checkBetween(long value, long min, long max) {
        if (value >= min && value <= max) {
            return value;
        } else {
            throw new ArgumentException(StrUtil.format("Length must be between {} and {}.", new Object[]{min, max}));
        }
    }

    public static double checkBetween(double value, double min, double max) {
        if (value >= min && value <= max) {
            return value;
        } else {
            throw new ArgumentException(StrUtil.format("Length must be between {} and {}.", new Object[]{min, max}));
        }
    }

    public static Number checkBetween(Number value, Number min, Number max) {
        notNull(value);
        notNull(min);
        notNull(max);
        double valueDouble = value.doubleValue();
        double minDouble = min.doubleValue();
        double maxDouble = max.doubleValue();
        if (valueDouble >= minDouble && valueDouble <= maxDouble) {
            return value;
        } else {
            throw new ArgumentException(StrUtil.format("Length must be between {} and {}.", new Object[]{min, max}));
        }
    }

    public static void equals(Object expected, Object actual, String errorMsgTemplate, Object... params) {
        equals(expected, actual, () -> {
            return new ArgumentException(StrUtil.format(errorMsgTemplate, params));
        });
    }

    public static <X extends Throwable> void equals(Object expected, Object actual, Supplier<? extends X> supplier) throws X {
        if (!ObjectUtil.equals(expected, actual)) {
            throw supplier.get();
        }
    }

    private static String badIndexMsg(int index, int size, String desc, Object... params) {
        if (index < 0) {
            return StrUtil.format("{} ({}) must not be negative", new Object[]{StrUtil.format(desc, params), index});
        } else if (size < 0) {
            throw new ArgumentException("negative size: " + size);
        } else {
            return StrUtil.format("{} ({}) must be less than size ({})", new Object[]{StrUtil.format(desc, params), index, size});
        }
    }
}