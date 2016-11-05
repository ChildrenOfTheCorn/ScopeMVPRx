package my.beelzik.mobile.scopemvptest.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.beelzik.mobile.scopemvptest.App;
import my.beelzik.mobile.scopemvptest.R;
import my.beelzik.mobile.scopemvptest.di.sub.MainComponent;
import my.beelzik.mobile.scopemvptest.di.sub.module.MainModule;
import my.beelzik.mobile.scopemvptest.model.Repository;
import my.beelzik.mobile.scopemvptest.mvp.contract.AbsSearcherViewContract;
import my.beelzik.mobile.scopemvptest.mvp.contract.MainRepositoryLoadMoreContract;
import my.beelzik.mobile.scopemvptest.mvp.util.ComponentDelegate;
import my.beelzik.mobile.scopemvptest.mvp.util.IHasComponent;
import my.beelzik.mobile.scopemvptest.mvp.view.BaseMvpActivity;
import my.beelzik.mobile.scopemvptest.ui.adapter.RepositoryAdapter;
import my.beelzik.mobile.scopemvptest.ui.holder.DefaultRecyclerLoadMoreListHolder;
import my.beelzik.mobile.scopemvptest.ui.holder.ProgressToolbarHolder;
import my.beelzik.mobile.scopemvptest.ui.holder.SearcherHolder;

/**
 * Created by Andrey on 17.10.2016.
 */

public class MainActivity extends BaseMvpActivity implements IHasComponent<MainComponent> {

    private static final String TAG = "MainActivity";

    DefaultRecyclerLoadMoreListHolder<Repository> mLoadMoreListViewHolder;

    SearcherHolder mSearcherHolder;

    @BindView(R.id.progress_toolbar)
    Toolbar mProgressToolbarView;

    @BindView(R.id.swipe_refresh_recycler)
    View mSwipeRefreshRecycler;

    RepositoryAdapter mAdapter;

    ProgressToolbarHolder mToolbarHolder;

    // ViewStateHelper mStateHelper;

    @Inject
    MainRepositoryLoadMoreContract.Presenter mLoadMoreListPresenter;

    @Inject
    AbsSearcherViewContract.Presenter mSearchPresenter;

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

       /* mStateHelper = new ViewStateHelper(TAG);
        mStateHelper.addViews(mToolbarHolder.progressBar);
        mStateHelper.onRestoreInstanceState(savedInstanceState);*/

        setSupportActionBar(mToolbarHolder.toolbar);

        mComponentDelegate = new ComponentDelegate<>(TAG, this);
        mComponentDelegate.getComponent().inject(this);

        mAdapter = new RepositoryAdapter(this);
        mLoadMoreListViewHolder = new DefaultRecyclerLoadMoreListHolder<Repository>(mSwipeRefreshRecycler, mAdapter, mLoadMoreListPresenter) {

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
      /*  mLoadMoreListViewHolder.onRestoreInstanceState(savedInstanceState);*/

        bindPresenter(mLoadMoreListPresenter, mLoadMoreListViewHolder);

    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLoadMoreListViewHolder.onSaveInstanceState(outState);
        mStateHelper.onSaveInstanceState(outState);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        /*
        TODO view создается денамически, не совсем понятно как это забиндить через делегат.
        Можно конечно mSearcherHolder создать в onCreate, а searchView передать не в конструкторе а в сетере но хз хз
        */
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        mSearcherHolder = new SearcherHolder(this, searchView, mSearchPresenter);

        bindPresenter(mSearchPresenter, mSearcherHolder);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public MainComponent createComponent() {
        return App.getAppComponent().plusMainModule(new MainModule());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void finish() {
        super.finish();
        mComponentDelegate.removeComponent();
    }
}
