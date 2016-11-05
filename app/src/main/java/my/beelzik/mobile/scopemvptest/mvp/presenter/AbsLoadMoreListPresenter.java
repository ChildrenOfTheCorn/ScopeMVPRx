package my.beelzik.mobile.scopemvptest.mvp.presenter;

import java.util.List;

import lombok.experimental.Accessors;
import my.beelzik.mobile.scopemvptest.mvp.contract.AbsLoadMoreListContract;
import my.beelzik.mobile.scopemvptest.utils.CollectionUtils;


/**
 * Created by Andrey on 11.09.2016.
 */
@Accessors(prefix = "m")
public abstract class AbsLoadMoreListPresenter<T, F> extends BasePresenter<AbsLoadMoreListContract.View<T>> implements AbsLoadMoreListContract.Presenter<T> {

    private static final String TAG = "AbsLoadMoreListPresenter";

    public enum OffsetType {
        LIMITED,
        PAGE
    }

    private OffsetType mOffsetType = OffsetType.PAGE;

    private int mDefaultOffset = 0;
    private int mOffset = 0;
    private int mLimit = 10;

    protected List<T> mDataList;

    protected boolean mRefreshing = false;
    protected boolean mLoading = false;
    protected boolean mAllLoaded = false;


    public AbsLoadMoreListPresenter(OffsetType offsetType, int defaultOffset, int limit) {
        super();
        mOffsetType = offsetType;
        mLimit = limit;
        mDefaultOffset = defaultOffset;
        mOffset = defaultOffset;
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
            onLoadMore(mOffset, mLimit, mOffsetType);
        }
    }

    protected void resetOffset() {
        mOffset = mDefaultOffset;
    }

    protected abstract void onLoadMore(int offset, int limit, OffsetType offsetType);

    protected void onLoadingSuccess(List<T> list) {

        cleanCommands();
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
        cancelProgress();
        updateContent();
        mLoading = false;
    }

    protected void onLoadingFailed(F failed) {

        if (mRefreshing) {
            mRefreshing = false;
        }
        mLoading = false;

        cleanCommands();
        cancelProgress();
        updateContent();
    }

    protected void updateContent() {
        send(view -> view.fetchItems(mDataList));
        updateContentView();
    }

    protected void cancelProgress() {
        send(view -> view.showRefreshing(false));
        send(view -> view.showLoadMoreProgress(false));
        send(view -> view.showContentProgress(false));
    }

    protected void showInProgress() {
        send(view -> view.showEmpty(false));
        if (mRefreshing) {
            send(view -> view.showRefreshing(true));
        } else if (CollectionUtils.isEmpty(mDataList)) {
            send(view -> view.showContentProgress(true));
        } else {
            send(view -> view.showLoadMoreProgress(true));
        }
    }

    private void updateContentView() {
        boolean empty = CollectionUtils.isEmpty(mDataList);
        send(view -> view.showEmpty(empty));
        send(view -> view.showContentList(!empty));
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
        cleanCommands();
        if (!mRefreshing) {
            mRefreshing = true;
            mAllLoaded = false;

            if (mLoading) {
                cancelLoading();
                mLoading = false;
            }
            resetOffset();
            loadMore();
        }

    }

    protected abstract void cancelLoading();
}
