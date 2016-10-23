package my.beelzik.mobile.scopemvptest.di.sub.module;

import dagger.Module;
import dagger.Provides;
import my.beelzik.mobile.scopemvptest.di.ActivityScope;
import my.beelzik.mobile.scopemvptest.mvp.contract.SignInContract;
import my.beelzik.mobile.scopemvptest.mvp.presenter.SignInPresenter;

/**
 * Created by Andrey on 16.10.2016.
 */

@Module
public class SignInModule {

    @ActivityScope
    @Provides
    public SignInContract.Presenter provideSignInPresenter() {
        return new SignInPresenter();
    }
}
