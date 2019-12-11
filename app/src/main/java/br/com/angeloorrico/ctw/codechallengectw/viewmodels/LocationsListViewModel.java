package br.com.angeloorrico.ctw.codechallengectw.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;
import br.com.angeloorrico.ctw.codechallengectw.repositories.NetworkRepository;

public class LocationsListViewModel extends LocationBaseViewModel {

    private NetworkRepository networkRepository;

    public LocationsListViewModel(@NonNull Application application) {
        super(application);
        this.networkRepository = NetworkRepository.getInstance();
    }

    /**
     * locations will be available through the live data on the repository when ready
     */
    public List<LocationModel> searchLocations(String query, String coordinates, int sortBy) {
        networkRepository.fetchLocations(locationsList, query, coordinates, sortBy);

        return locationsList.getValue();
    }

    public void sortList(int sortBy) {
        if (locationsList.getValue() != null)
            locationsList.postValue(networkRepository.sortList(locationsList.getValue(), sortBy));
    }

}