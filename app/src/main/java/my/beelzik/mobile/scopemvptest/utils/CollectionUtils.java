package my.beelzik.mobile.scopemvptest.utils;

import java.util.Collection;

/**
 * Created by Andrey on 13.09.2016.
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }
}
