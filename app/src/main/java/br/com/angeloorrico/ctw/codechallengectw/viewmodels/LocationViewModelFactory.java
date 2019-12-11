package br.com.angeloorrico.ctw.codechallengectw.viewmodels;

import android.app.Application;

public class LocationViewModelFactory {

    public static final int LIST_LOCATIONS_VIEW_MODEL_TYPE    = 0;
    public static final int DETAILS_LOCATIONS_VIEW_MODEL_TYPE = 1;

    public static LocationBaseViewModel getLocationViewModel(int type, Application application) {
        if (type == LIST_LOCATIONS_VIEW_MODEL_TYPE)
            return new LocationsListViewModel(application);
        else if (type == DETAILS_LOCATIONS_VIEW_MODEL_TYPE)
            return new LocationDetailsViewModel(application);

        return null;
    }

}