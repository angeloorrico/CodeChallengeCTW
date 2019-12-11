package br.com.angeloorrico.ctw.codechallengectw;

import android.content.Context;
import android.util.Log;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import br.com.angeloorrico.ctw.codechallengectw.models.AddressModel;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;
import br.com.angeloorrico.ctw.codechallengectw.repositories.RealmRepository;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Instrumented tests
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class RealmInstrumentedTest {

    Context context;

    Realm realm;
    RealmConfiguration testConfig;

    private RealmResults<LocationModel> locations;

    @Mock
    RealmRepository realmRepository;

    @Before
    public void setup() {
        try {
            context = InstrumentationRegistry.getInstrumentation().getTargetContext();

            Realm.init(context);

            /**
             * using an in memory Realm database so it is erased after each close operation
             */
            testConfig =
                    new RealmConfiguration.Builder().
                            inMemory().
                            name("test-realm").build();

            realm = Realm.getInstance(testConfig);
        } catch (Exception ex) {
            Log.e("RealmInstrumentedTest | setupDatabase", ex.getMessage());
        }
    }

    @After
    public void tearDown() {
        if (realm != null) {
            realm.close();
        }
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("br.com.angeloorrico.ctw.codechallengectw", appContext.getPackageName());
    }

    @Test
    public void shouldBeAbleToGetTestInstance() {
        assertThat(Realm.getInstance(testConfig), is(realm));
    }

    @Test
    public void shouldBeAbleToCreateARealmObject() {
        realm.beginTransaction();
        LocationModel loc = realm.createObject(LocationModel.class, UUID.randomUUID().toString());
        realm.commitTransaction();

        realm.close();

        assertThat(loc, is(instanceOf(LocationModel.class )));
    }

    @Test
    public void shouldBeAbleToCreateALocationObject() {
        realm.beginTransaction();
        LocationModel loc = realm.createObject(LocationModel.class, UUID.randomUUID().toString());
        loc.setLabel("Avenida Ademar de Barros");
        realm.commitTransaction();

        assertEquals(1, realm.where(LocationModel.class).count());
        assertEquals("Avenida Ademar de Barros", realm.where(LocationModel.class).findFirst().getLabel());

        realm.close();
    }

    @Test
    public void shouldBeAbleToPersistLocationDetails() {
        realm.beginTransaction();
        LocationModel loc = realm.createObject(LocationModel.class, UUID.randomUUID().toString());
        AddressModel address = new AddressModel.Builder()
                .setState("Bahia")
                .setCity("Salvador")
                .setStreet("Rua Gilberto Amado")
                .setHouseNumber("360")
                .setCountry("Brasil")
                .setPostalCode("41750110")
                .build();
        loc.setAddress(realm.copyToRealm(address));
        realm.commitTransaction();

        assertNotNull("Test Location Label", realm.where(LocationModel.class)
                .findFirst().getAddress());

        realm.close();
    }

    @Test
    public void shouldBeAbleToDeleteALocationObject() {
        realm.beginTransaction();
        realm.createObject(LocationModel.class, UUID.randomUUID().toString());
        realm.commitTransaction();

        assertEquals(1, realm.where(LocationModel.class).count());

        realm.close();

        realm = Realm.getInstance(testConfig);
        assertEquals(0, realm.where(LocationModel.class).count());
    }

}