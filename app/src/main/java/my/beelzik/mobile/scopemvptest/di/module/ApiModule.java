package my.beelzik.mobile.scopemvptest.di.module;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import my.beelzik.mobile.scopemvptest.domain.GithubApi;
import retrofit2.Retrofit;

/**
 * Created by Andrey on 16.10.2016.
 */

@Module(includes = {RetrofitModule.class})
public class ApiModule {

    @Provides
    @Singleton
    public GithubApi provideGithubApi(Retrofit retrofit) {
        return retrofit.create(GithubApi.class);
    }
}
