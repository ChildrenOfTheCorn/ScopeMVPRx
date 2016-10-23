package my.beelzik.mobile.scopemvptest.mvp.util;

import android.os.Bundle;

/**
 * Created by Andrey on 23.10.2016.
 */

public interface StateSavable {

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

}
