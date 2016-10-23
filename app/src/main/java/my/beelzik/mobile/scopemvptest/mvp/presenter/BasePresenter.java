package my.beelzik.mobile.scopemvptest.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import my.beelzik.mobile.scopemvptest.domain.ApiService;
import my.beelzik.mobile.scopemvptest.mvp.contract.BaseContract;
import my.beelzik.mobile.scopemvptest.mvp.util.LifeCycleSubscriber;
import my.beelzik.mobile.scopemvptest.preference.SessionPreference;

/**
 * Created by Andrey on 16.10.2016.
 */

public class BasePresenter<T> implements BaseContract.MvpPresenter<T>, LifeCycleSubscriber {

    private BasePresenterInjectionHelper mInjectionHelper;

    protected Context context;
    protected ApiService apiService;
    protected SessionPreference sessionPreference;

    protected T view;

    public BasePresenter() {
        mInjectionHelper = new BasePresenterInjectionHelper();

        context = mInjectionHelper.context;
        apiService = mInjectionHelper.apiService;
        sessionPreference = mInjectionHelper.sessionPreference;
    }


    protected String getString(@StringRes int stringRes) {
        return context.getString(stringRes);
    }

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
