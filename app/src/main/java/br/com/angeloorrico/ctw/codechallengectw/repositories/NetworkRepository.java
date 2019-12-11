package br.com.angeloorrico.ctw.codechallengectw.repositories;

import android.util.Log;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;
import br.com.angeloorrico.ctw.codechallengectw.models.responses.LocationDetailsResponse;
import br.com.angeloorrico.ctw.codechallengectw.models.responses.LocationsResponse;
import br.com.angeloorrico.ctw.codechallengectw.networking.HereListLocationServices;
import br.com.angeloorrico.ctw.codechallengectw.networking.HereLocationDetailsService;
import br.com.angeloorrico.ctw.codechallengectw.networking.LocationApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkRepository extends BaseRepository {

    protected static final String TAG_LOG = NetworkRepository.class.getSimpleName();

    /**
     * params to be used in Here API requests
     */
    private final String APP_ID          = "KZ0vdyPnplWU2yxI5ncP";
    private final String APP_CODE        = "RP8DxFe6cXWZTRB-bdYOPA";
    private final String BEGIN_HIGHLIGHT = "<b>";
    private final String END_HIGHLIGHT   = "</b>";
    private final int MAX_RESULTS        = 20;
    private final int GEN                = 9;
    private final int JSON_ATTRIBUTES    = 1;

    private static NetworkRepository networkRepository;

    private LocationApi apiListLocationsRequest, apiLocationDetailsRequest;

    public NetworkRepository() {
        apiListLocationsRequest = HereListLocationServices.getRetrofitInstance().create(LocationApi.class);
        apiLocationDetailsRequest = HereLocationDetailsService.getRetrofitInstance().create(LocationApi.class);
    }

    public static NetworkRepository getInstance() {
        if (networkRepository == null) {
            networkRepository = new NetworkRepository();
        }
        return networkRepository;
    }

    /**
     * retrieves a list of locations based on the query informed
     */
    public void fetchLocations(MutableLiveData<List<LocationModel>> data,
                                                             String query, String coordinates, int sortBy) {
        apiListLocationsRequest.getLocations(APP_ID, APP_CODE, MAX_RESULTS,
                BEGIN_HIGHLIGHT, END_HIGHLIGHT, coordinates, query)
                .enqueue(new Callback<LocationsResponse>() {
                    @Override
                    public void onResponse(Call<LocationsResponse> call, Response<LocationsResponse> response) {
                        Log.d(TAG_LOG, "onResponse | locationsResponse: " + response);

                        if (response.body() != null) {
                            data.postValue(sortList(response.body().getLocations(), sortBy));
                        }
                    }

                    @Override
                    public void onFailure(Call<LocationsResponse> call, Throwable t) {
                        data.setValue(null);
                    }
                });
    }

    /**
     * retrieves the selected locations details
     */
    public void fetchLocationDetails(MutableLiveData<LocationModel> data, String locationId) {
        apiLocationDetailsRequest.getLocationDetail(APP_ID, APP_CODE, GEN, JSON_ATTRIBUTES, locationId)
                .enqueue(new Callback<LocationDetailsResponse>() {
                    @Override
                    public void onResponse(Call<LocationDetailsResponse> call, Response<LocationDetailsResponse> response) {
                        Log.d(TAG_LOG, "onResponse | locationDetailsResponse: " + response);

                        if (response.body() != null) {
                            data.postValue(response.body().getLocationDetail().getView().get(0).getResult().get(0));
                        }
                    }

                    @Override
                    public void onFailure(Call<LocationDetailsResponse> call, Throwable t) {
                        data.setValue(null);
                    }
                });
    }

}