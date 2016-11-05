package my.beelzik.mobile.scopemvptest.domain;

import java.util.List;

import my.beelzik.mobile.scopemvptest.model.Repository;
import my.beelzik.mobile.scopemvptest.model.User;
import my.beelzik.mobile.scopemvptest.model.gson.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Andrey on 16.10.2016.
 */

public interface GithubApi {

    Integer PAGE_SIZE = 30;

    @GET("/user")
    Call<User> signIn(@Header("Authorization") String token);

    @GET("/search/repositories?sort=stars&order=desc")
    Call<SearchResult> search(@Query("q") String query, @Query("type") String type, @Query("page") int page, @Query("per_page") int pageSize);

    @GET("/users/{login}/repos")
    Call<List<Repository>> getUserRepos(@Path("login") String login, @Query("page") int page, @Query("per_page") int pageSize);
}
