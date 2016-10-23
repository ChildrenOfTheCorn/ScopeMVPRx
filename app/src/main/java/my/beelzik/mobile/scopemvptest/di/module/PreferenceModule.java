package my.beelzik.mobile.scopemvptest.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import my.beelzik.mobile.scopemvptest.preference.SessionPreference;

/**
 * Created by Andrey on 16.10.2016.
 */

@Module
public class PreferenceModule {

    @Singleton
    @Provides
    public SessionPreference provideSessionPreference(Context context) {
        return new SessionPreference(context);
    }
}
