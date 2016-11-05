package my.beelzik.mobile.scopemvptest.mvp.contract;

import my.beelzik.mobile.scopemvptest.model.Repository;

/**
 * Created by Andrey on 05.11.2016.
 */

public interface MainRepositoryLoadMoreContract {


    interface View extends AbsLoadMoreListContract.View<Repository> {

    }

    interface Presenter extends AbsLoadMoreListContract.Presenter<Repository>, AbsSearcherViewContract.OnSearchListener {

    }
}
