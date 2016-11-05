package my.beelzik.mobile.scopemvptest.mvp.presenter;

import android.text.TextUtils;

import lombok.NonNull;
import my.beelzik.mobile.scopemvptest.mvp.contract.AbsSearcherViewContract;

/**
 * Created by Andrey on 05.11.2016.
 */

public class SearchPresenter extends BasePresenter<AbsSearcherViewContract.View> implements AbsSearcherViewContract.Presenter {

    private AbsSearcherViewContract.OnSearchListener mOnSearchListener;

    CharSequence mQuery;

    public SearchPresenter(@NonNull AbsSearcherViewContract.OnSearchListener listener) {
        mOnSearchListener = listener;
    }

    @Override
    public void search(CharSequence query) {
        mQuery = query;
        mOnSearchListener.onSearchQuery(query);
    }

    @Override
    public void cancel() {
        mQuery = null;
        mOnSearchListener.onCancelQuery();
    }

    @Override
    public void attachView(AbsSearcherViewContract.View view) {
        super.attachView(view);

        if (!TextUtils.isEmpty(mQuery)) {
            view.showCurrentQueryText(mQuery);
        }
    }
}
