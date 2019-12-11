package br.com.angeloorrico.ctw.codechallengectw.ui;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.lifecycle.ViewModelProviders;
import br.com.angeloorrico.ctw.codechallengectw.R;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;
import br.com.angeloorrico.ctw.codechallengectw.utils.Utils;
import br.com.angeloorrico.ctw.codechallengectw.viewmodels.LocationDetailsViewModel;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_LOCATION         = "location";
    private static final String EXTRA_CAMERA_POSITION = "camera_position";
    private static final int MAP_ZOOM = 15;

    private LocationDetailsViewModel locationDetailsViewModel;

    private LocationModel location;
    private GoogleMap map;
    private CameraPosition cameraPosition;

    private TextView tvStreet, tvPostalCode, tvCoordinates, tvDistance;
    private ImageView ivFavorite;

    private boolean isLocationPersisted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (!Utils.checkPlayServices(this)) {
            finish();
        }

        if (savedInstanceState != null) {
            cameraPosition = savedInstanceState.getParcelable(EXTRA_CAMERA_POSITION);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_LOCATION)) {
            location = (LocationModel) intent.getSerializableExtra(EXTRA_LOCATION);
        }

        if (location == null) {
            showMessage(getString(R.string.an_error_occurred));
            return;
        }

        initialization();

        observeLocation();
    }

    /**
     * initialization of views, view model and map fragment
     */
    private void initialization() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(location.getPlainLabel());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setRetainInstance(true);

        tvStreet = findViewById(R.id.tv_street);
        tvPostalCode = findViewById(R.id.tv_postal_code);
        tvCoordinates = findViewById(R.id.tv_coordinates);
        tvDistance = findViewById(R.id.tv_distance);
        ivFavorite = findViewById(R.id.iv_favorite);
        ivFavorite.setOnClickListener(view -> {
            Utils.runViewAnimation(view);
            updateFavorites();
        });

        // view model
        locationDetailsViewModel = ViewModelProviders.of(this).get(LocationDetailsViewModel.class);

        isLocationPersisted = locationDetailsViewModel.isLocationPersisted(location.getLocationId());
        setFavoriteIcon();
    }

    /**
     * observes changes in the location details
     */
    private void observeLocation() {
        locationDetailsViewModel.getLocationDetailsResponse().observe(this, locationDetails -> {
            if (locationDetails != null) {
                this.location.setLocation(locationDetails.getLocation());

                updateLocationData();
            }
        });
    }

    private void setFavoriteIcon() {
        if (isLocationPersisted)
            ivFavorite.setBackground(getResources()
                    .getDrawable(R.drawable.remove_favorite_transition));
        else
            ivFavorite.setBackground(getResources()
                    .getDrawable(R.drawable.favorite_transition));
    }

    private void updateFavorites() {
        TransitionDrawable transition = (TransitionDrawable) ivFavorite.getBackground();
        String msg;
        if (isLocationPersisted) {
            transition.reverseTransition(400);

            locationDetailsViewModel.deleteLocation(location.getLocationId());
            isLocationPersisted = false;
            msg = getString(R.string.location_removed_favorites);
        } else {
            transition.startTransition(400);

            locationDetailsViewModel.saveLocation(location);
            isLocationPersisted = true;
            msg = getString(R.string.location_added_favorites);
        }
        Snackbar.make(ivFavorite, msg, Snackbar.LENGTH_SHORT).show();
        setFavoriteIcon();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        if (Utils.hasInternetConnection(this))
            locationDetailsViewModel.fetchLocationDetails(location.getLocationId());

        updateLocationData();
    }

    private void updateLocationData() {
        tvStreet.setText(Html.fromHtml(location.getAddress().getStreet()).toString());
        tvPostalCode.setText(Html.fromHtml(location.getAddress().getPostalCode()).toString());
        tvDistance.setText(location.getDistance() + "m");

        if (location.getLocation() != null) {
            tvCoordinates.setText(location.getLocation().getLongitude() + ", "
                    + location.getLocation().getLongitude());

            LatLng point = new LatLng(
                    location.getLocation().getLatitude(),
                    location.getLocation().getLongitude());
            if (map != null) {
                map.addMarker(new MarkerOptions().position(point)
                        .title(location.getPlainLabel()));

                if (cameraPosition != null)
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                else
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, MAP_ZOOM));
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(EXTRA_CAMERA_POSITION, map.getCameraPosition());
            super.onSaveInstanceState(outState);
        }
    }
}
