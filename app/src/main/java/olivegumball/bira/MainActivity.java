package olivegumball.bira;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String beers;
    private String[] beer_array;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(sharedPref.getInt("first", 1)==1){
            Intent in = new Intent(this, SettingsActivity.class);
            startActivity(in);
            this.finish();
        }
        beers = sharedPref.getString("beers", "בקס");
        beer_array = beers.split(",");

        setContentView(R.layout.activity_main);

        Typeface alef = Typeface.createFromAsset(this.getAssets(), "Alef-Regular.ttf");

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout)).setTitle("בירה");
        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout)).setCollapsedTitleTypeface(alef);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<CardInfo> cardInfo = Arrays.asList(new CardInfo("טוען תוצאות...", "", "", getDrawable(R.drawable.big_beer),0,0));
        mAdapter = new MyAdapter(cardInfo, MainActivity.this.getAssets());
        mRecyclerView.setAdapter(mAdapter);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        }else {
            setLocationField();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    setLocationField();

                } else {
                    this.finish();
                    System.exit(0);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void setLocationField(){
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(final Location location) {
                // Called when a new location is found by the network location provider.
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = "http://46.101.189.104/get.php?radius=5000&lat=" + location.getLatitude() + "&long=" + location.getLongitude();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                try {
                                    JSONObject json = new JSONObject(response);
                                    JSONObject tmp;
                                    int iterator = 0;

                                    List<CardInfo> cardInfo = new LinkedList<CardInfo>(Arrays.asList(new CardInfo("אין תוצאות!", "", "", getDrawable(R.drawable.big_beer), 0, 0)));
                                    Iterator<String> keys = json.keys();
                                    while(keys.hasNext()){
                                        String key = (String)keys.next();
                                        if (json.get(key) instanceof JSONObject){
                                            double dis = distance(location.getLatitude(), location.getLongitude(), ((JSONObject) json.get(key)).getDouble("lat"), ((JSONObject) json.get(key)).getDouble("long"));
                                            DecimalFormat df = new DecimalFormat("#.#");
                                            String dist = df.format(dis);
                                            Drawable d;
                                            String beer;
                                            try {
                                                beer = URLDecoder.decode(((JSONObject) json.get(key)).getString("beer"), "UTF-8");
                                            } catch (UnsupportedEncodingException uee) {
                                                //nofthing
                                                beer = "";
                                            }
                                            int check = 0;
                                            for(int x = 0; x < beer_array.length; x++){
                                                if(beer.equals(beer_array[x])){
                                                   check++;
                                                }
                                            }
                                            if (check==0) {
                                                continue;
                                            }
                                            if(beer.equals("קרלסברג")){
                                                d = getDrawable(R.drawable.carlsberg);
                                            } else if(beer.equals("בקס")) {
                                                d = getDrawable(R.drawable.becks);
                                            } else if(beer.equals("קורונה")) {
                                                d = getDrawable(R.drawable.corona);
                                            } else if(beer.equals("גינס")) {
                                                d = getDrawable(R.drawable.guiness);
                                            } else if(beer.equals("היינקן")) {
                                                d = getDrawable(R.drawable.heiniken);
                                            } else if(beer.equals("מרטנס פילס")) {
                                                d = getDrawable(R.drawable.martens);
                                            } else if(beer.equals("ניוקאסל")) {
                                                d = getDrawable(R.drawable.newcastle);
                                            } else if(beer.equals("פאולנר")) {
                                                d = getDrawable(R.drawable.paulaner);
                                            } else if(beer.equals("סאמואל אדאמס")) {
                                                d = getDrawable(R.drawable.samadams);
                                            }else{
                                                d = getDrawable(R.drawable.big_beer);
                                            }
                                            cardInfo.add(new CardInfo(beer, dist+" ק\"מ", ((JSONObject) json.get(key)).getString("price")+" ש\"ח", d, ((JSONObject) json.get(key)).getDouble("lat"), ((JSONObject) json.get(key)).getDouble("long")));
                                        }
                                    }
                                    if (cardInfo.size() > 1){
                                        cardInfo.remove(0);
                                    }
                                    mAdapter = new MyAdapter(cardInfo, MainActivity.this.getAssets());
                                    mRecyclerView.setAdapter(mAdapter);
                                } catch (JSONException je){
                                    Log.d("json", "failed...");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //SHIT
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }


    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


}
