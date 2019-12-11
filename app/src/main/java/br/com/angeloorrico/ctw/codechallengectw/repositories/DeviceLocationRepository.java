package br.com.angeloorrico.ctw.codechallengectw.repositories;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

public class DeviceLocationRepository {

    private static DeviceLocationRepository deviceLocationRepository;

    private LocationManager locationManager;
    private Context context;
    private LocationListener locationListener;

    public MutableLiveData<Location> deviceLocation;

    public DeviceLocationRepository(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                deviceLocation.setValue(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    public static DeviceLocationRepository getInstance(Context context) {
        if (deviceLocationRepository == null) {
            deviceLocationRepository = new DeviceLocationRepository(context);
        }
        return deviceLocationRepository;
    }

    public void registerListener(MutableLiveData<Location> deviceLocation) {
        this.deviceLocation = deviceLocation;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gps_enabled)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0,0, locationListener);
        else
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0,0, locationListener);
    }

    public void unregisterListener() {
        locationManager.removeUpdates(locationListener);
    }

}