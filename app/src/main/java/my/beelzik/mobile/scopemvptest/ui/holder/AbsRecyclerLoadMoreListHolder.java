package my.beelzik.mobile.scopemvptest.ui.holder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import my.beelzik.mobile.scopemvptest.R;
import my.beelzik.mobile.scopemvptest.mvp.contract.AbsLoadMoreListContract;
import my.beelzik.mobile.scopemvptest.mvp.util.ViewStateHelper;
import my.beelzik.mobile.scopemvptest.ui.adapter.BaseListRecyclerAdapter;
import my.beelzik.mobile.scopemvptest.ui.adapter.ProgressWrapperRecyclerAdapter;

;

/**
 * Created by Andrey on 11.09.2016.
 */
public abstract class AbsRecyclerLoadMoreListHolder<T> extends BaseHolder implements AbsLoadMoreListContract.View<T> {

    private static final String TAG = "AbsRecyclerLoadMoreList";

    private static final String KEY_STATE_IN_PROGRESS = "STATE_IN_PROGRESS";
    private static final String KEY_STATE_REFRESHING = "KEY_STATE_REFRESHING";

    @BindView(R.id.swipe_refresh_layout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @Nullable
    @BindView(R.id.empty_view)
    protected View mEmptyView;

    @BindView(R.id.progress_bar)
    protected View mProgressBar;

    protected AbsLoadMoreListContract.Presenter mPresenter;

    protected ProgressWrapperRecyclerAdapter mWrapperAdapter;
    protected BaseListRecyclerAdapter<T> mRootAdapter;

    protected Context mContext;

    private ViewStateHelper mStateHelper;

    RecyclerView.OnScrollListener mLoadMoreScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!mWrapperAdapter.isInProgress()) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int lastItemPosition = mWrapperAdapter.getItemCount() - 1;
                boolean lastItemOnTheScreen = layoutManager.findLastCompletelyVisibleItemPosition() == lastItemPosition;
                if (lastItemOnTheScreen) {
                    mPresenter.loadMore();
                }
            }
        }
    };

    public AbsRecyclerLoadMoreListHolder(View view, BaseListRecyclerAdapter<T> adapter) {
        super(view);

        mContext = view.getContext();

        mStateHelper = new ViewStateHelper(TAG + "_" + view.getId());
        mStateHelper.addViews(mSwipeRefreshLayout, mRecyclerView, mEmptyView, mProgressBar);

        mRootAdapter = adapter;
        mWrapperAdapter = new ProgressWrapperRecyclerAdapter(mContext, adapter, getProgressLayoutRes());


        mRecyclerView.setAdapter(mWrapperAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addOnScrollListener(mLoadMoreScrollListener);

        this.mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mPresenter.refresh();
        });
    }

    @LayoutRes
    protected abstract int getProgressLayoutRes();

    @Override
    public void showRefreshing(boolean progress) {
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(progress);
        });
    }

    @Override
    public void showContentProgress(boolean progress) {
        mProgressBar.setVisibility(progress ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showContentList(boolean show) {
        mRecyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void fetchItems(List<T> list) {
        mRootAdapter.setList(list);
    }

    @Override
    public void showLoadMoreProgress(boolean progress) {

        mWrapperAdapter.setInProgress(progress);

        if (progress) {

            mRecyclerView.post(() -> {
                int lastItemPosition = mWrapperAdapter.getItemCount() - 1;
                mRecyclerView.smoothScrollToPosition(lastItemPosition);
            });

        }
    }

    @Override
    public void showEmpty(boolean show) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void setPresenter(AbsLoadMoreListContract.Presenter<T> presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mStateHelper.onSaveInstanceState(outState);

        outState.putBoolean(KEY_STATE_IN_PROGRESS, mWrapperAdapter.isInProgress());
        outState.putBoolean(KEY_STATE_REFRESHING, mSwipeRefreshLayout.isRefreshing());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mStateHelper.onRestoreInstanceState(savedInstanceState);
            mWrapperAdapter.setInProgress(savedInstanceState.getBoolean(KEY_STATE_IN_PROGRESS));
            mSwipeRefreshLayout.setRefreshing(savedInstanceState.getBoolean(KEY_STATE_REFRESHING));
        }


    }
}
