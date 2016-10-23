package my.beelzik.mobile.scopemvptest;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import my.beelzik.mobile.scopemvptest.di.AppComponent;
import my.beelzik.mobile.scopemvptest.di.DaggerAppComponent;
import my.beelzik.mobile.scopemvptest.di.module.AppModule;
import my.beelzik.mobile.scopemvptest.di.module.PreferenceModule;

/**
 * Created by Andrey on 16.10.2016.
 */

public class App extends Application {

    private static AppComponent sAppComponent;

    private static final String TAG = "App";
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .preferenceModule(new PreferenceModule())
                .build();
        Log.d(TAG, "onCreate sAppComponent is null: " + (sAppComponent == null));
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

}
