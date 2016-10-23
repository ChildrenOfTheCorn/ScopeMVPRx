package my.beelzik.mobile.scopemvptest.ui.holder;

import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import my.beelzik.mobile.scopemvptest.R;

/**
 * Created by Andrey on 24.10.2016.
 */

public class ProgressToolbarHolder extends BaseHolder {

    public Toolbar toolbar;

    @BindView(R.id.progress)
    public View progressBar;


    public ProgressToolbarHolder(Toolbar rootView) {
        super(rootView);
        toolbar = rootView;
    }

    public void showToolbarProgress(boolean progress) {
        progressBar.setVisibility(progress ? View.VISIBLE : View.GONE);
    }
}
