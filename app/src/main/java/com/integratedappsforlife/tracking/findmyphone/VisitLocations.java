package com.integratedappsforlife.tracking.findmyphone;

/*
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

public class VisitLocations extends AppCompatActivity
        implements GoogleMap.OnMarkerDragListener, StreetViewPanorama.OnStreetViewPanoramaChangeListener {

    private static final String MARKER_POSITION_KEY = "MarkerPosition";

        //Toast.makeText(this,"VisitLocations fine ",Toast.LENGTH_LONG).show();
        static String CurrentString= SettingSaved.Userlocationinthemap;
        static String[] separated1 = CurrentString.split("%");
        static String MyLogLat=separated1[1].substring(29,separated1[1].length()-1);
        static String[] separated = MyLogLat.split(",");
        static double lat=Double.parseDouble(separated[0]);
        static double  lag=Double.parseDouble( separated[1]);
    // George St, Sydney
    private static final LatLng SYDNEY = new LatLng(lat, lag);


    private StreetViewPanorama mStreetViewPanorama;

    private Marker mMarker;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_locations);
        Toast.makeText(this, ""+ lat,Toast.LENGTH_LONG);
        final LatLng markerPosition;
        if (savedInstanceState == null) {
            markerPosition = SYDNEY;
        } else {
            markerPosition = savedInstanceState.getParcelable(MARKER_POSITION_KEY);
        }

        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment)
                        getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                new OnStreetViewPanoramaReadyCallback() {
                    @Override
                    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                        mStreetViewPanorama = panorama;
                        mStreetViewPanorama.setOnStreetViewPanoramaChangeListener(
                                VisitLocations.this);
                        // Only need to set the position once as the streetview fragment will maintain
                        // its state.
                        if (savedInstanceState == null) {
                            mStreetViewPanorama.setPosition(SYDNEY);
                        }
                    }
                });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                map.setOnMarkerDragListener(VisitLocations.this);
                // Creates a draggable marker. Long press to drag.
                mMarker = map.addMarker(new MarkerOptions()
                        .position(markerPosition)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.dmapsmall))
                        .draggable(true));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MARKER_POSITION_KEY, mMarker.getPosition());
    }

    @Override
    public void onStreetViewPanoramaChange(StreetViewPanoramaLocation location) {
        if (location != null) {
            mMarker.setPosition(location.position);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mStreetViewPanorama.setPosition(marker.getPosition(), 150);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }


}

*/


import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class VisitLocations extends FragmentActivity implements OnStreetViewPanoramaReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_locations);


        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.map);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
        //Toast.makeText(this,"everything is fine ",Toast.LENGTH_LONG).show();
       // onStreetViewPanoramaReady(streetViewPanoramaFragment);
    }

    @Override
    public void  onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        try{
            //Toast.makeText(this,"VisitLocations fine ",Toast.LENGTH_LONG).show();
            String CurrentString= SettingSaved.Userlocationinthemap;
            String[] separated1 = CurrentString.split("%");
            String MyLogLat=separated1[1].substring(29,separated1[1].length()-1);
            String[] separated = MyLogLat.split(",");
            double lat=Double.parseDouble(separated[0]);
            double lag=Double.parseDouble( separated[1]);
           // panorama.setPosition(new LatLng(lat,lag));

            //LatLng sydney = new LatLng(lat,lag);

            //-33.87365, 151.20689

            panorama.setPosition(new LatLng(lat,lag),500);
            //Toast.makeText(this," "+lat+" "+lag,Toast.LENGTH_LONG).show();
            //Log.e("lat and lag ",lat+"  "+ lag);
        }
        catch ( Exception ex){

            //// Toast.makeText(this,"VisitLocations wrong ",Toast.LENGTH_LONG).show();
            //  String x= String.valueOf(ex.getStackTrace()).toString();
            //  Toast.makeText(this,"error is "+ex+"   "+x,Toast.LENGTH_LONG).show();
        }
    }
}

