package my.beelzik.mobile.scopemvptest.domain.interceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import my.beelzik.mobile.scopemvptest.App;
import my.beelzik.mobile.scopemvptest.preference.SessionPreference;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Andrey on 23.10.2016.
 */

public class HeaderInterceptor implements Interceptor {

    private static final String X_OAUTH_SCOPES = "X-OAuth-Scopes";
    private static final String X_ACCEPTED_OAUTH_SCOPES = "X-Accepted-OAuth-Scopes";


    private static final String OAUTH_HEADER = "Authorization";

    @Inject
    SessionPreference mSessionPreference;

    public HeaderInterceptor() {
        App.getAppComponent().inject(this);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        if (mSessionPreference.isAuthorized()) {
            builder.addHeader(X_OAUTH_SCOPES, "repo, user")
                    .addHeader(X_ACCEPTED_OAUTH_SCOPES, "repo, user")
                    .addHeader(OAUTH_HEADER, mSessionPreference.getSessionToken());
        }

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Request request = builder.build();
        return chain.proceed(request);
    }
}
