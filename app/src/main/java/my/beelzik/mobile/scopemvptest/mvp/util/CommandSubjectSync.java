package my.beelzik.mobile.scopemvptest.mvp.util;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

/**
 * Created by Andrey on 08.11.2016.
 */
public class CommandSubjectSync<T> extends Subject<T, T> {

    CommandSubjectState<T> mState;


    public static <T> CommandSubjectSync<T> create() {
        CommandSubjectState<T> state = new CommandSubjectState<>();
        return new CommandSubjectSync<>(state);
    }


    private CommandSubjectSync(CommandSubjectState<T> state) {
        super(state);

        mState = state;
    }

    @Override
    public boolean hasObservers() {
        return false;
    }

    @Override
    public void onCompleted() {
        mState.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mState.onError(e);
    }

    @Override
    public void onNext(T value) {
        mState.onNext(value);
    }


    public void remove(T value) {
        mState.remove(value);
    }

    public void clear() {
        mState.clear();
    }

    private static class CommandSubjectState<T> implements OnSubscribe<T>, Observer<T> {

        List<T> mBuffer;

        boolean mDone = false;

        Throwable mThrowable = null;

        public CommandSubjectState() {
            mBuffer = new ArrayList<>();
            mSubscriberList = new ArrayList<>();
        }


        List<Subscriber<? super T>> mSubscriberList;

        @Override
        public void call(Subscriber<? super T> subscriber) {

            mSubscriberList.add(subscriber);
            subscriber.add(Subscriptions.create(() -> {
                mSubscriberList.remove(subscriber);
            }));

            drain(subscriber);
        }

        private void drain(Subscriber<? super T> subscriber) {
            for (T value : mBuffer) {
                subscriber.onNext(value);
            }


            if (mDone) {
                if (mThrowable != null) {
                    subscriber.onError(mThrowable);
                } else {
                    subscriber.onCompleted();
                }
            }
        }

        @Override
        public void onCompleted() {
            if (!mDone) {
                mDone = true;

                for (Subscriber<? super T> subscriber : mSubscriberList) {
                    subscriber.onCompleted();
                }
            }

        }

        @Override
        public void onError(Throwable e) {
            if (!mDone) {
                mDone = true;
                mThrowable = e;

                for (Subscriber<? super T> subscriber : mSubscriberList) {
                    subscriber.onError(e);
                }
            }

        }

        @Override
        public void onNext(T value) {
            if (!mDone) {
                mBuffer.add(value);

                for (Subscriber<? super T> subscriber : mSubscriberList) {
                    subscriber.onNext(value);
                }
            }

        }

        public void remove(T value) {
            mBuffer.remove(value);
        }

        public void clear() {
            mBuffer.clear();
        }
    }
}
