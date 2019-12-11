package br.com.angeloorrico.ctw.codechallengectw.models.responses;

import br.com.angeloorrico.ctw.codechallengectw.models.LocationDetailsModel;

public class LocationDetailsResponse extends LocationsResponse {

    private LocationDetailsModel response;

    public LocationDetailsModel getLocationDetail() {
        return response;
    }

    public void setLocationDetail(LocationDetailsModel response) {
        this.response = response;
    }
}
