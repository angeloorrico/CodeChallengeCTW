package br.com.angeloorrico.ctw.codechallengectw.repositories;

import android.content.Context;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;
import io.realm.Realm;
import io.realm.RealmResults;

import static br.com.angeloorrico.ctw.codechallengectw.models.LocationModel.COLUMN_ID;

public class RealmRepository extends BaseRepository {

    private static RealmRepository realmRepository;

    private Realm realm;

    public RealmRepository(Context context) {
        Realm.init(context);
    }

    public static RealmRepository getInstance(Context context) {
        if (realmRepository == null) {
            realmRepository = new RealmRepository(context);
        }
        return realmRepository;
    }

    public void saveLocation(LocationModel location) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(location);
        realm.commitTransaction();
    }

    public void deleteLocation(String locationId) {
        realm = Realm.getDefaultInstance();
        final RealmResults<LocationModel> result = realm.where(LocationModel.class)
                .equalTo(COLUMN_ID, locationId)
                .findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteFirstFromRealm();
            }
        });
    }

    public void fetchFavoritedLocations(MutableLiveData<List<LocationModel>> data) {
        realm = Realm.getDefaultInstance();
        realm.where(LocationModel.class).findAllAsync().addChangeListener(locationModels -> {
            data.postValue(locationModels);
        });
    }

    public boolean isLocationFavorited(String locationId) {
        realm = Realm.getDefaultInstance();
        if (realm.where(LocationModel.class).equalTo(COLUMN_ID, locationId).findFirst() != null)
            return true;
        return false;
    }

    public void dispose() {
        if (realm != null) {
            realm.close();
            realm = null;
        }
    }

}