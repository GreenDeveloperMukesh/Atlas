package com.taxiappz.client.place_api;

import com.taxiappz.client.retro.responsemodel.PredictionModel;

import java.util.List;

public interface SearchListener {
    public void onSuccessPlace(List<PredictionModel.PredictedData> predictedDataList);
    public void onSuccessGeocoding(PredictionModel.LocationClass locationObject);

    public void onFailure(Throwable t);
    public void onFailureGeocoding(Throwable t);
}
