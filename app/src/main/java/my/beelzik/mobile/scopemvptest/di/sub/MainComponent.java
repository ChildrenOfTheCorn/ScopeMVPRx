package my.beelzik.mobile.scopemvptest.di.sub;

import dagger.Subcomponent;
import my.beelzik.mobile.scopemvptest.di.ActivityScope;
import my.beelzik.mobile.scopemvptest.di.sub.module.MainModule;
import my.beelzik.mobile.scopemvptest.ui.MainActivity;

/**
 * Created by Andrey on 23.10.2016.
 */
@ActivityScope
@Subcomponent(modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity mainActivity);
}
