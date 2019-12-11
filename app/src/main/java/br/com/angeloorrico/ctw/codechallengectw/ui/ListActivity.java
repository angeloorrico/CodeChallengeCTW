package br.com.angeloorrico.ctw.codechallengectw.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.angeloorrico.ctw.codechallengectw.R;
import br.com.angeloorrico.ctw.codechallengectw.adapters.LocationAdapter;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;
import br.com.angeloorrico.ctw.codechallengectw.ui.fragments.ListLocationsFragment;
import br.com.angeloorrico.ctw.codechallengectw.utils.Utils;
import br.com.angeloorrico.ctw.codechallengectw.viewmodels.LocationsListViewModel;
import br.com.angeloorrico.ctw.codechallengectw.viewmodels.DeviceLocationViewModel;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static br.com.angeloorrico.ctw.codechallengectw.repositories.BaseRepository.SORT_BY_DISTANCE;
import static br.com.angeloorrico.ctw.codechallengectw.repositories.BaseRepository.SORT_BY_NAME;
import static br.com.angeloorrico.ctw.codechallengectw.utils.Utils.PERMISSION_REQUEST_DEVICE_LOCATION;
import static br.com.angeloorrico.ctw.codechallengectw.utils.Utils.REQUEST_LOCATION_ACTIVATION;

public class ListActivity extends AppCompatActivity {

    private static final String TAG_LIST_FRAGMENT = "list_fragment";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_SORT_BY = "sort_by";

    private RecyclerView recyclerView;
    private EditText etLocation;
    private TextView tvMessage;
    private LinearLayoutManager layoutManager;

    private LocationAdapter adapter;
    private ArrayList<LocationModel> locationsArrayList = new ArrayList<>();
    private LocationsListViewModel locationViewModel;
    private DeviceLocationViewModel deviceLocationViewModel;

    private Location deviceLocation;
    private int sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // retrieves the state of the last known location and sort preference
        if (savedInstanceState != null) {
            sortBy = savedInstanceState.getInt(KEY_SORT_BY);
            deviceLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        initialization();

        observeLocations();
    }

    /**
     * saves the state of the last known location and sort preference
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SORT_BY, sortBy);
        if (deviceLocation != null) {
            outState.putParcelable(KEY_LOCATION, deviceLocation);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * initialization of views and view model
     */
    private void initialization() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv);
        tvMessage = findViewById(R.id.tv_message);
        etLocation = findViewById(R.id.et_location);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            openListLocationsFragment();
        });

        etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchLocation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        layoutManager = new LinearLayoutManager(ListActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new LocationAdapter(locationsArrayList, LocationAdapter.TYPE_LIST);
        adapter.setOnItemClickListener(location -> {
            openDetailsActivity(location);
        });
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        // view models
        locationViewModel = ViewModelProviders.of(this).get(LocationsListViewModel.class);
        deviceLocationViewModel = ViewModelProviders.of(this).get(DeviceLocationViewModel.class);

        if (Utils.askForLocationPermission(this)) {
            Utils.checkGPSConfiguration(this);
            observeTracking();
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(getString(R.string.search_for_results));
        }
    }

    /**
     * observes changes in the locations list
     */
    private void observeLocations() {
        locationViewModel.getLocationsList().observe(this, locations -> {
            locationsArrayList.clear();
            if (locations != null) {
                locationsArrayList.addAll(locations);

                if (locationsArrayList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    tvMessage.setVisibility(View.INVISIBLE);
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText(getString(R.string.no_location_found));
                }
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText(getString(R.string.search_for_results));
            }
            adapter.notifyDataSetChanged();
        });
    }

    /**
     * observes changes and get the actual device location (single location update only)
     */
    private void observeTracking() {
        deviceLocationViewModel.getDeviceLocation().observe(this, deviceLocation -> {
            this.deviceLocation = deviceLocation;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        switch (sortBy) {
            case SORT_BY_DISTANCE:
                menu.findItem(R.id.mn_sort_by_distance).setChecked(true);
                break;
            case SORT_BY_NAME:
                menu.findItem(R.id.mn_sort_by_name).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(item.isChecked());

        int id = item.getItemId();
        if (id == R.id.mn_sort_by_distance) {
            locationViewModel.sortList(SORT_BY_DISTANCE);
            sortBy = SORT_BY_DISTANCE;
        } else if (id == R.id.mn_sort_by_name) {
            locationViewModel.sortList(SORT_BY_NAME);
            sortBy = SORT_BY_NAME;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Utils.checkGPSConfiguration(this);
        switch (requestCode) {
            case PERMISSION_REQUEST_DEVICE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    observeTracking();
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText(getString(R.string.no_location_permission));
                }
            }
        }
    }

    private void searchLocation(String query) {
        if (query.isEmpty() || query.trim().isEmpty()) {
            locationViewModel.clearLocationsList();
            return;
        }
        if (!Utils.isGPSEnabled(this)) {
            Utils.checkGPSConfiguration(this);

            //locationViewModel.clearLocationsList();
            recyclerView.setVisibility(View.INVISIBLE);
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(getString(R.string.no_location_disabled));
            return;
        }

        if (Utils.hasInternetConnection(this)) {
            recyclerView.setVisibility(View.VISIBLE);
            tvMessage.setVisibility(View.INVISIBLE);

            String coordinates = "";
            if (deviceLocation != null)
                coordinates = deviceLocation.getLatitude() + ","
                        + deviceLocation.getLongitude();

            locationViewModel.searchLocations(query, coordinates, sortBy);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(getString(R.string.no_internet_connection));
        }
    }

    /**
     * opens the location details activity
     */
    private void openDetailsActivity(LocationModel location) {
        if (Utils.hasInternetConnection(this)) {
            Intent intent = new Intent(ListActivity.this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_LOCATION, location);
            startActivity(intent);
        } else {
            Toast.makeText(this,
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * opens the favorited locations fragment
     */
    private void openListLocationsFragment() {
        ListLocationsFragment dialogFragment = new ListLocationsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_LIST_FRAGMENT);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, TAG_LIST_FRAGMENT);
    }

    @Override
    protected void onResume() {
        if (Utils.isAccessFineLocationPermissionGranted(this)) {
            observeTracking();
        }
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION_ACTIVATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        deviceLocationViewModel.init();
                        /**
                         * a second of waiting before getting the first location
                         */
                        new Handler().postDelayed (() -> {
                            searchLocation(etLocation.getText().toString());
                        }, 1000);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

}