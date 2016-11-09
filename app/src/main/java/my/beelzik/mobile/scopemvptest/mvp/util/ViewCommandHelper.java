package my.beelzik.mobile.scopemvptest.mvp.util;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Andrey on 05.11.2016.
 */

public class ViewCommandHelper<V> {

    private static final int MAX_COMMAND_IN_ROW = 16;

    private CommandSubjectSync<ViewCommand<V>> mCommandSubject;
    private CompositeSubscription mSubscription;

    Map<V, Subscription> mSubscriptionMap;

    public ViewCommandHelper() {
        createSubject();
        mSubscriptionMap = new HashMap<>();
    }

    private void createSubject() {
        //mCommandSubject = CommandSubject.createWithSize(MAX_COMMAND_IN_ROW);
        mCommandSubject = CommandSubjectSync.create();
    }

    public void sendCommand(ViewCommand<V> command) {
        mCommandSubject.onNext(command);
    }

    public void unsubscribe(V view) {
        if (mSubscription != null) {
            Subscription subscription = mSubscriptionMap.get(view);

            if (subscription != null) {
                mSubscription.remove(subscription);
            }

            if (!mSubscription.hasSubscriptions()) {
                mSubscription.unsubscribe();
                mSubscription = null;
            }
        }
    }

    private static final String TAG = "ViewCommandHelper";

    public void subscribe(V view) {
        if (mSubscription == null) {
            mSubscription = new CompositeSubscription();
        }

        Subscription subscription = mCommandSubject.doOnNext(command -> {
            if (command instanceof ViewCommandSingle) {
                mCommandSubject.remove(command);
            }
        }).subscribe(command -> {
                    command.emmit(view);

                }
        );

        mSubscriptionMap.put(view, subscription);
        mSubscription.add(subscription);
    }

    public void cleanCommands() {
        mCommandSubject.clear();
    }

    public boolean hasSubscribers() {
        return mSubscription != null && mSubscription.hasSubscriptions();
    }
}
