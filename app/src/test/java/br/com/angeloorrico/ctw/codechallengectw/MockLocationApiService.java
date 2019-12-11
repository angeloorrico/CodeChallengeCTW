package br.com.angeloorrico.ctw.codechallengectw;

import java.util.ArrayList;
import java.util.List;

import br.com.angeloorrico.ctw.codechallengectw.models.LocationDetailsModel;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;
import br.com.angeloorrico.ctw.codechallengectw.models.ResultDetailsModel;
import br.com.angeloorrico.ctw.codechallengectw.models.responses.LocationDetailsResponse;
import br.com.angeloorrico.ctw.codechallengectw.models.responses.LocationsResponse;
import br.com.angeloorrico.ctw.codechallengectw.networking.LocationApi;
import retrofit2.Call;
import retrofit2.mock.BehaviorDelegate;

public class MockLocationApiService implements LocationApi {

    private final BehaviorDelegate<LocationApi> delegate;

    public MockLocationApiService(BehaviorDelegate<LocationApi> service) {
        this.delegate = service;
    }

    @Override
    public Call<LocationsResponse> getLocations(String appId, String appCode, int maxresults,
                                                String beginHighlight, String endHighlight,
                                                String prox, String query) {
        LocationsResponse response = new LocationsResponse();
        List<LocationModel> list = new ArrayList<>();
        list.add(new LocationModel.Builder().
                setLocationId("NT-67898765")
                .build());
        list.add(new LocationModel.Builder()
                .setLocationId("NT-24343433")
                .build());
        list.add(new LocationModel.Builder()
                .setLocationId("NT-07264942")
                .build());
        response.setLocations(list);
        return delegate.returningResponse(response).getLocations(appId, appCode, 1,
                beginHighlight, endHighlight, prox, query);
    }

    @Override
    public Call<LocationDetailsResponse> getLocationDetail(String appId, String appCode,
                                                           int gen, int jsonattributes, String locationid) {
        LocationDetailsResponse response = new LocationDetailsResponse();

        LocationDetailsModel location = new LocationDetailsModel();
        List<ResultDetailsModel> list = new ArrayList<>();
        ResultDetailsModel model = new ResultDetailsModel();
        List<LocationModel> list2 = new ArrayList<>();
        LocationModel loc = new LocationModel.Builder()
                .setLocationId("NT-0234942")
                .setLabel("Rua Gilberto Amado")
                .build();
        list2.add(loc);
        model.setResult(list2);
        list.add(model);
        location.setView(list);
        response.setLocationDetail(location);

        return delegate.returningResponse(response).getLocationDetail(appId, appCode, gen,
                jsonattributes, locationid);
    }

}