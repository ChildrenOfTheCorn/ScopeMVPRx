package my.beelzik.mobile.scopemvptest.mvp.contract;

/**
 * Created by Andrey on 15.09.2016.
 */
public interface AbsSearcherViewContract {

    interface View {

        void setSearchQueryText(CharSequence query);

        void openSearchInput();

        void closeSearchInput();


    }


    interface Presenter {

        String getQuery();

        void search(CharSequence query);

        void cancel();

        void attachView(View view);

        void setOnSearchListener(OnSearchListener onSearchListener);

    }

    interface OnSearchListener {

        void onSearchQuery(CharSequence query);

        void onCancelQuery();
    }
}
