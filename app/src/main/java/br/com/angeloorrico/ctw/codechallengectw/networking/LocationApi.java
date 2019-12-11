package br.com.angeloorrico.ctw.codechallengectw.networking;


import br.com.angeloorrico.ctw.codechallengectw.models.responses.LocationDetailsResponse;
import br.com.angeloorrico.ctw.codechallengectw.models.responses.LocationsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationApi {

    @GET("suggest.json")
    Call<LocationsResponse> getLocations(@Query("app_id") String appId,
                                         @Query("app_code") String appCode,
                                         @Query("maxresults") int maxresults,
                                         @Query("beginHighlight") String beginHighlight,
                                         @Query("endHighlight") String endHighlight,
                                         @Query("prox") String prox,
                                         @Query("query") String query);

    @GET("geocode.json")
    Call<LocationDetailsResponse> getLocationDetail(@Query("app_id") String appId,
                                                    @Query("app_code") String appCode,
                                                    @Query("gen") int gen,
                                                    @Query("jsonattributes") int jsonattributes,
                                                    @Query("locationid") String locationid);

}