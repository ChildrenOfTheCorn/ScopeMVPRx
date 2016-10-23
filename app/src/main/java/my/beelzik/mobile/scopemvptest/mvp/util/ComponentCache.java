package my.beelzik.mobile.scopemvptest.mvp.util;

import java.util.HashMap;

/**
 * Created by Andrey on 23.10.2016.
 */

public class ComponentCache {

    private static HashMap<String, Object> sSubComponentCache;

    static {
        sSubComponentCache = new HashMap<>();
    }

    public static boolean hasComponent(String key) {
        return sSubComponentCache.containsKey(key);
    }

    public static void addComponent(String key, Object component) {
        sSubComponentCache.put(key, component);
    }

    public static void removeComponent(String key) {
        sSubComponentCache.remove(key);
    }

    public static Object getComponent(String key) {
        return sSubComponentCache.get(key);
    }
}
