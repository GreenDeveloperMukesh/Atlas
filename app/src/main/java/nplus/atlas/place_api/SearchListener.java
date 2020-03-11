package nplus.atlas.place_api;

import nplus.atlas.retro.responsemodel.PredictionModel;

import java.util.List;

public interface SearchListener {
    public void onSuccessPlace(List<PredictionModel.PredictedData> predictedDataList);
    public void onSuccessGeocoding(PredictionModel.LocationClass locationObject);

    public void onFailure(Throwable t);
    public void onFailureGeocoding(Throwable t);
}
