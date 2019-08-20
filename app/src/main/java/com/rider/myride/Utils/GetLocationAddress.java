package com.rider.myride.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetLocationAddress {


    static int geocoderMaxResults = 1;

    public static List<Address> getGeocoderAddress(Context context, LatLng latLng) {

        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

        try {
            /**
             * Geocoder.getFromLocation - Returns an array of Addresses
             * that are known to describe the area immediately surrounding the given latitude and longitude.
             */
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, geocoderMaxResults);

            return addresses;
        } catch (IOException e) {
            return null;
            //e.printStackTrace();
        }


    }

    public static String getAddressLine(Context context, LatLng latLng) {
        List<Address> addresses = getGeocoderAddress(context, latLng);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);

            return addressLine;
        } else {
            return null;
        }
    }
}
