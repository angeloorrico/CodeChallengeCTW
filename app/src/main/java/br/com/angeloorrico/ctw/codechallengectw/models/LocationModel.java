package br.com.angeloorrico.ctw.codechallengectw.models;

import android.text.Html;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class LocationModel extends RealmObject implements Serializable {

    public static final String COLUMN_ID = "locationId";

    @PrimaryKey
    private String locationId;

    private String label;

    private AddressModel address;

    @Ignore
    private int distance;

    private CoordinatesModel location;

    public LocationModel() {
    }

    private LocationModel(final Builder builder) {
        this.locationId = builder.locationId;
        this.label = builder.label;
        this.address = builder.address;
        this.distance = builder.distance;
        this.location = builder.location;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getLabel() {
        return label;
    }

    public String getPlainLabel() {
        return Html.fromHtml(label).toString();
    }

    public AddressModel getAddress() {
        return address;
    }

    public int getDistance() {
        return distance;
    }

    public CoordinatesModel getLocation() {
        return this.location;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public void setLocation(CoordinatesModel displayPosition) {
        this.location = displayPosition;
    }

    public static class Builder {

        private String locationId;

        private String label;

        private AddressModel address;

        private int distance;

        private CoordinatesModel location;

        public Builder setLocationId(String locationId) {
            this.locationId = locationId;
            return this;
        }

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder setDistance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder setAddress(AddressModel address) {
            this.address = address;
            return this;
        }

        public Builder setLocation(CoordinatesModel displayPosition) {
            this.location = displayPosition;
            return this;
        }

        public LocationModel build() {
            return new LocationModel(this);
        }
    }

}