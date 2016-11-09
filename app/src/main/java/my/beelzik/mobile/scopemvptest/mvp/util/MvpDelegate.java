package my.beelzik.mobile.scopemvptest.mvp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.NonNull;
import my.beelzik.mobile.scopemvptest.mvp.contract.BaseContract;

/**
 * Created by Andrey on 05.11.2016.
 */

public class MvpDelegate extends LifeCycleObservable {

    private List<BaseContract.MvpPresenter> mPresenterList;

    private Map<BaseContract.MvpPresenter, List<Object>> mMvpPresenterViewsMap;

    public MvpDelegate() {
        mPresenterList = new ArrayList<>();
        mMvpPresenterViewsMap = new HashMap<>();
    }

    public <P extends BaseContract.MvpPresenter<V>, V> void bind(@NonNull P presenter, @NonNull V view) {
        mPresenterList.add(presenter);
        presenter.attachView(view);

        if (mMvpPresenterViewsMap.get(presenter) == null) {
            List<Object> viewsList = new ArrayList<>();
            viewsList.add(view);
            mMvpPresenterViewsMap.put(presenter, viewsList);
        } else {
            List<Object> viewsList = mMvpPresenterViewsMap.get(presenter);
            viewsList.add(view);
        }
        if (presenter instanceof LifeCycleSubscriber) {
            bind((LifeCycleSubscriber) presenter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (BaseContract.MvpPresenter presenter : mPresenterList) {
            for (Object view : mMvpPresenterViewsMap.get(presenter)) {
                presenter.detachView(view);
            }
        }
    }
}
