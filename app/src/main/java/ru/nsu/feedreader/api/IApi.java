package ru.nsu.feedreader.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nsu.feedreader.model.Data;

public interface IApi {

    @GET("top-headlines")
    Call<Data> getNews(@Query("country") String country, @Query("apiKey") String apiKey);

}
