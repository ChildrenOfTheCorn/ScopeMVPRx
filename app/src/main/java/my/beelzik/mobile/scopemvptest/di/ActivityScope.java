package my.beelzik.mobile.scopemvptest.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Andrey on 16.10.2016.
 */


@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {

}
