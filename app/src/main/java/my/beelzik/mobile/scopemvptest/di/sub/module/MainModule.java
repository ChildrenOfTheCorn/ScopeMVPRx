package my.beelzik.mobile.scopemvptest.di.sub.module;

import dagger.Module;
import dagger.Provides;
import my.beelzik.mobile.scopemvptest.di.ActivityScope;
import my.beelzik.mobile.scopemvptest.mvp.contract.AbsSearcherViewContract;
import my.beelzik.mobile.scopemvptest.mvp.contract.MainRepositoryLoadMoreContract;
import my.beelzik.mobile.scopemvptest.mvp.presenter.AbsLoadMoreListPresenter;
import my.beelzik.mobile.scopemvptest.mvp.presenter.MainRepositoryLoadMoreListPresenter;
import my.beelzik.mobile.scopemvptest.mvp.presenter.SearchPresenter;

/**
 * Created by Andrey on 23.10.2016.
 */
@Module
public class MainModule {

    public static final int MAIN_PAGE_ITEMS_COUNT = 30;

    @ActivityScope
    @Provides
    public MainRepositoryLoadMoreContract.Presenter provideMainRepositoryListPresenter() {
        return new MainRepositoryLoadMoreListPresenter(AbsLoadMoreListPresenter.OffsetType.PAGE, MAIN_PAGE_ITEMS_COUNT);
    }

    @ActivityScope
    @Provides
    public AbsSearcherViewContract.Presenter provideMainSearchPresenter(MainRepositoryLoadMoreContract.Presenter loadMorePresenter) {
        return new SearchPresenter(loadMorePresenter);
    }

}
