package br.com.angeloorrico.ctw.codechallengectw.models;

import java.io.Serializable;

import io.realm.RealmObject;

public class AddressModel extends RealmObject implements Serializable {

    private String country;

    private String state;

    private String city;

    private String street;

    private String houseNumber;

    private String postalCode;

    public AddressModel() {
    }

    private AddressModel(final Builder builder) {
        this.country = builder.country;
        this.state = builder.state;
        this.city = builder.city;
        this.street = builder.street;
        this.houseNumber = builder.houseNumber;
        this.postalCode = builder.postalCode;
    }

    public String getCountry() {
        return country == null ? "" : country + " ";
    }

    public String getState() {
        return state == null ? "" : state + " ";
    }

    public String getCity() {
        return city == null ? "" : city + " ";
    }

    public String getStreet() {
        return street == null ? "" : street + " ";
    }

    public String getHouseNumber() {
        return houseNumber == null ? "" : houseNumber + " ";
    }

    public String getPostalCode() {
        return postalCode == null ? "" : postalCode + " ";
    }


    public static class Builder {

        private String country;

        private String state;

        private String city;

        private String street;

        private String houseNumber;

        private String postalCode;

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public AddressModel build() {
            return new AddressModel(this);
        }
    }

}