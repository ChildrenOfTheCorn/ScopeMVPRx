package my.beelzik.mobile.scopemvptest.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import my.beelzik.mobile.scopemvptest.mvp.contract.BaseContract;
import my.beelzik.mobile.scopemvptest.mvp.util.LifeCycleSubscriber;
import my.beelzik.mobile.scopemvptest.mvp.util.MvpDelegate;

/**
 * Created by Andrey on 23.10.2016.
 */

public class BaseMvpActivity extends AppCompatActivity {


    MvpDelegate mMvpDelegate;

    protected void bindToLifeCycle(LifeCycleSubscriber subscriber) {
        mMvpDelegate.bind(subscriber);
    }

    protected void unbindFromLifeCycle(LifeCycleSubscriber subscriber) {
        mMvpDelegate.unbind(subscriber);
    }

    protected <P extends BaseContract.MvpPresenter<V>, V> void bindPresenter(P presenter, V view) {
        mMvpDelegate.bind(presenter, view);
    }

    public BaseMvpActivity() {
        mMvpDelegate = new MvpDelegate();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMvpDelegate.onCreate(savedInstanceState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mMvpDelegate.onRestoreInstanceState(savedInstanceState);
    }

    protected void isResumed() {
        mMvpDelegate.isResumed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mMvpDelegate.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMvpDelegate.onResume();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mMvpDelegate.onSaveInstanceState(outState);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mMvpDelegate.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMvpDelegate.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMvpDelegate.onDestroy();
    }
}
