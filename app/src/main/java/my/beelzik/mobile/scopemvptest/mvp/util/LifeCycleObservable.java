package my.beelzik.mobile.scopemvptest.mvp.util;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Created by Andrey on 23.10.2016.
 */
@Accessors(prefix = "m")
public class LifeCycleObservable {

    List<LifeCycleSubscriber> mSubscriberList;

    Bundle mSavedInstanceState;

    @Getter private boolean mCreated;
    @Getter private boolean mStateRestored;
    @Getter private boolean mStarted;
    @Getter private boolean mResumed;
    @Getter private boolean mStopped;
    @Getter private boolean mPaused;

    public LifeCycleObservable() {
        mSubscriberList = new ArrayList<>();
    }

    public void bind(LifeCycleSubscriber subscriber) {
        if (subscriber != null && !mSubscriberList.contains(subscriber)) {
            mSubscriberList.add(subscriber);

            if (mCreated) {
                subscriber.onCreate(mSavedInstanceState);
            }

            if (mStateRestored) {
                subscriber.onRestoreInstanceState(mSavedInstanceState);
            }

            if (mStarted) {
                subscriber.onStart();
            }

            if (mResumed) {
                subscriber.onResume();
            }

            if (mPaused) {
                subscriber.onPause();
            }

            if (mStopped) {
                subscriber.onStop();
            }
        }
    }

    public void unbind(LifeCycleSubscriber subscriber) {
        if (subscriber != null && mSubscriberList.contains(subscriber)) {
            mSubscriberList.remove(subscriber);
        }
    }


    public void onCreate(@Nullable Bundle savedInstanceState) {

        mCreated = true;
        for (int i = 0; i < mSubscriberList.size(); i++) {
            mSubscriberList.get(i).onCreate(savedInstanceState);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        mStateRestored = true;
        for (int i = 0; i < mSubscriberList.size(); i++) {
            mSubscriberList.get(i).onRestoreInstanceState(savedInstanceState);
        }
    }


    public void onStart() {

        mStarted = true;
        for (int i = 0; i < mSubscriberList.size(); i++) {
            mSubscriberList.get(i).onStart();
        }
    }


    public void onResume() {

        mResumed = true;
        for (int i = 0; i < mSubscriberList.size(); i++) {
            mSubscriberList.get(i).onResume();
        }
    }


    public void onSaveInstanceState(Bundle outState) {

        for (int i = 0; i < mSubscriberList.size(); i++) {
            mSubscriberList.get(i).onSaveInstanceState(outState);
        }
    }


    public void onStop() {

        mStopped = true;
        for (int i = 0; i < mSubscriberList.size(); i++) {
            mSubscriberList.get(i).onStop();
        }
    }


    public void onPause() {

        mPaused = true;
        for (int i = 0; i < mSubscriberList.size(); i++) {
            mSubscriberList.get(i).onPause();
        }
    }


    public void onDestroy() {

        for (int i = 0; i < mSubscriberList.size(); i++) {
            mSubscriberList.get(i).onDestroy();
        }

        mCreated = false;
        mSavedInstanceState = null;

        mStateRestored = false;
        mStarted = false;
        mResumed = false;

        mStopped = false;
        mPaused = false;

        mSubscriberList.clear();
    }
}
