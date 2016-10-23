package my.beelzik.mobile.scopemvptest.mvp.presenter;

import android.content.Context;

import javax.inject.Inject;

import my.beelzik.mobile.scopemvptest.App;
import my.beelzik.mobile.scopemvptest.domain.ApiService;
import my.beelzik.mobile.scopemvptest.preference.SessionPreference;

/**
 * Created by Andrey on 23.10.2016.
 */

public class BasePresenterInjectionHelper {

    @Inject
    Context context;

    @Inject
    ApiService apiService;

    @Inject
    SessionPreference sessionPreference;

    public BasePresenterInjectionHelper() {
        App.getAppComponent().inject(this);
    }
}
