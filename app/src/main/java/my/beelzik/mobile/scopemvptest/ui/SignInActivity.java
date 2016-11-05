package my.beelzik.mobile.scopemvptest.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.beelzik.mobile.scopemvptest.App;
import my.beelzik.mobile.scopemvptest.R;
import my.beelzik.mobile.scopemvptest.di.sub.SignInComponent;
import my.beelzik.mobile.scopemvptest.di.sub.module.SignInModule;
import my.beelzik.mobile.scopemvptest.mvp.contract.SignInContract;
import my.beelzik.mobile.scopemvptest.mvp.util.ComponentDelegate;
import my.beelzik.mobile.scopemvptest.mvp.util.IHasComponent;
import my.beelzik.mobile.scopemvptest.mvp.view.BaseMvpActivity;
import my.beelzik.mobile.scopemvptest.ui.holder.ProgressToolbarHolder;
import my.beelzik.mobile.scopemvptest.utils.ViewUtils;

public class SignInActivity extends BaseMvpActivity implements SignInContract.View, IHasComponent<SignInComponent> {

    private static final String KEY_STATE_FORM_ENABLED = "KEY_STATE_FORM_ENABLED";

    private ComponentDelegate<SignInComponent> mComponentDelegate;

    @BindView(R.id.progress_toolbar)
    Toolbar mProgressToolbarView;

    @BindView(R.id.input_layout_email)
    TextInputLayout mInputLayoutEmail;

    @BindView(R.id.input_layout_password)
    TextInputLayout mInputLayoutPassword;

    @BindView(R.id.email)
    AutoCompleteTextView mInputEmail;

    @BindView(R.id.password)
    EditText mInputPassword;

    @BindView(R.id.login_form)
    ViewGroup mLoginForm;

    @BindView(R.id.sign_in)
    Button mSignIn;

    ProgressToolbarHolder mToolbarHolder;

    // ViewStateHelper mStateHelper;

    @Inject
    SignInContract.Presenter mSignInPresenter;

    private AlertDialog mErrorDialog;

    private boolean mFormEnabled = false;

    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        ButterKnife.bind(this);
        mToolbarHolder = new ProgressToolbarHolder(mProgressToolbarView);
      /*  mStateHelper = new ViewStateHelper(this);
        mStateHelper.addViews(mInputLayoutEmail, mInputLayoutPassword, mInputEmail, mInputPassword, mToolbarHolder.progressBar, mSignIn);
        mStateHelper.onRestoreInstanceState(savedInstanceState);*/

       /* if (savedInstanceState != null) {
            mFormEnabled = savedInstanceState.getBoolean(KEY_STATE_FORM_ENABLED);
            if (!mFormEnabled) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        }*/
        mErrorDialog = new AlertDialog.Builder(this).setTitle("Error").create();
        mErrorDialog.setOnDismissListener(dialog -> mSignInPresenter.onErrorDialogDismiss());

        setSupportActionBar(mToolbarHolder.toolbar);

        mComponentDelegate = new ComponentDelegate<>(TAG, this);

        mComponentDelegate.getComponent().inject(this);

        bindPresenter(mSignInPresenter, this);


        mSignIn.setOnClickListener(v -> attemptLogin());

        mInputPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE) {
                attemptLogin();
                return true;
            }
            return false;
        });
    }

    private void attemptLogin() {
        mSignInPresenter.onSignInClick(mInputEmail.getText().toString(), mInputPassword.getText().toString());
    }

   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStateHelper.onSaveInstanceState(outState);

        outState.putBoolean(KEY_STATE_FORM_ENABLED, mFormEnabled);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSignInPresenter.detachView();
    }

    @Override
    public void finish() {
        mComponentDelegate.removeComponent();
        super.finish();
    }

    @Override
    public void signIn() {
        Toast.makeText(this, "signIn ", Toast.LENGTH_SHORT).show();
        MainActivity.start(this);
        finish();
    }


    @Override
    public void enableForm(boolean enable) {
        mFormEnabled = enable;
        ViewUtils.enableHierarchy(mLoginForm, enable);
        if (!enable) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    @Override
    public void showProgress(boolean progress) {
        mToolbarHolder.showToolbarProgress(progress);
    }

    @Override
    public void showError(CharSequence error) {
        mErrorDialog.setMessage(error);
        mErrorDialog.show();
    }

    @Override
    public void showSignInFormError(CharSequence emailError, CharSequence passwordError) {

        mInputLayoutEmail.setError(emailError);
        mInputLayoutPassword.setError(passwordError);

    }

    @Override
    public SignInComponent createComponent() {
        return App.getAppComponent().plusSignInModule(new SignInModule());
    }
}
