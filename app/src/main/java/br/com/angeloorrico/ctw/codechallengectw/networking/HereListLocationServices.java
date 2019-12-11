package br.com.angeloorrico.ctw.codechallengectw.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HereListLocationServices {

    protected static final String BASE_URL = "http://autocomplete.geocoder.api.here.com/6.2/";

    protected static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}