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

    @Override
    public void onSignInClick(String email, String password) {
        String emailError = null;
        String passwordError = null;

        send(view -> view.showSignInFormError(null, null));

        if (TextUtils.isEmpty(email)) {
            emailError = getString(R.string.error_field_required);
        }

        if (TextUtils.isEmpty(password)) {
            passwordError = getString(R.string.error_invalid_password);
        }

        if (emailError != null || passwordError != null) {
            final String finalEmailError = emailError;
            final String finalPasswordError = passwordError;
            send(view -> view.showSignInFormError(finalEmailError, finalPasswordError));
            return;
        }
        send(view -> view.showProgress(true));
        send(view -> view.enableForm(false));

        String credentials = String.format("%s:%s", email, password);

        final String token = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


        apiService.signIn(token).subscribe(user -> {
                    sessionPreference.setSessionToken(token);
                    send(view -> view.showProgress(false));
                    send(view -> view.enableForm(true));
                    send(SignInContract.View::signIn);
                },
                throwable -> {
                    send(view -> view.showProgress(false));
                    send(view -> view.enableForm(true));
                    sendSingle(view -> view.showError(throwable.getMessage()));
                });
    }

    @Override
    public void onErrorDialogDismiss() {
        // cleanCommands();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionPreference.logout();
    }

}
