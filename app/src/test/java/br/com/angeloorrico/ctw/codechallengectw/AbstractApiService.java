package br.com.angeloorrico.ctw.codechallengectw;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import br.com.angeloorrico.ctw.codechallengectw.networking.LocationApi;
import br.com.angeloorrico.ctw.codechallengectw.viewmodels.LocationDetailsViewModel;
import br.com.angeloorrico.ctw.codechallengectw.viewmodels.LocationsListViewModel;
import retrofit2.Retrofit;

public abstract class AbstractApiService {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    LocationsListViewModel listViewModel;

    @Mock
    LocationDetailsViewModel detailsViewModel;

    @Mock
    LocationApi apiInterface;

    Retrofit retrofit;

    LocationApi mockApiService;

    @Before
    public abstract void setup();

}