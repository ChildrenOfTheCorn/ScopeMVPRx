package my.beelzik.mobile.scopemvptest.mvp.contract;

import java.util.List;

import my.beelzik.mobile.scopemvptest.mvp.util.StateSavable;

/**
 * Created by Andrey on 11.09.2016.
 */
public interface AbsLoadMoreListContract {

    interface View<T> extends StateSavable {

        void showRefreshing(boolean progress);

        void showLoadMoreProgress(boolean progress);

        void showContentProgress(boolean progress);

        void showContentList(boolean show);

        void fetchItems(List<T> list);

        void showEmpty(boolean show);

        void showLoadError(CharSequence message);

    }

    interface Presenter<T> extends BaseContract.MvpPresenter<View<T>> {

        void loadMore();

        void refresh();

    }


}
