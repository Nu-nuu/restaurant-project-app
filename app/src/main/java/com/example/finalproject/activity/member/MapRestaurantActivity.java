package com.example.finalproject.activity.member;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapRestaurantActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private TextView textName;
    private TextView textDistance;
    private ImageView imageView;

    private double latitude;
    private double longitude;
    private String name;
    private String distance;
    private String imageRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_restaurant);

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get the restaurant details from the intent
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);
        name = getIntent().getStringExtra("name");
        distance = getIntent().getStringExtra("distance");
        imageRes = getIntent().getStringExtra("image");

        // Initialize views
        mapView = findViewById(R.id.mapView);
        textName = findViewById(R.id.textName);
        textDistance = findViewById(R.id.textDistance);
        imageView = findViewById(R.id.imageView);

        // Set the restaurant details
        textName.setText(name);
        textDistance.setText(distance);

        // Load and display the image using Picasso
        String imageUrl = "android.resource://com.example.finalproject/drawable/" + imageRes;
        Picasso.get().load(Uri.parse(imageUrl)).into(imageView);

        // Initialize the map view
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Add a marker for the restaurant location
        LatLng restaurantLocation = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(restaurantLocation).title(name));

        // Move the camera to the restaurant location and zoom in
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 15f));

        // Fetch the route from the default location to the restaurant location
        LatLng defaultLocation = new LatLng(10.876148039416597, 106.80127449652109);
        fetchRoute(defaultLocation, restaurantLocation);
    }

    private void fetchRoute(LatLng origin, LatLng destination) {
        String apiKey = "AIzaSyBni6-rtAKoLXppJwLZJTpLEfdCGtvRuK4";
        String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + "&key=" + apiKey;
        Log.d("Req","" + requestUrl);

        FetchRouteTask fetchRouteTask = new FetchRouteTask();
        fetchRouteTask.execute(requestUrl);
    }

    private void drawRouteOnMap(List<LatLng> points) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(points)
                .width(5)
                .color(Color.BLUE);

        googleMap.addPolyline(polylineOptions);
    }

    private class FetchRouteTask extends AsyncTask<String, Void, List<LatLng>> {

        @Override
        protected List<LatLng> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String requestUrl = params[0];
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(requestUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder responseBuilder = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }

                    JSONObject responseJson = new JSONObject(responseBuilder.toString());
                    return parseRoutePoints(responseJson);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<LatLng> points) {
            if (points != null) {
                drawRouteOnMap(points);
            }
        }

        private List<LatLng> parseRoutePoints(JSONObject responseJson) throws JSONException {
            List<LatLng> points = new ArrayList<>();

            JSONArray routes = responseJson.getJSONArray("routes");
            if (routes.length() > 0) {
                JSONObject route = routes.getJSONObject(0);
                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                String pointsString = overviewPolyline.getString("points");
                return decodePolyline(pointsString);
            }

            return points;
        }

        private List<LatLng> decodePolyline(String encodedPolyline) {
            List<LatLng> points = new ArrayList<>();
            int index = 0;
            int length = encodedPolyline.length();
            int latitude = 0;
            int longitude = 0;

            while (index < length) {
                int b;
                int shift = 0;
                int result = 0;
                do {
                    b = encodedPolyline.charAt(index++) - 63;
                    result |= (b & 0x1F) << shift;
                    shift += 5;
                } while (b >= 0x20);

                int deltaLatitude = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                latitude += deltaLatitude;

                shift = 0;
                result = 0;
                do {
                    b = encodedPolyline.charAt(index++) - 63;
                    result |= (b & 0x1F) << shift;
                    shift += 5;
                } while (b >= 0x20);

                int deltaLongitude = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                longitude += deltaLongitude;

                LatLng point = new LatLng((latitude / 1E5), (longitude / 1E5));
                points.add(point);
            }

            return points;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
