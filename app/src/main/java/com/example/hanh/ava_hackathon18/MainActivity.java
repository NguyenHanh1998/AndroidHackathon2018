package com.example.hanh.ava_hackathon18;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanh.ava_hackathon18.adapter.PlaceAutocompleteAdapter;
import com.example.hanh.ava_hackathon18.androidbase.BaseActivity;
import com.example.hanh.ava_hackathon18.model.AnswerInsrtuct;
import com.example.hanh.ava_hackathon18.model.EndLocation;
import com.example.hanh.ava_hackathon18.model.Instruct;
import com.example.hanh.ava_hackathon18.model.Jam;
import com.example.hanh.ava_hackathon18.model.PlaceInfo;
import com.example.hanh.ava_hackathon18.model.StartLocation;
import com.example.hanh.ava_hackathon18.model.Step;
import com.example.hanh.ava_hackathon18.remote.ApiUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap map;
    private LocationManager locationManager;
    private static final String TAG = "MainActivity";
    private static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    private MapFragment mapFragment;
    private Location location = null;
    // private boolean CheckDirection = false;

    private StartLocation startLocation;
    private EndLocation endLocation;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient googleApiClient;
    private Marker marker;
    private PlaceInfo placeInfo;
    private Instruct instruct;
    private List<Step> stepList;
    private List<Jam> jamList;
    private Jam jam;
    private  Step step;
    private Double startLat;
    private Double startLng;
    private Double endLat;
    private Double endLng;
    private MKLoader loader;
    private PlacePicker placePicker;



    private List<Address> list = new ArrayList<>();
    private static final String CONTENT_TYPE = "application/json";
    private Address address;

    @BindView(R.id.btnActionFloat)
    FloatingActionButton btnFLoat;
    private AutoCompleteTextView edtDestination;
    @BindView(R.id.imgClearDesti)
    ImageView imgClear;
    private ViewGroup layoutDirection;
    private ImageView imgDetail;
    private ImageView imgNear;

    private LocationListener locationListener = new LocationListener() {
        @SuppressLint("DefaultLocale")
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.d(TAG, String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                drawMaker(location);
                locationManager.removeUpdates(locationListener);

            } else {
                Log.d(TAG, "Loaction is null");
            }

        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        edtDestination = (AutoCompleteTextView) findViewById(R.id.edtDesti);
        imgClear = (ImageView) findViewById(R.id.imgClearDesti);
        layoutDirection = (ViewGroup) findViewById(R.id.layoutDirection);
        imgDetail = (ImageView) findViewById(R.id.imgShowDetail);
        layoutDirection.setVisibility(View.GONE);
        loader = (MKLoader) findViewById(R.id.loading);
        imgNear = (ImageView) findViewById(R.id.imgPlaceBynear);
        //local = new Local();

        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDestination.setText("");
            }
        });

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);

        mapFragment.getMapAsync(this);


        //  map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initMap();
        getCurrentLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapFragment.onResume();
        getCurrentLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapFragment.onPause();
        locationManager.removeUpdates(locationListener);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        btnFLoat = (FloatingActionButton) findViewById(R.id.btnActionFloat);
        btnFLoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
        // map.setMyLocationEnabled(true);

        initMap();
        init();
    }

    private void init() {
        Log.d(TAG, "init:initializing");

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .build();

        edtDestination.setOnItemClickListener(autoCompleteListener);

        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, googleApiClient,
                LAT_LNG_BOUNDS, null);

        edtDestination.setAdapter(placeAutocompleteAdapter);

        edtDestination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }

                return false;
            }
        });

        imgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, " Click show InformationDetail");
                try {
                    if (marker.isInfoWindowShown()) {
                        marker.hideInfoWindow();
                    } else {
                        Log.d(TAG, "Click. Place info: " + address.toString());
                        marker.showInfoWindow();
                    }
                } catch (NullPointerException e) {
                    Log.e(TAG, "onClick: NullPoiterExeption + " + e.getMessage());
                }
            }
        });

        imgNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, "GooglePlayServicesRepairableException: " + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "GooglePlayServicesNotAvailableException: " + e.getMessage() );
                }

            }
        });


        BaseActivity.hideSoftKeyboard(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(googleApiClient, place.getId());
                placeResult.setResultCallback(updatePlaceCallback);
            }
        }
    }

    private void geoLocate() {
        BaseActivity.hideSoftKeyboard(this);
        Log.d(TAG, "geoLoacte: geoLocating");

        String searchString = edtDestination.getText().toString();

        Geocoder geocoder = new Geocoder(this);

        try {
            list = geocoder.getFromLocationName(searchString, 1);

        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOExeption: " + e.getMessage());
        }

        if (list.size() > 0) {

            address = list.get(0);
            Log.d(TAG, "geoLocate: found a location " + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
            imgDetail.setVisibility(View.VISIBLE);



            if (location == null) {
                Toast.makeText(getApplicationContext(), "please get your location!", Toast.LENGTH_LONG).show();
                return;
            } else {
                layoutDirection.setVisibility(View.VISIBLE);
                layoutDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng start = new LatLng(location.getLatitude(), location.getLongitude());
                        LatLng end = new LatLng(address.getLatitude(), address.getLongitude());
                        Log.i(TAG, "start: " + location.getLatitude() + location.getLongitude());
                        Log.i(TAG, "end : " + address.getLatitude() + address.getLongitude());

                        startLocation = new StartLocation(location.getLatitude(), location.getLongitude());
                        endLocation = new EndLocation(address.getLatitude(), address.getLongitude());

                        drawLine(start, end);
//                        Map<String, Map> coorDinate = new HashMap<>();
//                        Map<String, ArrayList> localMap = new HashMap<>();
//
//                        ArrayList<StartLocation> startLocations = new ArrayList<>();
//                        startLocations.add(startLocation);
//                        localMap.put("start_location", startLocations);
//
//                        ArrayList<EndLocation> endLocations = new ArrayList<>();
//                        endLocations.add(endLocation);
//                        localMap.put("end_location", endLocations);
//
//                        coorDinate.put("Local", localMap);
//                        callApiLocation(coorDinate);
//                        drawLine(start, end);
                        //route(start, end);
                    }
                });

            }

        }

    }

    private void drawLine(LatLng start, LatLng end) {
        ApiUtils.getMapService().getWaypoint(start.latitude, start.longitude, end.latitude, end.longitude)
                .enqueue(new Callback<AnswerInsrtuct>() {
                    @Override
                    public void onResponse(Call<AnswerInsrtuct> call, Response<AnswerInsrtuct> response) {
                        AnswerInsrtuct answerInsrtuct = response.body();
                        if(answerInsrtuct != null && answerInsrtuct.getInstruct() != null) {
                            instruct = answerInsrtuct.getInstruct();
                            getListStep(instruct);
                        }
                    }

                    @Override
                    public void onFailure(Call<AnswerInsrtuct> call, Throwable t) {
                        Log.e(TAG, "onFailture: " + t.getMessage());
                    }
                });
    }

    private void getListStep(Instruct instruct) {
        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
        ArrayList<LatLng> listGeoJams = new ArrayList<LatLng>();

        stepList = instruct.getStep();
        step = new Step();
        for (int i = 0; i < stepList.size() ; i++) {
            step = stepList.get(i);
            startLat = step.getStartLocation().getLat();
            startLng = step.getStartLocation().getLng();
            endLat = step.getEndLocation().getLat();
            endLng = step.getEndLocation().getLng();
            listGeopoints.add(new LatLng(startLat, startLng));
            listGeopoints.add(new LatLng(endLat, endLng));
        }
        drawPoints(listGeopoints);

        jamList = instruct.getJams();
        jam = new Jam();
        for (int i = 0; i < jamList.size() ; i++) {
            jam = jamList.get(i);
            startLat = jam.getStartLocation().getLat();
            startLng = jam.getStartLocation().getLng();
            endLat = jam.getEndLocation().getLat();
            endLng = jam.getEndLocation().getLng();
            listGeoJams.add(new LatLng(startLat, startLng));
            listGeoJams.add(new LatLng(endLat, endLng));
        }
        drawPointsJam(listGeoJams);
    }

    private void drawPointsJam(ArrayList<LatLng> listGeoJams) {
        PolylineOptions rectLine = new PolylineOptions().width(10).color(getResources()
                .getColor(R.color.colorJam));
        for (int i = 0; i < listGeoJams.size(); i++) {
            rectLine.add(listGeoJams.get(i));
        }
        Polyline polylin = map.addPolyline(rectLine);
    }


    private void drawPoints(ArrayList<LatLng> listGeopoints) {
        PolylineOptions rectLine = new PolylineOptions().width(10).color(getResources()
                .getColor(R.color.colorDirection));
        for (int i = 0; i < listGeopoints.size(); i++) {
            rectLine.add(listGeopoints.get(i));
        }
        Polyline polylin = map.addPolyline(rectLine);
    }

//    protected void route(final LatLng sourcePosition, LatLng destPosition) {
//        Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                try {
//                    Document doc = (Document) msg.obj;
////                    GMapV2Direction md = new GMapV2Direction();
//                    ArrayList<LatLng> directionPoint =  getDirection(doc);
//                    PolylineOptions rectLine = new PolylineOptions().width(10).color(getResources()
//                            .getColor(R.color.colorDirection));
//                    for (int i = 0; i < directionPoint.size(); i++) {
//                        rectLine.add(directionPoint.get(i));
//                    }
//                    Polyline polylin = map.addPolyline(rectLine);
//                    getDurationText(doc);
//
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        };
//
//        new GMapV2Direction(handler, sourcePosition, destPosition).execute();
//    }

//
//    private void callApiLocation(Map coorDinate) {
//        ApiUtils.getMapService().createMap(CONTENT_TYPE, coorDinate)
//                .enqueue(new Callback<com.example.hanh.ava_android.model.Map>() {
//                    @Override
//                    public void onResponse(Call<com.example.hanh.ava_android.model.Map> call, Response<com.example.hanh.ava_android.model.Map> response) {
//
//                        Log.i(TAG, "onResponse: " + response);
//                    }
//
//                    @Override
//                    public void onFailure(Call<com.example.hanh.ava_android.model.Map> call, Throwable t) {
//                        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
//                    }
//                });
//
//    }


    private void initMap() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int googlePlayStatus = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            googleApiAvailability.getErrorDialog(this, googlePlayStatus, -1).show();
            finish();
        } else {
            if (map != null) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setAllGesturesEnabled(true);
            }
        }
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!(isGPSEnabled || isNetworkEnabled)) {
            Toast.makeText(this, R.string.error_location_provider, Toast.LENGTH_LONG).show();
        } else {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME,
                    LOCATION_UPDATE_MIN_DISTANCE, locationListener);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }

        if (location != null) {
            Log.d(TAG, String.format("getCurrentLocation(%f,%f)", location.getLatitude(), location.getLongitude()));
            drawMaker(location);
        }
    }

    private void drawMaker(Location location) {
        if (map != null) {
            map.clear();
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
//            map.addMarker(new MarkerOptions().position(gps).title("Current position"));
//            map.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 17));
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (listAddress != null && listAddress.size() > 0) {
                    String nameLocation = listAddress.get(0).getAddressLine(0);
                    Log.d(TAG, "drawMaker: found a location " + listAddress.toString());
                    moveCamera(gps, DEFAULT_ZOOM, nameLocation);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void moveCamera(LatLng latLng, float zoom, final String title) {
        Log.d(TAG, "moveCamera: moving camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        marker = map.addMarker(options);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.setTitle(title);
                return  false;
            }
        });

        BaseActivity.hideSoftKeyboard(this);
    }


    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {
        Log.d(TAG, "moveCamera: moving camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        map.clear();

        //map.setInfoWindowAdapter(new CustomInfoWindowAdapter(MainActivity.this));
        if (placeInfo != null) {
            String snippet = "Address: " + placeInfo.getAddress() + "\n"
                    + "Phone number: " + placeInfo.getPhoneNumber() + "\n"
                    + "Website: " + placeInfo.getWebsiteUri() + "\n"
                    + "Price Rating: " + placeInfo.getRating() + "\n";

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(placeInfo.getName())
                    .snippet(snippet);
            marker = map.addMarker(markerOptions);
        } else {
            map.addMarker(new MarkerOptions().position(latLng));
        }

        layoutDirection.setVisibility(View.VISIBLE);
        BaseActivity.hideSoftKeyboard(this);
    }


    //get GMapV2Direction




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //
    private AdapterView.OnItemClickListener autoCompleteListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BaseActivity.hideSoftKeyboard(getParent());

            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(updatePlaceCallback);
        }
    };

    private ResultCallback<PlaceBuffer> updatePlaceCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()) {
                Log.e(TAG, "Result: Place query did not complete successfully");
                places.release();
                return;
            }

            final Place place = places.get(0);


            placeInfo = new PlaceInfo();
            placeInfo.setName(place.getName().toString());
            placeInfo.setAddress(place.getAddress().toString());
            placeInfo.setId(place.getId());
            placeInfo.setLatlng(place.getLatLng());
            placeInfo.setRating(place.getRating());
            placeInfo.setPhoneNumber(place.getPhoneNumber().toString());
            placeInfo.setWebsiteUri(place.getWebsiteUri());

            Log.d(TAG, "onResult: " + place.getName() + "," + place.getAddress() + "," + place.getId()
                    + "," + place.getLatLng() + "," + place.getRating() + "," + place.getPhoneNumber() + place.getWebsiteUri());

            Log.d(TAG, "onResult: placeInfo : " + place.getName() + "," + place.getAddress() + "," + place.getId()
                    + "," + place.getLatLng() + "," + place.getRating() + "," + place.getPhoneNumber() + place.getWebsiteUri());

            moveCamera(new LatLng(place.getViewport().getCenter().latitude, place.getViewport()
                    .getCenter().longitude), DEFAULT_ZOOM, placeInfo);


            places.release();
        }
    };
}

