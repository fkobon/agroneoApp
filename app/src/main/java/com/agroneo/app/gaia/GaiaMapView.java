package com.agroneo.app.gaia;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.agroneo.app.R;
import com.agroneo.app.api.Api;
import com.agroneo.app.api.ApiResponse;
import com.agroneo.app.ui.ActionBarCtl;
import com.agroneo.app.utils.Json;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GaiaMapView extends MapView implements ApiResponse, OnMapReadyCallback, OnMapClickListener, OnCameraMoveStartedListener, OnCameraMoveListener, OnCameraMoveCanceledListener, OnCameraIdleListener {

    private final Map<String, Marker> markers = new HashMap<>();
    private GoogleMap map = null;
    private ActionBarCtl actionbar = null;
    private Api api = Api.build(this);

    public GaiaMapView(Context context, Bundle savedInstanceState) {
        super(context);
        onCreate(savedInstanceState);
        onResume();
        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getMapAsync(this);

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }


    @Override
    public void onCameraIdle() {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        Json data = new Json("action", "specimens");
        data.put("zoom", map.getCameraPosition().zoom);
        data.put("bounds", new Json().put("south", bounds.southwest.latitude).put("west", bounds.southwest.longitude).put("north", bounds.northeast.latitude).put("east", bounds.northeast.longitude));

        api.doPost("gaia", data);
        actionbar.show(1);
    }

    @Override
    public void apiError() {

    }

    @Override
    public void apiResult(Json response) {

        if (response == null) {
            return;
        }
        Map<String, Marker> markers_ = new HashMap<>(markers);
        markers.clear();
        for (Json zone : response.getListJson("result")) {
            if (zone.containsKey("specimens")) {
                for (Json specimen : zone.getListJson("specimens")) {
                    Json location = specimen.getJson("location");
                    List<Double> coordinates = location.getList("coordinates", Double.class);

                    MarkerOptions marker = new MarkerOptions().position(new LatLng(coordinates.get(1), coordinates.get(0))).title(specimen.getJson("species").getString("name")).snippet(specimen.getString("desc"));
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));

                    marker.zIndex(3);
                    String identifier = specimen.getId();
                    if (!markers_.containsKey(identifier)) {
                        markers.put(identifier, map.addMarker(marker));
                    } else {
                        markers.put(identifier, markers_.get(identifier));
                    }
                    markers_.remove(identifier);
                }
            } else if (zone.containsKey("count")) {

                Json location = zone.getJson("location");

                LatLngBounds.Builder bound = new LatLngBounds.Builder();

                if (location.getString("type", "").equals("MultiPolygon")) {
                    List<List<List<List<Double>>>> coordinates = (List<List<List<List<Double>>>>) location.get("coordinates");
                    for (List<List<List<Double>>> multi : coordinates) {
                        for (List<List<Double>> polys : multi) {
                            for (List<Double> poly : polys) {
                                bound.include(new LatLng(poly.get(1), poly.get(0)));
                            }
                        }
                    }
                } else {
                    List<List<List<Double>>> coordinates = (List<List<List<Double>>>) location.get("coordinates");
                    for (List<List<Double>> polys : coordinates) {

                        for (List<Double> poly : polys) {
                            bound.include(new LatLng(poly.get(1), poly.get(0)));
                        }
                    }
                }

                MarkerOptions marker = new MarkerOptions().position(bound.build().getCenter()).snippet("" + zone.getInteger("count"));//.position(new LatLng(coordinates.get(1), coordinates.get(0)))


                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.cluster));
                marker.zIndex(4);
                String identifier = zone.getJson("location").toString(true);
                if (!markers_.containsKey(identifier)) {
                    markers.put(identifier, map.addMarker(marker));
                } else {
                    markers.put(identifier, markers_.get(identifier));
                }

                markers_.remove(identifier);

            }
        }
        for (Marker marker : markers_.values()) {
            marker.remove();
        }
    }


    @Override
    public void onCameraMoveStarted(int reason) {

    }

    @Override
    public void onCameraMoveCanceled() {
    }

    @Override
    public void onCameraMove() {

        actionbar.hide();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        enableLocation();

        map.setOnMapClickListener(this);
        map.setOnCameraIdleListener(this);
        map.setOnCameraMoveStartedListener(this);
        map.setOnCameraMoveListener(this);
        map.setOnCameraMoveCanceledListener(this);

        map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(-19.222797032131915, 46.59959784328453)).zoom(7).build()));
        if (actionbar != null) {
            actionbar.show();
        }
    }

    private void enableLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }

    }

    public void setActionbar(ActionBarCtl actionbar) {
        this.actionbar = actionbar;
    }
}
