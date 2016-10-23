package my.beelzik.mobile.scopemvptest.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrey on 16.10.2016.
 */

@Module
public class AppModule {

    Context mContext;


    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    public Context provideContext() {
        return mContext;
    }
}
