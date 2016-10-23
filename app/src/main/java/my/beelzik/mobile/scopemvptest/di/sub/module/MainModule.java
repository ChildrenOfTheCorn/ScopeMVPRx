package my.beelzik.mobile.scopemvptest.di.sub.module;

import dagger.Module;
import dagger.Provides;
import my.beelzik.mobile.scopemvptest.di.ActivityScope;
import my.beelzik.mobile.scopemvptest.model.Repository;
import my.beelzik.mobile.scopemvptest.mvp.contract.AbsLoadMoreListContract;
import my.beelzik.mobile.scopemvptest.mvp.presenter.AbsLoadMoreListPresenter;
import my.beelzik.mobile.scopemvptest.mvp.presenter.MainRepositoryLoadMoreListPresenter;

/**
 * Created by Andrey on 23.10.2016.
 */
@Module
public class MainModule {

    public static final int MAIN_PAGE_ITEMS_COUNT = 30;

    @ActivityScope
    @Provides
    public AbsLoadMoreListContract.Presenter<Repository> provideMainRepositoryListPresenter() {
        return new MainRepositoryLoadMoreListPresenter(AbsLoadMoreListPresenter.OffsetType.PAGE, MAIN_PAGE_ITEMS_COUNT);
    }

}
