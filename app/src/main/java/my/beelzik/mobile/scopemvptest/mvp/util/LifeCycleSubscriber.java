package my.beelzik.mobile.scopemvptest.mvp.util;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Andrey on 23.10.2016.
 */

public interface LifeCycleSubscriber extends StateSavable {


    void onCreate(@Nullable Bundle savedInstanceState);

    // form StateSavable void onSaveInstanceState(Bundle outState);

    void onStart();

    void onResume();

    // form StateSavable void onRestoreInstanceState(Bundle savedInstanceState);

    void onStop();

    void onPause();

    void onDestroy();
}
