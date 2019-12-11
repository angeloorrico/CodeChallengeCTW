package br.com.angeloorrico.ctw.codechallengectw.utils;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public final class Utils {

    public static final int PERMISSION_REQUEST_DEVICE_LOCATION = 1;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES       = 1000;
    public static final int REQUEST_LOCATION_ACTIVATION        = 2000;

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return false;
            }
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
        } else {
            boolean connectedMobile = false;
            boolean connectedWifi = false;
            try {
                connectedMobile =
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
            } catch (Exception e) {
            }

            try {
                connectedWifi =
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
            } catch (Exception e) {
            }
            return connectedMobile || connectedWifi;
        }
    }

    public static boolean askForLocationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_DEVICE_LOCATION);
        }
        return false;
    }

    public static void runViewAnimation(View view) {
        ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1.0f).setDuration(500).start();
        ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1.0f).setDuration(500).start();
    }

    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(activity);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(activity, result,
                        REQUEST_GOOGLE_PLAY_SERVICES).show();
            }
            return false;
        }
        return true;
    }

    public static boolean isAccessFineLocationPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isGPSEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return isAccessFineLocationPermissionGranted(context) &&
                manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void checkGPSConfiguration(Activity activity) {
        LocationRequest mLocationRequest;
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        GoogleApiClient mGoogleClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .build();
        mGoogleClient.connect();

        PendingResult<LocationSettingsResult> pendingResult =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleClient, builder.build());

        pendingResult.setResultCallback(result -> {
            final Status status = result.getStatus();
            final LocationSettingsStates states = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    try {
                        activity.createPendingResult(REQUEST_LOCATION_ACTIVATION, activity.getIntent(),
                                PendingIntent.FLAG_ONE_SHOT).send(Activity.RESULT_OK);
                    } catch (PendingIntent.CanceledException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                                activity,
                                REQUEST_LOCATION_ACTIVATION);
                        mGoogleClient.disconnect();
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                    try {
                        activity.createPendingResult(REQUEST_LOCATION_ACTIVATION, activity.getIntent(),
                                PendingIntent.FLAG_ONE_SHOT).send(Activity.RESULT_CANCELED);
                    } catch (PendingIntent.CanceledException ce) {
                        // Ignore the error.
                    }
                    break;
            }
        });
    }

}