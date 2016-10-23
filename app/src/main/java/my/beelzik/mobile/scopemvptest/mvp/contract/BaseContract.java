package my.beelzik.mobile.scopemvptest.mvp.contract;

/**
 * Created by Andrey on 23.10.2016.
 */

public interface BaseContract {

    interface MvpPresenter<T> {

        void attachView(T view);

        void detachView();

        boolean isViewAttached();
    }
}
