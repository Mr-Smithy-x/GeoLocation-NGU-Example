/*
 * Copyright Mr Smithy x (c) 3/28/2015
 * This application is already released in the google playstore.
 * Under no circumstance may you modify this application to be released.
 * This project is just for you to learn of off.
 */

package com.geo;

import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.util.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
import android.support.v7.widget.Toolbar;
import android.view.View.*;
import android.view.*;
import android.content.*;

public class MainActivity extends ActionBarActivity implements OnResult, OnClickListener, Toolbar.OnMenuItemClickListener, View.OnKeyListener {

    @Override
    public boolean onKey(View p1, int p2, KeyEvent p3) {
        switch (p1.getId()) {
            case R.id.search_barEditText:
                if (p3.getAction() == KeyEvent.ACTION_DOWN && p3.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    onClick(p1);
                    return true;
                }
                return false;
        }
        return false;
    }


    @Override
    public void OnItemLongClick(View view, int position, Object object) {
        searchRecyclerAdapter.RemoveItem(position);
    }

    @Override
    public void OnError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemClick(View view, int position, Object object) {
        if (object instanceof List && !((List) object).isEmpty() && ((List) object).get(0) instanceof Location) {
            Location loc = (Location) ((List) object).get(position);
            new PopupInfo(loc).show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem p1) {
        switch (p1.getItemId()) {
            case R.id.action_settings:
                searchRecyclerAdapter.Clear();
                return true;
            case R.id.action_credits:
                Toast.makeText(this, "Mr Smithy x, FreeGeoIP API", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_exit:
                finish();
                break;
        }
        return false;
    }

    @Override
    public void onClick(View p1) {
        GetInfo get = new GetInfo(this);
        get.execute(url = searchTxt.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void OnCallBack(JSONObject obj) {
        System.out.println(obj.toString());
        String ip, country_code, country_name, region_code, region_name, city, zip_code, time_zone, latitude, longitude, metro_code;
        try {
            ip = obj.getString("ip");
            country_code = obj.getString("country_code");
            country_name = obj.getString("country_name");
            region_code = obj.getString("region_code");
            region_name = obj.getString("region_name");
            city = obj.getString("city");
            zip_code = obj.getString("zip_code");
            time_zone = obj.getString("time_zone");
            latitude = obj.getString("latitude");
            longitude = obj.getString("longitude");
            metro_code = obj.getString("metro_code");
            if (!ip.contains(":")) {
                Location location = new Location(url, ip, country_code, country_name
                        , region_code, region_name, city, zip_code, time_zone,
                        latitude, longitude, metro_code);
                searchRecyclerAdapter.InsertItem(location);
            } else {
                if (counter <= 3) {
                    System.out.println("Redoing");
                    onClick(null);
                    counter++;
                } else {
                    Toast.makeText(this, "After multiple tries i could not find the ip of the website", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Could not grab website info...", Toast.LENGTH_SHORT).show();
            Log.e("Damn", "Error", e);
        }
    }

    int counter = 0;
    String url;
    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText searchTxt;
    Button searchBtn;
    SearchRecyclerAdapter searchRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toolbar = (Toolbar) findViewById(R.id.le_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnCreateContextMenuListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.LocationRecyclerView);
        searchBtn = (Button) findViewById(R.id.search_barButton);
        searchTxt = (EditText) findViewById(R.id.search_barEditText);
        searchTxt.setOnKeyListener(this);
        searchBtn.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerGesture rg = new RecyclerGesture(this, recyclerView, this);
        searchRecyclerAdapter = new SearchRecyclerAdapter(this, this, new ArrayList<Location>());
        recyclerView.addOnItemTouchListener(rg);
        recyclerView.setAdapter(searchRecyclerAdapter);
        Toast.makeText(this, "Developed By Cj (Mr Smithy x) using FreeGeoIP API", Toast.LENGTH_LONG).show();
    }

    class GetInfo extends AsyncTask<Object, Integer, Object> {
        OnResult on;

        public GetInfo(OnResult on) {
            this.on = on;
        }

        @Override
        protected Object doInBackground(Object[] p1) {
            String ip = (p1.length > 0) ? ip = p1[0].toString() : null;
            try {
                URL url = new URL(String.format("https://freegeoip.net/json/%s", ip));
                HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
                httpUrl.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(httpUrl.getInputStream()));
                String b = null;
                StringBuilder sb = new StringBuilder();
                while ((b = br.readLine()) != null) {
                    sb.append(b);
                }
                br.close();
                return new JSONObject(sb.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            } catch (JSONException jsonex) {
                jsonex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "Could not load geo location";
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            try {
                if (result instanceof JSONObject)
                    on.OnCallBack((JSONObject) result);
                else if (result instanceof String)
                    on.OnError(result.toString());
            } catch (Exception ex) {
                Log.e("", "", ex);
            }
        }
    }

    public class RecyclerGesture implements RecyclerView.OnItemTouchListener {
        GestureDetector gestureDetector;
        OnResult on;

        public RecyclerGesture(Context context, final RecyclerView recyclerView, final OnResult on) {
            this.on = on;
            gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {

                @Override
                public boolean onDown(MotionEvent p1) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent p1) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent p1) {
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent p1, MotionEvent p2, float p3, float p4) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (view != null && on != null) {
                        on.OnItemLongClick(view, recyclerView.getChildPosition(view), searchRecyclerAdapter.locations);
                    }
                }

                @Override
                public boolean onFling(MotionEvent p1, MotionEvent p2, float p3, float p4) {
                    return false;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (view != null && on != null && gestureDetector.onTouchEvent(motionEvent)) {
                on.OnItemClick(view, recyclerView.getChildPosition(view), searchRecyclerAdapter.locations);
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

        }
    }
}
