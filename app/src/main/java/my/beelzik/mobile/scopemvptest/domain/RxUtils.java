package my.beelzik.mobile.scopemvptest.domain;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxUtils {

    public static <T> Observable<T> wrapRetroCall(final Call<T> call) {
        return Observable.create(subscriber ->
        {
            final Response<T> execute;
            try {
                execute = call.execute();
            } catch (IOException e) {
                subscriber.onError(e);
                return;
            }

            if (execute.isSuccessful()) {
                subscriber.onNext(execute.body());
            } else {
                subscriber.onError(new ServiceError(execute.errorBody()));
            }
        });
    }

    public static <T> Observable<T> wrapAsyncRetroCall(Call<T> call) {
        Observable<T> observable = wrapRetroCall(call);
        observable = wrapAsync(observable);
        return observable;
    }

    public static <T> Observable<T> wrapAsync(Observable<T> observable) {
        return wrapAsync(observable, Schedulers.io());
    }

    public static <T> Observable<T> wrapAsync(Observable<T> observable, Scheduler scheduler) {
        return observable
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
