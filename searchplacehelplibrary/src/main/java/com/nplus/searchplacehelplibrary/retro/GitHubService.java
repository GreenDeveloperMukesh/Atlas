package com.nplus.searchplacehelplibrary.retro;

import com.nplus.searchplacehelplibrary.retro.responsemodel.PredictionModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Mukesh on 17/02/2020.
 */

public interface GitHubService {

    @GET("/places/{place_name}")
    Call<PredictionModel> searchPlace(@Path("place_name") String placename, @QueryMap Map<String, String> latlng);

    @GET("/places/location/{place_id}")
    Call<PredictionModel> geocodePlace(@Path("place_id") String place_id);

}
