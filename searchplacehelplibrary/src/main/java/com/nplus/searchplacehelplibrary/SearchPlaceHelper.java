package com.nplus.searchplacehelplibrary;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.nplus.searchplacehelplibrary.retro.GitHubService;
import com.nplus.searchplacehelplibrary.retro.responsemodel.PredictionModel;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchPlaceHelper {
    private static String baseURL;
    private static SearchListener searchListener;
    private static GitHubService gitHubService;
    private static Context ctxt;
    private static HashMap<String, String> map = new HashMap();

    public static void init(String baseurl, SearchListener searchlistener, Context context) {
        baseURL = baseurl;
        ctxt = context;
        searchListener = searchlistener;
        if (gitHubService == null)
            initialiseGithubService();
    }

    public static void searchPlace(String input, Double lat, Double lng) {
        if (input != null && !TextUtils.isEmpty(input)) {
            map.clear();
            map.put("lat", "" + lat);
            map.put("lng", "" + lng);
            gitHubService.searchPlace(input, map).enqueue(predictionModelCallback);
        }
    }

    public static void geocodePlace(String placeID) {
        if (placeID != null && !TextUtils.isEmpty(placeID)) {
            gitHubService.geocodePlace(placeID).enqueue(geocodingCallBack);
        }
    }

    private static void initialiseGithubService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(httpClient.build())
                .baseUrl(baseURL);
        gitHubService = builder.build().create(GitHubService.class);
    }

    private static Callback<PredictionModel> predictionModelCallback = new Callback<PredictionModel>() {
        @Override
        public void onResponse(Call<PredictionModel> call, Response<PredictionModel> response) {
            if (response.isSuccessful() && response.body() != null) {

                if (response.body().place_json != null && response.body().place_json.predictions != null) {

                    searchListener.onSuccessPlace(response.body().place_json.predictions);
                } else {
                    searchListener.onFailure(new Throwable("Zero Results"));
                }
            } else {
                searchListener.onFailure(new Throwable("Zero Results"));
            }
        }

        @Override
        public void onFailure(Call<PredictionModel> call, Throwable t) {
            searchListener.onFailure(t);
        }
    };
    private static Callback<PredictionModel> geocodingCallBack = new Callback<PredictionModel>() {
        @Override
        public void onResponse(Call<PredictionModel> call, Response<PredictionModel> response) {
            if (response.isSuccessful() && response.body() != null) {

                if (response.body().locaion_details != null
                        && response.body().locaion_details.result != null
                        && response.body().locaion_details.result.geometry != null
                        && response.body().locaion_details.result.geometry.location != null) {

                    searchListener.onSuccessGeocoding(response.body().locaion_details.result.geometry.location);
                } else {
                    searchListener.onFailureGeocoding(new Throwable("Zero Results"));
                }
            } else {
                searchListener.onFailureGeocoding(new Throwable("Zero Results"));
            }
        }

        @Override
        public void onFailure(Call<PredictionModel> call, Throwable t) {
            searchListener.onFailureGeocoding(t);
        }
    };
}
