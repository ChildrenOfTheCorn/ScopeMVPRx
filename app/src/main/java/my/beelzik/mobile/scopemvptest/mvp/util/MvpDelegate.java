package my.beelzik.mobile.scopemvptest.mvp.util;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import my.beelzik.mobile.scopemvptest.mvp.contract.BaseContract;

/**
 * Created by Andrey on 05.11.2016.
 */

public class MvpDelegate extends LifeCycleObservable {

    List<BaseContract.MvpPresenter> mPresenterList;

    public MvpDelegate() {
        mPresenterList = new ArrayList<>();
    }

    public <P extends BaseContract.MvpPresenter<V>, V> void bind(@NonNull P presenter, @NonNull V view) {
        mPresenterList.add(presenter);
        presenter.attachView(view);
        if (presenter instanceof LifeCycleSubscriber) {
            bind((LifeCycleSubscriber) presenter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (BaseContract.MvpPresenter presenter : mPresenterList) {
            presenter.detachView();
        }
    }
}
