package my.beelzik.mobile.scopemvptest.di.sub;

/**
 * Created by Andrey on 16.10.2016.
 */

import dagger.Subcomponent;
import my.beelzik.mobile.scopemvptest.di.ActivityScope;
import my.beelzik.mobile.scopemvptest.di.sub.module.SignInModule;
import my.beelzik.mobile.scopemvptest.ui.SignInActivity;

@ActivityScope
@Subcomponent(modules = SignInModule.class)
public interface SignInComponent {

    void inject(SignInActivity signInActivity);
}
