package my.beelzik.mobile.scopemvptest.ui.holder;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Andrey on 23.10.2016.
 */

public class BaseHolder {


    public View rootView;

    public BaseHolder(View rootView) {
        this.rootView = rootView;

        ButterKnife.bind(this, rootView);
    }
}
