package com.example.apocalypseescape;

import androidx.fragment.app.FragmentActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class TopRank extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng[] locations;

//    private Fragment ranks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_rank);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.TopRankingFragment, new TopRanking()).commit();

        locations = new LatLng[3];


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        SharedPreferences preferences = getSharedPreferences("Prefs", 0);
        int firstPlace = preferences.getInt("firstPlace",0);
        if (firstPlace == 0)
            return;
        mMap = googleMap;
        for (int i = 0; i < locations.length; i++) {
            switch (i){
                case 0:
                    locations[i] = new LatLng(getDouble(preferences,"fLat",0.0f), getDouble(preferences,"fLon",0.0f));
                    mMap.addMarker(new MarkerOptions().position(locations[i]).title((i + 1) + " Place: " + preferences.getString("firstPlaceName","")).icon(BitmapDescriptorFactory.defaultMarker()));
                    break;
                case 1:
                    locations[i] = new LatLng(getDouble(preferences,"sLat",0.0f), getDouble(preferences,"sLon",0.0f));
                    mMap.addMarker(new MarkerOptions().position(locations[i]).title((i + 1) + " Place: " + preferences.getString("secondPlaceName","")).icon(BitmapDescriptorFactory.defaultMarker()));
                    break;
               case 2:
                    locations[i] = new LatLng(getDouble(preferences,"tLat",0.0f), getDouble(preferences,"tLon",0.0f));
                   mMap.addMarker(new MarkerOptions().position(locations[i]).title((i + 1) + " Place: " + preferences.getString("thirdPlaceName","")).icon(BitmapDescriptorFactory.defaultMarker()));
                   break;
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[0], 18.0f));
        TextView First = findViewById(R.id.FirstPlace);
First.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
                  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[0], 18.0f));

    }
});   TextView Second = findViewById(R.id.SecondPlace);
Second.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
                  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[1], 18.0f));

    }
});   TextView Third = findViewById(R.id.ThirdPlace);
Third.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
                  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[2], 18.0f));

    }
});
    }


    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue)
    { return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));}

}
