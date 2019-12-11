package br.com.angeloorrico.ctw.codechallengectw.models.responses;

import java.util.List;

import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;

public class LocationsResponse {

    private List<LocationModel> suggestions = null;

    public List<LocationModel> getLocations() {
        return suggestions;
    }

    public void setLocations(List<LocationModel> suggestions) {
        this.suggestions = suggestions;
    }
}
