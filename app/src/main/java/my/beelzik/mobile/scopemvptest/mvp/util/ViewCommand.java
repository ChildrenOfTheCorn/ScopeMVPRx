package my.beelzik.mobile.scopemvptest.mvp.util;

/**
 * Created by Andrey on 05.11.2016.
 */

public interface ViewCommand<V> {

    void emmit(V view);
}
