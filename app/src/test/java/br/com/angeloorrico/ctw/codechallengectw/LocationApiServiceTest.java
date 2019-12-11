package br.com.angeloorrico.ctw.codechallengectw;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import br.com.angeloorrico.ctw.codechallengectw.models.responses.LocationDetailsResponse;
import br.com.angeloorrico.ctw.codechallengectw.models.responses.LocationsResponse;
import br.com.angeloorrico.ctw.codechallengectw.viewmodels.LocationViewModelFactory;
import br.com.angeloorrico.ctw.codechallengectw.viewmodels.LocationsListViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.angeloorrico.ctw.codechallengectw.viewmodels.LocationViewModelFactory.LIST_LOCATIONS_VIEW_MODEL_TYPE;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Local unit tests, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LocationApiServiceTest extends AbstractApiService {

    @Mock
    Call<LocationsResponse> callLocationsResponse;

    @Mock
    Call<LocationDetailsResponse> callLocationDetailsResponse;

    @Mock
    Application application;

    @Before
    public void setup() {
        try {
            MockitoAnnotations.initMocks(this);

            listViewModel = (LocationsListViewModel)LocationViewModelFactory.getLocationViewModel(
                    LIST_LOCATIONS_VIEW_MODEL_TYPE, application);


            when(apiInterface.getLocations("", "", 20,
                    "","", "", ""))
                    .thenReturn(callLocationsResponse);

            when(apiInterface.getLocationDetail("", "",
                    0, 0, "NT-76395763"))
                    .thenReturn(callLocationDetailsResponse);


        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldApiCallRespond() {
        Mockito.doAnswer(invocation -> {
            Callback<LocationsResponse> callback = invocation.getArgument(0, Callback.class);

            callback.onResponse(callLocationsResponse, Response.success(new LocationsResponse()));

            return null;
        }).when(callLocationsResponse).enqueue(any(Callback.class));
    }

    @Test
    public void shouldApiCallFail() {
        Mockito.doAnswer(invocation -> {
            Callback<LocationsResponse> callback = invocation.getArgument(0, Callback.class);

            callback.onFailure(callLocationsResponse, new IOException());

            return null;
        }).when(callLocationsResponse).enqueue(any(Callback.class));
    }

    @Test
    public void shouldSearchLocationsReturnsResult() {
        try {
            assertNotNull(apiInterface.getLocations("", "", 20,
                    "", "", "", ""));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldSearchLocationsReturnsNoResult() {
        try {
            assertNull(apiInterface.getLocations("", "", 20,
                    "", "", "", "asdfghjkl"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldGetLocationDetailsReturnsResult() {
        try {
            assertNotNull(apiInterface.getLocationDetail("", "", 0,
                    0, "NT-76395763"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldGetLocationDetailsReturnsNoResult() {
        try {
            assertNull(apiInterface.getLocationDetail("", "", 0,
                    0, "NT-2100"));
        } catch (Exception e) {
            fail();
        }
    }

}