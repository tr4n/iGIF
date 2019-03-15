package com.example.mypc.officaligif.networks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface iGIPHYService {
    @GET("search")
    Call<MediaResponse> getMediaResponses(@Query("q") String key,@Query("lang") String language, @Query("limit") Integer limit, @Query("api_key") String apiKey);


}
