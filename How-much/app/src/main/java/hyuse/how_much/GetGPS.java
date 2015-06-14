package hyuse.how_much;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * Created by Eunjae Lee on 2015-05-26.
 */
public class GetGPS implements LocationListener {
    /* Eunjae Lee code */
    private LocationManager locationManager;
    private String latitude;
    private String longitude;
    private Criteria criteria;
    private String provider;
    private Context prev;

    public GetGPS(Context context){
        prev = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0, this);
        setMostRecentLocation(locationManager.getLastKnownLocation(provider));
    }
    private void setMostRecentLocation(Location lastKnownLocation) {}
    public String get_wido(){
        return latitude + " / " + longitude;
    }
    public String get_location() {
        String address = null;
        //위치정보를 활용하기 위한 구글 API 객체
        Geocoder geocoder = new Geocoder(prev, Locale.KOREA);
        //주소 목록을 담기 위한 HashMap
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(Float.parseFloat(latitude), Float.parseFloat(longitude), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null) {
            Log.e("getAddress", "주소 데이터 얻기 실패");
            return ",0,0";
        }
        if (list.size() > 0) {
            address = "," + latitude.substring(0, 5) + "," + longitude.substring(0, 6);
        }
        return address;
    }
    public void set_location(String str){}
    public void onLocationChanged(Location location) {
        double lon = (double) (location.getLongitude());/// * 1E6);
        double lat = (double) (location.getLatitude());// * 1E6);
        latitude = lat + "";
        longitude = lon + "";
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    public void onProviderEnabled(String provider) {}
    public void onProviderDisabled(String provider) {}
    /* Eunjae Lee code END */
}