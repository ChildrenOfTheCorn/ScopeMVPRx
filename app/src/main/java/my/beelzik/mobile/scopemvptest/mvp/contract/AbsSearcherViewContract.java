package my.beelzik.mobile.scopemvptest.mvp.contract;

/**
 * Created by Andrey on 15.09.2016.
 */
public interface AbsSearcherViewContract {

    interface View {

        void showCurrentQueryText(CharSequence query);

        void openSearchInput();

        void closeSearchInput();


    }


    interface Presenter extends BaseContract.MvpPresenter<View> {

        void search(CharSequence query);

        void cancel();

    }

    interface OnSearchListener {

        void onSearchQuery(CharSequence query);

        void onCancelQuery();
    }
}
