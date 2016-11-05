package my.beelzik.mobile.scopemvptest.domain;

import java.util.List;

import my.beelzik.mobile.scopemvptest.model.Repository;
import my.beelzik.mobile.scopemvptest.model.User;
import my.beelzik.mobile.scopemvptest.model.gson.SearchResult;
import rx.Observable;

/**
 * Created by Andrey on 16.10.2016.
 */

public class ApiServiceImpl implements ApiService {

    GithubApi mGithubApi;

    public ApiServiceImpl(GithubApi githubApi) {
        mGithubApi = githubApi;
    }

    @Override
    public Observable<User> signIn(String token) {
        return RxUtils.wrapAsyncRetroCall(mGithubApi.signIn(token));
    }

    @Override
    public Observable<SearchResult> search(String query, String type, int page, int pageSize) {

        return RxUtils.wrapAsyncRetroCall(mGithubApi.search(query, type, page, pageSize));
    }

    @Override
    public Observable<List<Repository>> getUserRepos(String login, int page, int pageSize) {
        return RxUtils.wrapAsyncRetroCall(mGithubApi.getUserRepos(login, page, pageSize));
    }
}
