package my.beelzik.mobile.scopemvptest.mvp.presenter;

import my.beelzik.mobile.scopemvptest.model.Repository;
import rx.Subscription;

/**
 * Created by Andrey on 23.10.2016.
 */

public class MainRepositoryLoadMoreListPresenter extends AbsLoadMoreListPresenter<Repository, Throwable> {

    private Subscription mCurrentSubscription;

    public MainRepositoryLoadMoreListPresenter(OffsetType offsetType, int limit) {
        super(offsetType, limit);
    }

    @Override
    protected void onLoadMore(int offset, int limit, OffsetType offsetType, boolean refreshing) {

        if (mCurrentSubscription != null && refreshing) {
            mCurrentSubscription.unsubscribe();
        }
        mCurrentSubscription = apiService.getUserRepos("JakeWharton", offset, limit)
                .subscribe(this::onLoadingSuccess, this::onLoadingFailed);
    }

    @Override
    protected void onLoadingFailed(Throwable failed) {
        super.onLoadingFailed(failed);
        view.showLoadError(failed.getMessage());
    }
}
