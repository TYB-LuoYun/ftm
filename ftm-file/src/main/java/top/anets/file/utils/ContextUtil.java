package top.anets.file.utils;

/**
 * @author ftm
 * @date 2023-12-21 18:03
 */

import cn.hutool.core.convert.Convert;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public final class ContextUtil {
    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new ThreadLocal();

    private ContextUtil() {
    }

    public static void putAll(Map<String, String> map) {
        map.forEach((k, v) -> {
            set(k, v);
        });
    }

    public static void set(String key, Object value) {
        Map<String, String> map = getLocalMap();
        map.put(key, value == null ? "" : value.toString());
    }

    public static <T> T get(String key, Class<T> type) {
        Map<String, String> map = getLocalMap();
        return Convert.convert(type, map.get(key));
    }

    public static <T> T get(String key, Class<T> type, Object def) {
        Map<String, String> map = getLocalMap();
        return Convert.convert(type, map.getOrDefault(key, String.valueOf(def == null ? "" : def)));
    }

    public static String get(String key) {
        Map<String, String> map = getLocalMap();
        return (String)map.getOrDefault(key, "");
    }

    public static Map<String, String> getLocalMap() {
        Map<String, String> map = (Map)THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap(10);
            THREAD_LOCAL.set(map);
        }

        return (Map)map;
    }

    public static void setLocalMap(Map<String, String> localMap) {
        THREAD_LOCAL.set(localMap);
    }

    public static Boolean getBoot() {
        return (Boolean)get("boot", Boolean.class, false);
    }

    public static void setBoot(Boolean val) {
        set("boot", val);
    }

    public static Long getUserId() {
        return (Long)get("userid", Long.class, 0L);
    }

    public static String getUserIdStr() {
        return String.valueOf(getUserId());
    }

    public static void setUserId(Long userId) {
        set("userid", userId);
    }

    public static void setUserId(String userId) {
        set("userid", userId);
    }

    public static String getAccount() {
        return (String)get("account", String.class);
    }

    public static void setAccount(String account) {
        set("account", account);
    }

    public static String getName() {
        return (String)get("name", String.class);
    }

    public static void setName(String name) {
        set("name", name);
    }

    public static String getToken() {
        return (String)get("token", String.class);
    }

    public static void setToken(String token) {
        set("token", token);
    }

    public static String getTenant() {
        return (String)get("tenant", String.class, "");
    }

    public static void setTenant(String val) {
        set("tenant", val);
        setTenantBasePoolName(val);
        setTenantExtendPoolName(val);
    }

    public static void clearDatabase() {
        set("database", "");
    }

    public static String getDatabase() {
        return (String)get("database", String.class, "");
    }

    public static void setDatabaseBase() {
        set("database", "lamp_base");
    }

    public static void setDatabaseExtend() {
        set("database", "lamp_extend");
    }

    public static void setTenantBasePoolName(Object tenant) {
        set("lamp_base", tenant);
    }

    public static void setTenantExtendPoolName(Object tenant) {
        set("lamp_extend", tenant);
    }

    public static void setDefTenant() {
        set("lamp_base", "master");
        set("lamp_extend", "master");
    }

    public static boolean isDefTenantId() {
        String base = get("lamp_base");
        if ("master".equals(base)) {
            return true;
        } else {
            String extend = get("lamp_extend");
            return "master".equals(extend);
        }
    }

    public static String getSubTenant() {
        return (String)get("sub_tenant", String.class, "");
    }

    public static void setSubTenant(String val) {
        set("sub_tenant", val);
    }

    public static String getClientId() {
        return (String)get("client_id", String.class);
    }

    public static void setClientId(String val) {
        set("client_id", val);
    }

    public static String getPath() {
        return (String)get("Path", String.class, "");
    }

    public static void setPath(Object path) {
        set("Path", path == null ? "" : path);
    }

    public static String getGrayVersion() {
        return (String)get("gray_version", String.class);
    }

    public static void setGrayVersion(String val) {
        set("gray_version", val);
    }

    public static boolean isEmptyTenant() {
        return isEmptyStr("tenant");
    }

    private static boolean isEmptyLong(String key) {
        String val = (String)getLocalMap().get(key);
        return val == null || "null".equals(val) || "0".equals(val);
    }

    private static boolean isEmptyStr(String key) {
        String val = (String)getLocalMap().get(key);
        return val == null || "null".equals(val);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}

