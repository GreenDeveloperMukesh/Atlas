package com.taxiappz.client.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PredictionModel implements Serializable {
    @SerializedName("place_json")
    @Expose
    public PlaceJsonClass place_json;
    @SerializedName("locaion_details")
    @Expose
    public LocationDetailsClass locaion_details;

    public class LocationDetailsClass {
        @SerializedName("result")
        public ResultClass result;
    }

    public class ResultClass {
        @SerializedName("geometry")
        public GeomentryClass geometry;
    }

    public class GeomentryClass {
        @SerializedName("location")
        public LocationClass location;
    }

    public class LocationClass {
        @SerializedName("lat")
        public String lat;
        @SerializedName("lng")
        public String lng;
    }

    public class PlaceJsonClass {
        @SerializedName("predictions")
        public List<PredictedData> predictions;

        @SerializedName("status")
        public String status;
    }

    public class PredictedData implements Serializable {
        @SerializedName("place_id")
        public String place_id;
        @SerializedName("description")
        public String description;
        @SerializedName("id")
        public String id;
    }

}