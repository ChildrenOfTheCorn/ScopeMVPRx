package my.beelzik.mobile.scopemvptest.di;

import javax.inject.Singleton;

import dagger.Component;
import my.beelzik.mobile.scopemvptest.di.module.AppModule;
import my.beelzik.mobile.scopemvptest.di.module.PreferenceModule;
import my.beelzik.mobile.scopemvptest.di.module.ServiceModule;
import my.beelzik.mobile.scopemvptest.di.sub.MainComponent;
import my.beelzik.mobile.scopemvptest.di.sub.SignInComponent;
import my.beelzik.mobile.scopemvptest.di.sub.module.MainModule;
import my.beelzik.mobile.scopemvptest.di.sub.module.SignInModule;
import my.beelzik.mobile.scopemvptest.domain.interceptor.HeaderInterceptor;
import my.beelzik.mobile.scopemvptest.mvp.presenter.BasePresenterInjectionHelper;


/**
 * Created by Andrey on 16.10.2016.
 */

@Singleton
@Component(modules = {AppModule.class, ServiceModule.class, PreferenceModule.class})
public interface AppComponent {

    void inject(BasePresenterInjectionHelper basePresenter);

    SignInComponent plusSignInModule(SignInModule signInModule);

    MainComponent plusMainModule(MainModule mainModule);

    void inject(HeaderInterceptor headerInterceptor);
}
