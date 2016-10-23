package my.beelzik.mobile.scopemvptest.domain;

import java.util.List;

import my.beelzik.mobile.scopemvptest.model.Repository;
import my.beelzik.mobile.scopemvptest.model.User;
import my.beelzik.mobile.scopemvptest.model.gson.SearchResult;
import rx.Observable;

/**
 * Created by Andrey on 16.10.2016.
 */

public interface ApiService {

    Observable<User> signIn(String token);

    Observable<SearchResult> search(String query);

    Observable<List<Repository>> getUserRepos(String login, int page, int pageSize);
}
