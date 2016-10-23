package my.beelzik.mobile.scopemvptest.ui.holder;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;

import my.beelzik.mobile.scopemvptest.mvp.contract.AbsSearcherViewContract;


/**
 * Created by Andrey on 15.09.2016.
 */
public class SearcherHolder implements AbsSearcherViewContract.View {


    Context mContext;
    SearchView mSearchView;
    AbsSearcherViewContract.Presenter mPresenter;
    boolean mSelfClose = false;

    public SearcherHolder(Context context, SearchView searchView) {
        mContext = context;
        mSearchView = searchView;


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        mSearchView.setOnCloseListener(() -> {
            if (!mSelfClose) {
                mPresenter.cancel();
            } else {
                mSelfClose = false;
            }
            return false;
        });
    }


    @Override
    public void setSearchQueryText(CharSequence query) {

        mSearchView.post(() -> {
            mSearchView.setQuery(query, false);
            mSearchView.clearFocus();
        });

        if (!TextUtils.isEmpty(query)) {
            mSearchView.setIconified(false);
            mSearchView.clearFocus();
        } else {
            mSelfClose = true;
            mSearchView.setIconified(true);
        }
    }

    @Override
    public void openSearchInput() {
        mSearchView.setIconified(true);
    }

    @Override
    public void closeSearchInput() {
        mSearchView.setIconified(false);
        mSearchView.clearFocus();
    }

    public void setPresenter(AbsSearcherViewContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
