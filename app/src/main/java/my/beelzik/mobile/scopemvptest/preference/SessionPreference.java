package my.beelzik.mobile.scopemvptest.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Andrey on 16.10.2016.
 */

public class SessionPreference {


    private static final String PREF_SESSION = "PREF_SESSION";
    private static final String KEY_TOKEN = "KEY_TOKEN";

    private SharedPreferences mSessionPref;

    private String mSessionToken = null;


    public SessionPreference(Context context) {
        mSessionPref = context.getSharedPreferences(PREF_SESSION, Context.MODE_PRIVATE);
    }

    public void setSessionToken(String token) {
        mSessionToken = token;
        mSessionPref.edit().putString(KEY_TOKEN, token).apply();
    }

    public boolean isAuthorized() {
        return !TextUtils.isEmpty(getSessionToken());
    }

    public void logout() {
        setSessionToken(null);
    }

    public String getSessionToken() {
        if (mSessionToken == null) {
            mSessionToken = mSessionPref.getString(KEY_TOKEN, null);
        }
        return mSessionToken;
    }
}
