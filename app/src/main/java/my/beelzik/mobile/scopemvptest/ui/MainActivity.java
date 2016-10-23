package my.beelzik.mobile.scopemvptest.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.beelzik.mobile.scopemvptest.App;
import my.beelzik.mobile.scopemvptest.R;
import my.beelzik.mobile.scopemvptest.di.sub.MainComponent;
import my.beelzik.mobile.scopemvptest.di.sub.module.MainModule;
import my.beelzik.mobile.scopemvptest.model.Repository;
import my.beelzik.mobile.scopemvptest.mvp.contract.AbsLoadMoreListContract;
import my.beelzik.mobile.scopemvptest.mvp.util.ComponentDelegate;
import my.beelzik.mobile.scopemvptest.mvp.util.IHasComponent;
import my.beelzik.mobile.scopemvptest.mvp.util.LifeCycleSubscriber;
import my.beelzik.mobile.scopemvptest.mvp.util.ViewStateHelper;
import my.beelzik.mobile.scopemvptest.mvp.view.BaseActivity;
import my.beelzik.mobile.scopemvptest.ui.adapter.RepositoryAdapter;
import my.beelzik.mobile.scopemvptest.ui.holder.DefaultRecyclerLoadMoreListHolder;
import my.beelzik.mobile.scopemvptest.ui.holder.ProgressToolbarHolder;

/**
 * Created by Andrey on 17.10.2016.
 */

public class MainActivity extends BaseActivity implements IHasComponent<MainComponent> {

    private static final String TAG = "MainActivity";
    DefaultRecyclerLoadMoreListHolder<Repository> mLoadMoreListViewHolder;

    @BindView(R.id.progress_toolbar)
    Toolbar mProgressToolbarView;

    @BindView(R.id.swipe_refresh_recycler)
    View mSwipeRefreshRecycler;

    RepositoryAdapter mAdapter;

    ProgressToolbarHolder mToolbarHolder;

    ViewStateHelper mStateHelper;

    @Inject
    AbsLoadMoreListContract.Presenter<Repository> mLoadMoreListPresenter;

    private ComponentDelegate<MainComponent> mComponentDelegate;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mToolbarHolder = new ProgressToolbarHolder(mProgressToolbarView);

        mStateHelper = new ViewStateHelper(TAG);
        mStateHelper.addViews(mToolbarHolder.progressBar);
        mStateHelper.onRestoreInstanceState(savedInstanceState);

        setSupportActionBar(mToolbarHolder.toolbar);

        mComponentDelegate = new ComponentDelegate<>(TAG, this);
        mComponentDelegate.getComponent().inject(this);

        mAdapter = new RepositoryAdapter(this);
        mLoadMoreListViewHolder = new DefaultRecyclerLoadMoreListHolder<Repository>(mSwipeRefreshRecycler, mAdapter) {

            @Override
            public void showLoadMoreProgress(boolean progress) {
                super.showLoadMoreProgress(progress);
                mToolbarHolder.showToolbarProgress(progress);
            }

            @Override
            public void showContentProgress(boolean progress) {
                super.showContentProgress(progress);
                mToolbarHolder.showToolbarProgress(progress);
            }

            @Override
            public void showRefreshing(boolean progress) {
                super.showRefreshing(progress);
                mToolbarHolder.showToolbarProgress(progress);
            }

        };
        mLoadMoreListViewHolder.onRestoreInstanceState(savedInstanceState);
        mLoadMoreListViewHolder.setPresenter(mLoadMoreListPresenter);

        mLoadMoreListPresenter.attachView(mLoadMoreListViewHolder);
        bindToLifeCycle((LifeCycleSubscriber) mLoadMoreListPresenter);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLoadMoreListViewHolder.onSaveInstanceState(outState);
        mStateHelper.onSaveInstanceState(outState);
    }


    @Override
    public MainComponent createComponent() {
        return App.getAppComponent().plusMainModule(new MainModule());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoadMoreListPresenter.detachView();


    }

    @Override
    public void finish() {
        super.finish();
        mComponentDelegate.removeComponent();
    }
}
