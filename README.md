# Atlas
Alternate for Google Places Search and Geocoding library, with the purpose of using our custom endpoint.
[![](https://jitpack.io/v/GreenDeveloperMukesh/Atlas.svg)](https://jitpack.io/#GreenDeveloperMukesh/Atlas)


# To get a Git project into your build:

# Step 1. 
place the module 'searchplacehelplibrary' from this project to your project 

# Step 2.
add in settings.gradle as below:
```
include ':app', ':searchplacehelplibrary'

``` 


# Step 2. 
Add the dependency app/build.gradle
```
implementation project(':searchplacehelplibrary')

```


# Step 3.
Implement SearchListener in your Activity
````
Activity extends AppCompatActivity implements SearchListener

````
# Step 4.
Initialize the base url of your endpoint
```
SearchPlaceHelper.init(BaseURL, this);
```

# Step 5.
Override Methods:
```
public void onSuccessPlace(List<PredictionModel.PredictedData> predictedDataList)
public void onSuccessGeocoding(PredictionModel.LocationClass locationObject)
public void onFailure(Throwable t)
public void onFailureGeocoding(Throwable t)
```
# Step 6.
Search a place by passing the input String to 
```
SearchPlaceHelper.searchPlace(inputString);
```
the result will be returned in callback as an Array
```
public void onSuccessPlace(List<PredictionModel.PredictedData> predictedDataList)
```
# Step 7.
Each Place item in the list holds
```
public String place_id;------> Place ID to be passed while 
                                converting to Latitude and Longitude Geocoding
public String description;----> Address String 
```
# For Converting Address to Latitude
# Step 1.
pass the place_id of searched place 
```
SearchPlaceHelper.geocodePlace(predictedData.place_id);
```
the latutude and longitude of the searched place will be returned in 
```
 @Override
    public void onSuccessGeocoding(PredictionModel.LocationClass locationObject) {
        if (locationObject != null && !TextUtils.isEmpty(locationObject.lat) && !TextUtils.isEmpty(locationObject.lng))
            resultLocation.setText("Location: " + locationObject.lat + "," + locationObject.lng);
    }
```

