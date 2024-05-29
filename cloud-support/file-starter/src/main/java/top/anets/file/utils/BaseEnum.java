package top.anets.file.utils;


/**
 * @author ftm
 * @date 2023-12-21 16:55
 */
public interface BaseEnum  {
    default String getCode() {
        return this.toString();
    }

    String getDesc();

    default String getExtra() {
        return "";
    }

    default boolean eq(String val) {
        return this.getCode().equalsIgnoreCase(val);
    }

    default String getValue() {
        return this.getCode();
    }
}

