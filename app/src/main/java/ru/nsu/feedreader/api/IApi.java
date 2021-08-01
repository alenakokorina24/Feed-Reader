package ru.nsu.feedreader.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nsu.feedreader.model.Data;

public interface IApi {

    @GET("everything")
    Call<Data> getNews(@Query("q") String query, @Query("pageSize") int pageSize,
                       @Query("page") int page, @Query("apiKey") String apiKey);

}
