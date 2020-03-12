package com.nplus.searchplacehelplibrary.retro;

import com.nplus.searchplacehelplibrary.retro.responsemodel.PredictionModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Mukesh on 17/02/2020.
 */

public interface GitHubService {

    @GET("/places/{place_name}")
    Call<PredictionModel> searchPlace(@Path("place_name") String placename);

    @GET("/places/location/{place_id}")
    Call<PredictionModel> geocodePlace(@Path("place_id") String place_id);

}
