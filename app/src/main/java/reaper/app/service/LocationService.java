package reaper.app.service;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import reaper.conf.AppPreferences;
import reaper.conf.Constants;

/**
 * Created by Aditya on 23-04-2015.
 */
public class LocationService
{
    private Context context;
    private double latitude, longitude;
    private LocationManager locationManager;

    public LocationService(Context context)
    {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public Map<String, Double> getUserLocation()
    {
        Map<String, Double> locationMap = new HashMap<>();

        String latitudeStr = AppPreferences.get(context, Constants.Location.LATITUDE);
        String longitudeStr = AppPreferences.get(context, Constants.Location.LONGITUDE);

        if (latitudeStr != null && longitudeStr != null && !latitudeStr.isEmpty() && !longitudeStr.isEmpty())
        {
            latitude = Double.parseDouble(latitudeStr);
            longitude = Double.parseDouble(longitudeStr);

            locationMap = getLocationMap(latitude, longitude, true);
            return locationMap;
        }


        if (locationManager == null)
        {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        locationMap = getLocationFromProvider(LocationManager.GPS_PROVIDER);
        if (locationMap != null)
        {
            return locationMap;
        }


        locationMap = getLocationFromProvider(LocationManager.NETWORK_PROVIDER);
        if (locationMap != null)
        {
            return locationMap;
        }


        return null;
    }

    private Map<String, Double> getLocationFromProvider(String provider)
    {

        Map<String, Double> map = new HashMap<>();

        boolean isProviderEnabled = locationManager.isProviderEnabled(provider);
        if (isProviderEnabled)
        {
            Location location = locationManager.getLastKnownLocation(provider);


            if (location != null)
            {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();


                map = getLocationMap(latitude, longitude, false);

                return map;
            }

        }

        return null;
    }

    private Map<String, Double> getLocationMap(double latitude, double longitude, boolean isSaved)
    {
        Map<String, Double> mapLocation = new HashMap<>();

        mapLocation.put(Constants.Location.LATITUDE, latitude);
        mapLocation.put(Constants.Location.LONGITUDE, longitude);


        if (!isSaved)
        {
            AppPreferences.set(context, Constants.Location.LATITUDE, String.valueOf(latitude));
            AppPreferences.set(context, Constants.Location.LONGITUDE, String.valueOf(longitude));
        }
        return mapLocation;
    }


    public String getUserZone(Map<String, Double> latlongmap)
    {
        String zone = AppPreferences.get(context, Constants.Location.ZONE);

        if (zone == null)
        {
            latlongmap = getUserLocation();

            for (Map.Entry<String, Double> entry : latlongmap.entrySet())
            {
                if (entry.getKey() == Constants.Location.LATITUDE)
                {
                    latitude = entry.getValue();
                }

                if (entry.getKey() == Constants.Location.LONGITUDE)
                {
                    longitude = entry.getValue();
                }
            }

            zone = getZoneFromLocation(latitude, longitude);

        }

        return zone;
    }


    private String getZoneFromLocation(double latitude, double longitude)
    {
        if(latitude == 0 || longitude ==0){
            return null;
        }

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        String zone = null;

        try
        {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if ((addresses != null) && (addresses.size() > 0))
            {
                zone = addresses.get(0).getLocality();

                AppPreferences.set(context, Constants.Location.ZONE, zone);
            }
        }
        catch (IOException e)
        {
            return null;
        }

        return zone;
    }
}

