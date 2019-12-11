package br.com.angeloorrico.ctw.codechallengectw.repositories;

import java.util.Collections;
import java.util.List;

import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;

public class BaseRepository {

    public static final int SORT_BY_DISTANCE = 0;
    public static final int SORT_BY_NAME     = 1;

    public List<LocationModel> sortList(List<LocationModel> locationsList, int sortBy) {
        if (locationsList != null) {
            switch (sortBy) {
                case SORT_BY_DISTANCE:
                    Collections.sort(locationsList, (LocationModel m1, LocationModel m2) ->
                            m1.getDistance() - m2.getDistance());
                    break;
                case SORT_BY_NAME:
                    Collections.sort(locationsList, (LocationModel m1, LocationModel m2) ->
                            m1.getLabel().compareTo(m2.getLabel()));
                    break;
            }
        }

        return locationsList;
    }

}