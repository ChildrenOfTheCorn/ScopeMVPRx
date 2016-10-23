package my.beelzik.mobile.scopemvptest.mvp.presenter;

import java.util.List;

import my.beelzik.mobile.scopemvptest.mvp.contract.AbsLoadMoreListContract;
import my.beelzik.mobile.scopemvptest.utils.CollectionUtils;


/**
 * Created by Andrey on 11.09.2016.
 */
public abstract class AbsLoadMoreListPresenter<T, F> extends BasePresenter<AbsLoadMoreListContract.View<T>> implements AbsLoadMoreListContract.Presenter<T> {

    private static final String TAG = "AbsLoadMoreListPresenter";


    public enum OffsetType {
        LIMITED,
        PAGE
    }

    private OffsetType mOffsetType = OffsetType.PAGE;
    private int mOffset = 0;
    private int mLimit = 10;

    private List<T> mDataList;

    boolean mRefreshing = false;
    boolean mLoading = false;

    boolean mAllLoaded = false;


    public AbsLoadMoreListPresenter(OffsetType offsetType, int limit) {
        super();
        mOffsetType = offsetType;
        mLimit = limit;
    }

    @Override
    public void attachView(AbsLoadMoreListContract.View<T> view) {
        super.attachView(view);

        if (isViewAttached()) {
            view.fetchItems(mDataList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CollectionUtils.isEmpty(mDataList)) {
            loadMore();
        }
    }

    @Override
    public void loadMore() {
        if (!mAllLoaded && !mLoading) {
            mLoading = true;
            showInProgress();
            onLoadMore(mOffset, mLimit, mOffsetType, mRefreshing);
        }
    }

    protected abstract void onLoadMore(int offset, int limit, OffsetType offsetType, boolean refreshing);


    protected void onLoadingSuccess(List<T> list) {

        if (CollectionUtils.isEmpty(list) || mLimit > list.size()) {
            mAllLoaded = true;
        } else {
            updateOffset(list);
        }

        if (mRefreshing) {
            mDataList = null;
            mRefreshing = false;
        }
        if (mDataList == null) {
            mDataList = list;
        } else {
            mDataList.addAll(list);
        }
        view.fetchItems(mDataList);
        cancelProgress();
        updateContent();
        mLoading = false;
    }

    protected void onLoadingFailed(F failed) {
        if (mRefreshing) {
            mRefreshing = false;
        }
        cancelProgress();
        updateContent();
        mLoading = false;

    }

    private void updateContent() {
        view.fetchItems(mDataList);
        updateContentView();
    }

    private void cancelProgress() {
        view.showRefreshing(false);
        view.showLoadMoreProgress(false);
        view.showContentProgress(false);
    }

    private void showInProgress() {

        if (mRefreshing) {
            view.showRefreshing(true);
        } else if (CollectionUtils.isEmpty(mDataList)) {
            view.showContentProgress(true);
        } else {
            view.showLoadMoreProgress(true);
        }
    }

    private void updateContentView() {
        boolean empty = CollectionUtils.isEmpty(mDataList);
        view.showEmpty(empty);
        view.showContentList(!empty);
    }


    private void updateOffset(List<T> list) {

        switch (mOffsetType) {
            case LIMITED:
                mOffset += mLimit;
            case PAGE:
            default:
                mOffset++;
        }
    }

    @Override
    public void refresh() {
        if (!mRefreshing) {
            mRefreshing = true;
            mAllLoaded = false;
            mOffset = 0;
            loadMore();
        }

    }
}
