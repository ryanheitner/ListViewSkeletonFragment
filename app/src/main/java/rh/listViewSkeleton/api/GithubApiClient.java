package rh.listViewSkeleton.api;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import rh.listViewSkeleton.model.Contributor;

public class GithubApiClient {
    private static final String API_URL = "https://api.github.com";

    private static TwitchTvApiInterface sTwitchTvService;

    public static TwitchTvApiInterface getTwitchTvApiClient() {
        if (sTwitchTvService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .build();

            sTwitchTvService = restAdapter.create(TwitchTvApiInterface.class);
        }

        return sTwitchTvService;
    }

    public interface TwitchTvApiInterface {
        @GET("/repos/{owner}/{repo}/contributors")
        void getContributors(@Path("owner") String owner,@Path("repo") String repo, Callback<List<Contributor>> callback);
    }
}
