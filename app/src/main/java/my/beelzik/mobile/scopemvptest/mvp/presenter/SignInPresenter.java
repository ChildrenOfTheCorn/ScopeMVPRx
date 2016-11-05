package my.beelzik.mobile.scopemvptest.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;

import my.beelzik.mobile.scopemvptest.R;
import my.beelzik.mobile.scopemvptest.mvp.contract.SignInContract;


/**
 * Created by Andrey on 16.10.2016.
 */

public class SignInPresenter extends BasePresenter<SignInContract.View> implements SignInContract.Presenter {

    boolean mSignedIn = false;
    Throwable mThrowable;

    @Override
    public void onSignInClick(String email, String password) {
        String emailError = null;
        String passwordError = null;

        view.showSignInFormError(null, null);

        if (TextUtils.isEmpty(email)) {
            emailError = getString(R.string.error_field_required);
        }

        if (TextUtils.isEmpty(password)) {
            passwordError = getString(R.string.error_invalid_password);
        }

        if (emailError != null || passwordError != null) {
            view.showSignInFormError(emailError, passwordError);
            return;
        }
        view.showProgress(true);
        view.enableForm(false);

        String credentials = String.format("%s:%s", email, password);

        final String token = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


        apiService.signIn(token).subscribe(user -> {
                    mSignedIn = true;
                    sessionPreference.setSessionToken(token);
                    signIn();
                },
                throwable -> {
                    mThrowable = throwable;
                    signInFailed();
                });
    }

    private void signInFailed() {
        if (isViewAttached()) {
            view.showProgress(false);
            view.enableForm(true);
            view.showError(mThrowable.getMessage());
            mThrowable = null;
        }
    }

    private void signIn() {
        if (isViewAttached()) {
            view.showProgress(false);
            view.enableForm(true);
            view.signIn();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionPreference.logout();
    }

    @Override
    public void attachView(SignInContract.View view) {
        super.attachView(view);

        if (mSignedIn) {
            signIn();
        }

        if (mThrowable != null) {
            signInFailed();
        }
    }
}
