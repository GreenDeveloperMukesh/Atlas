package nplus.atlas.ui.placesearch;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nplus.atlas.R;
import nplus.atlas.place_api.SearchListener;
import nplus.atlas.place_api.SearchPlaceHelper;
import nplus.atlas.retro.responsemodel.PredictionModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mukesh on 17/02/2020.
 */

public class PlaceSearchScreen extends AppCompatActivity implements SearchListener {
    String BaseURL = "http://13.56.246.254:3001";
    PlaceSearchAdapter searchRestultAdapter;
    List<PredictionModel.PredictedData> predictedDataList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView resultAddress, resultLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SearchPlaceHelper.init(BaseURL, this);

        resultAddress = findViewById(R.id.txtViewAddress);
        resultLocation = findViewById(R.id.txtViewLocation);

        ((EditText) findViewById(R.id.edit_placeSearch)).addTextChangedListener(textWatcher);
        searchRestultAdapter = new PlaceSearchAdapter(predictedDataList, this);

        recyclerView = ((RecyclerView) findViewById(R.id.recycler_placeSearch));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(searchRestultAdapter);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable != null && editable.length() > 0)
                SearchPlaceHelper.searchPlace(editable.toString());
        }
    };

    @Override
    public void onSuccessPlace(List<PredictionModel.PredictedData> predictedDataList) {
        Log.d("Result", "Got The Result");
        Toast.makeText(this, "Got the result", Toast.LENGTH_SHORT).show();
        searchRestultAdapter.addAllItems(predictedDataList);
    }

    @Override
    public void onSuccessGeocoding(PredictionModel.LocationClass locationObject) {
        Log.d("Result", "Got The Result");
        Toast.makeText(this, "Got the result", Toast.LENGTH_SHORT).show();
        if (locationObject != null && !TextUtils.isEmpty(locationObject.lat) && !TextUtils.isEmpty(locationObject.lng))
            resultLocation.setText("Location: " + locationObject.lat + "," + locationObject.lng);
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d("Result", "Failed in Result");
        Toast.makeText(this, "Failed in Result", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailureGeocoding(Throwable t) {
        Log.d("Result", "Failed in Geocoding Result");
        Toast.makeText(this, "Failed in Geocoding Result", Toast.LENGTH_SHORT).show();
//        searchRestultAdapter.clearAllItems();

    }

    public void onItemClicked(PredictionModel.PredictedData predictedData) {
        if (predictedData != null && !TextUtils.isEmpty(predictedData.description) && !TextUtils.isEmpty(predictedData.place_id)) {
            searchRestultAdapter.clearAllItems();
            resultAddress.setText("Address: "+predictedData.description);
            SearchPlaceHelper.geocodePlace(predictedData.place_id);
        }
    }
}