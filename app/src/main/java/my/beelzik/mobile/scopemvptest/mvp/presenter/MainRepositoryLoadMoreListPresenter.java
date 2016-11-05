package my.beelzik.mobile.scopemvptest.mvp.presenter;

import android.text.TextUtils;

import my.beelzik.mobile.scopemvptest.model.Repository;
import my.beelzik.mobile.scopemvptest.mvp.contract.MainRepositoryLoadMoreContract;
import rx.Subscription;

/**
 * Created by Andrey on 23.10.2016.
 */

public class MainRepositoryLoadMoreListPresenter extends AbsLoadMoreListPresenter<Repository, Throwable> implements MainRepositoryLoadMoreContract.Presenter {

    private static final int FIRST_PAGE_NUMBER = 1;
    private Subscription mCurrentSubscription;
    private String mSearch;

    public MainRepositoryLoadMoreListPresenter(OffsetType offsetType, int limit) {
        super(offsetType, FIRST_PAGE_NUMBER, limit);
    }

    @Override
    protected void onLoadMore(int offset, int limit, OffsetType offsetType) {
        mCurrentSubscription = apiService.search(getLoadQuery(), "Repositories", offset, limit)
                .subscribe(searchResult -> {
                    onLoadingSuccess(searchResult.getRepositories());
                }, this::onLoadingFailed);
    }

    @Override
    protected void cancelLoading() {
        if (mCurrentSubscription != null) {
            mCurrentSubscription.unsubscribe();
        }
    }

    private String getLoadQuery() {
        String query = null;

        if (!TextUtils.isEmpty(mSearch)) {
            query = mSearch + " ";
        }

        query = query == null ? "user:JakeWharton" : query + "user:JakeWharton";
        return query;
    }

    @Override
    protected void onLoadingFailed(Throwable failed) {
        super.onLoadingFailed(failed);
        view.showLoadError(failed.getMessage());
    }


    @Override
    public void onSearchQuery(CharSequence query) {
        mSearch = query.toString();
        reload();
    }

    private void reload() {
        if (isViewAttached()) {
            view.fetchItems(null);
        }
        mDataList = null;
        mAllLoaded = false;

        if (mLoading) {
            cancelLoading();
            mLoading = false;
        }
        resetOffset();
        loadMore();
    }

    @Override
    public void onCancelQuery() {
        mSearch = null;
        reload();
    }
}
