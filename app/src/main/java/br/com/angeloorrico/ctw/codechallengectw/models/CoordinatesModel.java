package br.com.angeloorrico.ctw.codechallengectw.models;

import io.realm.RealmObject;

public class CoordinatesModel extends RealmObject {

    private LatLngModel displayPosition;

    public float getLatitude() {
        return displayPosition.latitude;
    }

    public void setLatitude(float latitude) {
        this.displayPosition.latitude = latitude;
    }

    public float getLongitude() {
        return displayPosition.longitude;
    }

    public void setLongitude(float longitude) {
        this.displayPosition.longitude = longitude;
    }

}
