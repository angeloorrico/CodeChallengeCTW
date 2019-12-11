package br.com.angeloorrico.ctw.codechallengectw.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationDetailsModel;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;
import br.com.angeloorrico.ctw.codechallengectw.repositories.NetworkRepository;
import br.com.angeloorrico.ctw.codechallengectw.repositories.RealmRepository;

public class LocationDetailsViewModel extends LocationBaseViewModel {

    private NetworkRepository networkRepository;
    private RealmRepository realmRepository;

    private MutableLiveData<LocationModel> locationDetails = new MutableLiveData<>();

    public LocationDetailsViewModel(@NonNull Application application) {
        super(application);
        this.networkRepository = NetworkRepository.getInstance();
        this.realmRepository = RealmRepository.getInstance(getApplication());
    }

    /**
     * details will be available through the live data on the repository when ready
     */
    public LocationModel fetchLocationDetails(String locationId) {
        networkRepository.fetchLocationDetails(locationDetails, locationId);

        return locationDetails.getValue();
    }

    public LiveData<LocationModel> getLocationDetailsResponse() {
        return locationDetails;
    }

    public void saveLocation(LocationModel location) {
        realmRepository.saveLocation(location);
    }

    public void deleteLocation(String locationId) {
        realmRepository.deleteLocation(locationId);
    }

    /**
     * favorites will be available through the live data on the repository when ready
     */
    public void fetchFavoritedLocations() {
        realmRepository.fetchFavoritedLocations(locationsList);
    }

    public boolean isLocationPersisted(String locationId) {
        return realmRepository.isLocationFavorited(locationId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        realmRepository.dispose();
    }

}