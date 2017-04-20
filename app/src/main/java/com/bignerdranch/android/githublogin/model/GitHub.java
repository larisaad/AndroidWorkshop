package com.bignerdranch.android.githublogin.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by larisa on 06.04.2017.
 */

public interface GitHub {
    @GET("/")
    Call<LoginData> checkAuth(@Header("Authorization") String auth);

    @GET("/user")
    Call<UserData> getUserData(@Header("Authorization") String auth);

    @GET("/user/repos")
    Call<List<RepositoryData>> getRepoData(@Header("Authorization") String auth);

    class Service {
        private static GitHub sInstance;

        public synchronized static GitHub Get() {
            if (sInstance == null) {
                sInstance = new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(GitHub.class);
            }
            return sInstance;
        }
    }
}
