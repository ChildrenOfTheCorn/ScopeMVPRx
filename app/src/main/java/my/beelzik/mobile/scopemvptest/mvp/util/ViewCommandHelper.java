package my.beelzik.mobile.scopemvptest.mvp.util;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Andrey on 05.11.2016.
 */

public class ViewCommandHelper<V> {

    private static final int MAX_COMMAND_IN_ROW = 16;

    private CommandSubject<ViewCommand<V>> mCommandSubject;
    private CompositeSubscription mSubscription;

    public ViewCommandHelper() {
        createSubject();
    }

    private void createSubject() {
        mCommandSubject = CommandSubject.createWithSize(MAX_COMMAND_IN_ROW);
    }

    public void sendCommand(ViewCommand<V> command) {
        mCommandSubject.onNext(command);
    }

    public void unsubscribe() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    private static final String TAG = "ViewCommandHelper";

    public void subscribe(V view) {
        if (mSubscription == null) {
            mSubscription = new CompositeSubscription();
        }
        mSubscription.add(mCommandSubject.subscribe(command -> {
                    command.emmit(view);
                    if (command instanceof ViewCommandSingle) {
                        mCommandSubject.remove(command);
                    }
                }
        ));
    }

    public void cleanCommands() {
        mCommandSubject.clear();
    }
}
