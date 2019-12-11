package br.com.angeloorrico.ctw.codechallengectw;

import org.junit.Test;
import org.mockito.MockitoAnnotations;

import br.com.angeloorrico.ctw.codechallengectw.models.responses.LocationDetailsResponse;
import br.com.angeloorrico.ctw.codechallengectw.models.responses.LocationsResponse;
import br.com.angeloorrico.ctw.codechallengectw.networking.LocationApi;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Local unit tests, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LocationApiServiceMockTest extends AbstractApiService {

    private MockRetrofit mockRetrofit;

    private NetworkBehavior behavior;

    public void setup() {
        MockitoAnnotations.initMocks(this);

        /**
         * this is a mock, so the Base Url can be anything
         */
        retrofit = new Retrofit.Builder().baseUrl("http://apis.here.com/")
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        behavior = NetworkBehavior.create();
        mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();

        BehaviorDelegate<LocationApi> delegate = mockRetrofit.create(LocationApi.class);
        mockApiService = new MockLocationApiService(delegate);
    }

    @Test
    public void shouldLocationsListHasValidResults() {
        try {
            Call<LocationsResponse> quote = mockApiService.getLocations("", "",
                    0, "", "", "", "");
            Response<LocationsResponse> response = quote.execute();

            when((listViewModel).searchLocations("", "", 0))
                    .thenReturn(response.body().getLocations());

            assertEquals(3, listViewModel.searchLocations("", "", 0).size());
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void shouldLocationDetailsHasValidResult() {
        try {
            Call<LocationDetailsResponse> quote = mockApiService.getLocationDetail("",
                    "", 0, 0, "");
            Response<LocationDetailsResponse> response = quote.execute();

            when((detailsViewModel).fetchLocationDetails("1"))
                    .thenReturn(response.body().getLocationDetail().getView().get(0).getResult().get(0));

            assertEquals("Rua Gilberto Amado", detailsViewModel
                    .fetchLocationDetails("1").getLabel());
        } catch (Exception ex) {
            fail();
        }
    }

}