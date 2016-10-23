package my.beelzik.mobile.scopemvptest.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import my.beelzik.mobile.scopemvptest.domain.ApiService;
import my.beelzik.mobile.scopemvptest.domain.ApiServiceImpl;
import my.beelzik.mobile.scopemvptest.domain.GithubApi;

/**
 * Created by Andrey on 16.10.2016.
 */

@Module(includes = {ApiModule.class})
public class ServiceModule {

    @Provides
    @Singleton
    public ApiService provideApiService(GithubApi githubApi) {
        return new ApiServiceImpl(githubApi);
    }
}
