package br.com.angeloorrico.ctw.codechallengectw.viewmodels;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import br.com.angeloorrico.ctw.codechallengectw.repositories.DeviceLocationRepository;

public class DeviceLocationViewModel extends AndroidViewModel {

    private DeviceLocationRepository deviceLocationRepository;

    private MutableLiveData<Location> deviceLocation = new MutableLiveData<>();

    public DeviceLocationViewModel(@NonNull Application application) {
        super(application);
        deviceLocationRepository = DeviceLocationRepository.getInstance(application);
    }

    public void init() {
        deviceLocationRepository.registerListener(deviceLocation);
    }

    /**
     * device locations will be available through the live data on the repository when ready
     */
    public MutableLiveData<Location> getDeviceLocation() {
        return deviceLocation;
    }

    @Override
    public void finalize() {
        deviceLocationRepository.unregisterListener();
    }

}