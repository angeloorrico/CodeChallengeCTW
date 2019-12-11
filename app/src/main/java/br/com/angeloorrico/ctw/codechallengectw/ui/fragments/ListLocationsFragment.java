package br.com.angeloorrico.ctw.codechallengectw.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.angeloorrico.ctw.codechallengectw.R;
import br.com.angeloorrico.ctw.codechallengectw.adapters.LocationAdapter;
import br.com.angeloorrico.ctw.codechallengectw.models.LocationModel;
import br.com.angeloorrico.ctw.codechallengectw.utils.Utils;
import br.com.angeloorrico.ctw.codechallengectw.viewmodels.LocationDetailsViewModel;

public class ListLocationsFragment extends AppCompatDialogFragment implements OnMapReadyCallback {

    private static final String EXTRA_SELECTED_LOCATION = "selected_location";
    private static final String EXTRA_CAMERA_POSITION   = "camera_position";
    private static final int MAP_ZOOM = 18;

    private RecyclerView recyclerView;
    private TextView tvMessage;
    private LinearLayoutManager layoutManager;

    private GoogleMap map;
    private CameraPosition cameraPosition;

    private LocationAdapter adapter;
    private ArrayList<LocationModel> favoritedLocations = new ArrayList<>();
    private LocationDetailsViewModel locationsViewModel;

    int selectedPosition = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Light_Dialog_Alert);

        if (!Utils.checkPlayServices(getActivity())) {
            dismiss();
        }

        if (savedInstanceState != null) {
            cameraPosition = savedInstanceState.getParcelable(EXTRA_CAMERA_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialization();

        observeLocations();
    }

    /**
     * initialization of views and view model
     */
    private void initialization() {
        recyclerView = getView().findViewById(R.id.rv);
        tvMessage = getView().findViewById(R.id.tv_message);

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setRetainInstance(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        AppCompatButton btClose = getView().findViewById(R.id.bt_close);
        btClose.setOnClickListener(v -> {
            dismiss();
        });

        adapter = new LocationAdapter(favoritedLocations, LocationAdapter.TYPE_FAVORITE);
        adapter.setOnItemClickListener(location -> {
            showLocationOnMap(location);
        });
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        // view model
        locationsViewModel = ViewModelProviders.of(this).get(LocationDetailsViewModel.class);
    }

    /**
     * observes changes in the locations list
     */
    private void observeLocations() {
        locationsViewModel.getLocationsList().observe(this, locations -> {
            if (locations != null) {
                favoritedLocations.clear();

                favoritedLocations.addAll(locations);
                adapter.notifyDataSetChanged();

                if (favoritedLocations.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    tvMessage.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText(getString(R.string.no_favorited_locations));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        locationsViewModel.fetchFavoritedLocations();

        if (selectedPosition > -1) {
            showLocationOnMap(favoritedLocations.get(selectedPosition));
        }
    }

    private void showLocationOnMap(LocationModel location) {
        if (map != null) {
            map.clear();
            LatLng point = new LatLng(
                    location.getLocation().getLatitude(),
                    location.getLocation().getLongitude());

            map.addMarker(new MarkerOptions().position(point)
                    .title(location.getPlainLabel()));

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, MAP_ZOOM));
            cameraPosition = map.getCameraPosition();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(EXTRA_SELECTED_LOCATION, adapter.getSelectedPosition());
        if (map != null) {
            outState.putParcelable(EXTRA_CAMERA_POSITION, cameraPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(EXTRA_SELECTED_LOCATION);
        }
    }

}