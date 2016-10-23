package my.beelzik.mobile.scopemvptest.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import my.beelzik.mobile.scopemvptest.mvp.util.LifeCycleObservable;
import my.beelzik.mobile.scopemvptest.mvp.util.LifeCycleSubscriber;

/**
 * Created by Andrey on 23.10.2016.
 */

public class BaseActivity extends AppCompatActivity {


    LifeCycleObservable mLifeCycleDelegate;


    protected void bindToLifeCycle(LifeCycleSubscriber subscriber) {
        mLifeCycleDelegate.bind(subscriber);
    }

    protected void unbindFromLifeCycle(LifeCycleSubscriber subscriber) {
        mLifeCycleDelegate.unbind(subscriber);
    }

    public BaseActivity() {
        mLifeCycleDelegate = new LifeCycleObservable();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLifeCycleDelegate.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mLifeCycleDelegate.onRestoreInstanceState(savedInstanceState);
    }

    protected void isResumed() {
        mLifeCycleDelegate.isResumed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLifeCycleDelegate.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLifeCycleDelegate.onResume();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mLifeCycleDelegate.onSaveInstanceState(outState);
    }


    @Override
    protected void onStop() {
        super.onStop();

        mLifeCycleDelegate.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mLifeCycleDelegate.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLifeCycleDelegate.onDestroy();
    }
}
