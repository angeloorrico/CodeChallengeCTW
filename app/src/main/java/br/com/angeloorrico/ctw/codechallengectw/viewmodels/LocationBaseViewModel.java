package br.com.angeloorrico.ctw.codechallengectw.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;

public abstract class LocationBaseViewModel extends AndroidViewModel {

    MutableLiveData<List<LocationModel>> locationsList = new MutableLiveData<>();

    public LocationBaseViewModel(@NonNull Application application) {
        super(application);
    }

    public void clearLocationsList() {
        this.locationsList.setValue(null);
    }

    public LiveData<List<LocationModel>> getLocationsList() {
        return this.locationsList;
    }

}
