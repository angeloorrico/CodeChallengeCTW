package br.com.angeloorrico.ctw.codechallengectw;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import br.com.angeloorrico.ctw.codechallengectw.models.AddressModel;
import br.com.angeloorrico.ctw.codechallengectw.models.CoordinatesModel;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;
import br.com.angeloorrico.ctw.codechallengectw.repositories.BaseRepository;

import static org.junit.Assert.assertEquals;

/**
 * Local unit tests, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BaseRepositoryTest {

    @Mock
    BaseRepository baseRepository;

    LocationModel location1, location2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        baseRepository = new BaseRepository();

        AddressModel address1 = new AddressModel.Builder()
                .setState("Bahia")
                .setCity("Salvador")
                .setStreet("Rua Gilberto Amado")
                .setHouseNumber("364")
                .setCountry("Brasil")
                .setPostalCode("41750110")
                .build();
        location1 = new LocationModel.Builder()
                .setLocationId("NT_0gissZ8.a831N.wzrE8wzC_zYDN")
                .setLabel("Brasil, Salvador, Rua Gilberto Amado, 364")
                .setDistance(123)
                .setAddress(address1)
                .setLocation(new CoordinatesModel())
                .build();

        AddressModel address2 = new AddressModel.Builder()
                .setState("Pernambuco")
                .setCity("Recife")
                .setStreet("Avenida Beira Mar")
                .setHouseNumber("1739A")
                .setCountry("Brasil")
                .setPostalCode("47720230")
                .build();
        location2 = new LocationModel.Builder()
                .setLocationId("NT-6523957")
                .setLabel("Av. Adelaide da Costa Machado")
                .setDistance(50)
                .setAddress(address2)
                .setLocation(new CoordinatesModel())
                .build();
    }

    @Test
    public void shouldSortLocationsByDistance() {
        List<LocationModel> sorted = new ArrayList<>();
        sorted.add(location1);
        sorted.add(location2);
        sorted = baseRepository.sortList(sorted, BaseRepository.SORT_BY_DISTANCE);
        assertEquals(sorted.get(0), location2);
    }

    @Test
    public void shouldSortLocationsByName() {
        List<LocationModel> sorted = new ArrayList<>();
        sorted.add(location1);
        sorted.add(location2);
        sorted = baseRepository.sortList(sorted, BaseRepository.SORT_BY_NAME);
        assertEquals(sorted.get(0).getLabel(), location2.getLabel());
    }

}