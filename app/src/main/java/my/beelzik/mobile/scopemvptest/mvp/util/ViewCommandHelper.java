package my.beelzik.mobile.scopemvptest.mvp.util;

import android.util.Log;

import rx.Subscription;

/**
 * Created by Andrey on 05.11.2016.
 */

public class ViewCommandHelper<V> {

    private static final int MAX_COMMAND_IN_ROW = 16;

    private CommandSubject<ViewCommand<V>> mReplaySubject;
    private Subscription mSubscription;

    //V mView;

    public ViewCommandHelper() {
        createSubject();
    }

    private void createSubject() {
        mReplaySubject = CommandSubject.createWithSize(MAX_COMMAND_IN_ROW);
    }

    public void sendCommand(ViewCommand<V> command) {
        mReplaySubject.onNext(command);
    }

    public void unsubscribe() {
        //mView = null;
        mSubscription.unsubscribe();
    }

    private static final String TAG = "ViewCommandHelper";

    public void subscribe(V view) {
        //mView = view;
        mSubscription = mReplaySubject.subscribe(command -> {
                    Log.d(TAG, "command type: " + command.getClass());
                    Log.d(TAG, "command instanceof ViewCommandSingle: " + (command instanceof ViewCommandSingle));
                    command.emmit(view);
                    if (command instanceof ViewCommandSingle) {
                        mReplaySubject.remove(command);
                    }
                }
        );
    }

    public void cleanCommands() {
        mReplaySubject.clear();
        /*mReplaySubject.onCompleted();
        mSubscription.unsubscribe();
        createSubject();*/
        /*if (mView != null) {
            subscribe(mView);
        }*/

    }
}
