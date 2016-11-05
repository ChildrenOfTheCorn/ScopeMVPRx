package my.beelzik.mobile.scopemvptest.ui.holder;

import android.content.Context;
import android.support.v7.widget.SearchView;

import my.beelzik.mobile.scopemvptest.mvp.contract.AbsSearcherViewContract;


/**
 * Created by Andrey on 15.09.2016.
 */
public class SearcherHolder implements AbsSearcherViewContract.View {


    Context mContext;
    SearchView mSearchView;
    AbsSearcherViewContract.Presenter mPresenter;

    public SearcherHolder(Context context, SearchView searchView, AbsSearcherViewContract.Presenter presenter) {
        mContext = context;
        mSearchView = searchView;
        mPresenter = presenter;


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
            mPresenter.cancel();
            return false;
        });
    }


    @Override
    public void showCurrentQueryText(CharSequence query) {

        mSearchView.post(() -> {
            mSearchView.setQuery(query, false);
            mSearchView.clearFocus();
        });

        mSearchView.setIconified(false);
        mSearchView.clearFocus();
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

}
