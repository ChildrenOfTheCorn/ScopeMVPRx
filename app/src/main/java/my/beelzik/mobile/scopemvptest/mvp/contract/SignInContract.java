package my.beelzik.mobile.scopemvptest.mvp.contract;

/**
 * Created by Andrey on 16.10.2016.
 */

public interface SignInContract {


    interface View {

        void signIn();

        void showProgress(boolean progress);

        void enableForm(boolean enable);

        void showError(CharSequence error);


        void showSignInFormError(CharSequence emailError, CharSequence passwordError);

    }

    interface Presenter extends BaseContract.MvpPresenter<View> {

        void onSignInClick(String email, String password);

    }
}
